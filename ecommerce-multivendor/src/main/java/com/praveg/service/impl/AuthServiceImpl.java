package com.praveg.service.impl;

import com.praveg.config.JwtProvider;
import com.praveg.domain.USER_ROLE;
import com.praveg.entity.Cart;
import com.praveg.entity.Seller;
import com.praveg.entity.User;
import com.praveg.entity.VerificationCode;
import com.praveg.logic.OtpUtil;
import com.praveg.repository.CartRepo;
import com.praveg.repository.SellerRepo;
import com.praveg.repository.UserRepo;
import com.praveg.repository.VerificationCodeRepo;
import com.praveg.request.LoginRequest;
import com.praveg.request.SignupRequest;
import com.praveg.response.AuthResponse;
import com.praveg.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final CartRepo cartRepo;

    private final JwtProvider jwtProvider;

    private final VerificationCodeRepo verificationCodeRepo;

    private final EmailService emailService;

    private final CustomeUserServiceImpl customeUserService;

    private final SellerRepo sellerRepo;

    // register method
    @Override
    public String createUser(SignupRequest req) throws Exception {

        //find verification code
        VerificationCode verificationCode = verificationCodeRepo.findByEmail(req.getEmail());

        //check code already present in db if already present then forward and null then send error
        //customer send otp and db otp match then again process and not match throw error
        if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("Wrong otp...");
        }

        User user = userRepo.findByEmail(req.getEmail());
        if(user==null){
            User createdUser = new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("9011292322");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepo.save(createdUser);

            // we create new user also create user cart
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepo.save(cart);
        }

        // User assign has role
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        //generate token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                req.getEmail(),
                null,
                authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    //send otp method
    @Override
    public void sendLoginOtp(String email, USER_ROLE role) throws Exception {

        String SIGNING_PREFIX = "signing_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)) {
                Seller seller = sellerRepo.findByEmail(email);
                if(seller == null) {
                    throw new Exception("Seller not found");
                }
            }
                else {
                    //find user with email
                System.out.println("Email - " +email);
                    User user = userRepo.findByEmail(email);
                    if(user==null){
                        throw new Exception("User not exist with provided email - ");
                    }
                }
        }

        //verifi is exist
        VerificationCode isExist = verificationCodeRepo.findByEmail(email);

        //delete user existing otp and create new otp
        if(isExist!=null){
            verificationCodeRepo.delete(isExist);
        }

        //generating new otp create class for logic and call here
        String otp = OtpUtil.generateOtp();

        //generating otp to set new otp and save db
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepo.save(verificationCode);

        //after save otp in db then send email to user
        String subject = "praveg bazaar login/signup otp ";
        String text = "your login/signup otp is - " + otp;

        emailService.sendVerificationOtpEmail(email,otp,subject,text);  //send email to user
    }

    // login method
    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        String username = req.getEmail();
        String otp = req.getOtp();

        // create method for check user is authenticated or not
        Authentication authentication = authenticate(username,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //generate token for login user
        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Successfully...");

        // set role access roll
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    // verify otp
    private Authentication authenticate(String username, String otp) throws Exception {
        UserDetails userDetails = customeUserService.loadUserByUsername(username);

        String SELLER_PREFIX ="seller_";
        if(username.startsWith(SELLER_PREFIX)){
           username = username.substring(SELLER_PREFIX.length());
        }

        if(userDetails==null){
            throw new BadCredentialsException("Invalid username !");
        }

        VerificationCode verificationCode = verificationCodeRepo.findByEmail(username);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp !");
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,null,userDetails.getAuthorities());
    }
}

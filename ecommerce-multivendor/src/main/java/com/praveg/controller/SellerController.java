package com.praveg.controller;

import com.praveg.config.JwtProvider;
import com.praveg.domain.AccountStatus;
import com.praveg.entity.Seller;
import com.praveg.entity.SellerReport;
import com.praveg.entity.VerificationCode;
import com.praveg.exception.SellerException;
import com.praveg.logic.OtpUtil;
import com.praveg.repository.VerificationCodeRepo;
import com.praveg.request.LoginRequest;
import com.praveg.response.AuthResponse;
import com.praveg.service.AuthService;
import com.praveg.service.SellerReportService;
import com.praveg.service.SellerService;
import com.praveg.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepo verificationCodeRepo;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    private final SellerReportService sellerReportService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        String otp = req.getOtp();
        String email = req.getEmail();
        req.setEmail("seller_" +email);
        System.out.println("otp -" + otp + "email -" +email);
        AuthResponse authResponse = authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }

    //check
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception{
        VerificationCode verificationCode = verificationCodeRepo.findByOtp(otp);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp");
        }
        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    // check
    @PostMapping("/create")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller)throws Exception, MessagingException{
        Seller saveSaller = sellerService.createSeller(seller);
        //generating new otp create class for logic and call here
        String otp = OtpUtil.generateOtp();

        //generating otp to set new otp and save db
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepo.save(verificationCode);
        String subject = "Praveg bazar verification code";
        String text = "Welcome to praveg bazar, verify your account using this link";
        String frontend_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),
                subject,text + frontend_url);
        return new ResponseEntity<>(saveSaller, HttpStatus.CREATED);
    }

    //check
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    // check
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt)
            throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller = sellerService.getSellerByEmail(email); // getSellerProfile(jwt); remove 1st line
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    // *** report
    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt)
            throws Exception {
        Seller seller = sellerService.getSellerProfile(jwt);
        SellerReport report = sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    //check
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(@RequestParam(required = false)AccountStatus status){
        List<Seller> sellers = sellerService.getAllSellers(status);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    //check
    @PatchMapping("/update")
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller)
            throws Exception {
        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updateSeller = sellerService.updateSeller(profile.getId(), seller);
        return new ResponseEntity<>(updateSeller, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();

    }
}

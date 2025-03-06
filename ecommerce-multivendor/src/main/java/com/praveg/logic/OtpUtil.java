package com.praveg.logic;

import java.util.Random;

public class OtpUtil {

    //generating new otp logic here
    public static String generateOtp(){
        int otplength = 6;

        Random random = new Random();

        StringBuilder otp = new StringBuilder(otplength);

        for(int i = 0 ; i<otplength; i++){
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}

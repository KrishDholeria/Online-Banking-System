package com.project.backendrestapi.service;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SMSService {
    String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    public String genrateOTP(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Verification verification = Verification.creator(
                        "VAb6f3d99acb637bd76c2f054e0cee17e2", // this is your verification sid
                        "+919664846536", //this is your Twilio verified recipient phone number
                        "sms") // this is your channel type
                .create();

        System.out.println(verification.getStatus());

        log.info("OTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());
        return "OTP generated and sent succesfully";
    }

    public  String verifyOTP(String otpString){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {

            VerificationCheck verificationCheck = VerificationCheck.creator(
                            "VAb6f3d99acb637bd76c2f054e0cee17e2")
                    .setTo("+919664846536")
                    .setCode(otpString)
                    .create();

            System.out.println(verificationCheck.getStatus());

        } catch (Exception e) {
            return "Invalid OTP!!!!!";
        }
        return "OTP verified succesfully";
    }
}

package tn.esprit.springfever.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j

public class LoggingAspect {


    @Autowired
    private MailConfiguration mailConfiguration;

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);



    @After("execution(* tn.esprit.springfever.controllers.UserController.getAllUsers())")
    public void logGetAllUsers(JoinPoint joinPoint) {
        log.info("\n******************************************************************************\n" +
                "GET ALL USERS \n"+"******************************************************************************");
    }

    @After("execution(* tn.esprit.springfever.controllers.AuthController.signUpV3(..))")
    public void logGetUserById(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String email = (String) args[4]; // assuming the username is the second argument
        String username=(String) args[2];
        log.info("\n******************************************************************************\n" +
                "A USER HAS BEEN ADDED \n" +
                "email: {}\n" +
                "******************************************************************************", email);

        String emailBody = "Hello " + username + ",\n\n" +
                "\"Welcome to ESPRIT University! We're thrilled to have you join our community of learners. Our mission is to provide you with the highest quality education and support as you pursue your academic and career goals. Whether you're interested in computer science, engineering, business, or another field, our expert faculty and resources are here to help you succeed. We look forward to seeing all that you will accomplish during your time with us. ";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("WELCOME TO ESPRIT");
        message.setText(emailBody);
        message.setTo(email);
        mailConfiguration.sendEmail(message);
    }

}
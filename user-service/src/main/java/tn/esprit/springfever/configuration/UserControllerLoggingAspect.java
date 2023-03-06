package tn.esprit.springfever.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j

public class UserControllerLoggingAspect {


    private static final Logger logger = LogManager.getLogger(UserControllerLoggingAspect.class);

    @After("execution(* tn.esprit.springfever.controllers.UserController.getAllUsers())")
    public void logGetAllUsers(JoinPoint joinPoint) {
        log.info("\n******************************************************************************\n" +
                "GET ALL USERS \n"+"******************************************************************************");
    }

    @After("execution(* tn.esprit.springfever.controllers.UserController.signUpV3())")
    public void logGetUserById(JoinPoint joinPoint) {


        log.info("\n******************************************************************************\n" +
                "A  USER HAS BEEN ADDED \n"+"******************************************************************************");
    }

}
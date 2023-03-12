package tn.esprit.springfever.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.configuration.MailConfiguration;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.UserInterest;
import tn.esprit.springfever.repositories.InterestRepository;
import tn.esprit.springfever.services.implementations.MatchingService;
import tn.esprit.springfever.services.implementations.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@Aspect
@Slf4j
public class PostAspect {
    private Long delay = 24L;
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private MailConfiguration mailConfiguration;
    @Autowired
    private UserService userService;

}

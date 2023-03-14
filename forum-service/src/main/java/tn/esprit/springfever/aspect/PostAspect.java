package tn.esprit.springfever.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.configuration.MailConfiguration;
import tn.esprit.springfever.dto.PostDTO;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.UserInterest;
import tn.esprit.springfever.repositories.InterestRepository;
import tn.esprit.springfever.services.implementations.MatchingService;
import tn.esprit.springfever.services.implementations.UserService;
import tn.esprit.springfever.utils.MailTemplating;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
    @Autowired
    private MailTemplating mailTemplating;

    @Async
    @AfterReturning(pointcut ="execution(* tn.esprit.springfever.controllers.PostController.addPost(..))", returning = "result")
    public void captureControllerResult(ResponseEntity<?> result) throws Exception {
        if(result.getStatusCodeValue() == 201){
            CompletableFuture.runAsync(() -> {
                List<String> topics = matchingService.extractTopicsFromPost(((PostDTO)result.getBody()).getTitle()+((PostDTO)result.getBody()).getTopic()+((PostDTO)result.getBody()).getContent());
                topics.add((((PostDTO) result.getBody()).getTopic()));
                List<UserInterest> interests = interestRepository.findByTopicIn(topics);
                List<Long> ids = new ArrayList<>();
                for (UserInterest userInterest : interests){
                   ids.add(userInterest.getUser()) ;
                }
                try {
                    List<UserDTO> users = userService.getUserDetailsFromIds(ids);
                    MimeMessage message = mailConfiguration.getJavaMailSender().createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setSubject("Check out this post by: " +" "+ ((PostDTO)result.getBody()).getUser().getUsername());
                    helper.setText(mailTemplating.getHtml(mailTemplating.changeHtml((PostDTO) result.getBody())),true);
                    for (UserDTO user : users){
                        if(user.getEmail() !=null){
                            helper.setTo(user.getEmail());
                            mailConfiguration.sendEmail(message);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }


            });
        }
    }

}

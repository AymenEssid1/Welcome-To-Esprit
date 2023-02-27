package tn.esprit.springfever.Services.Implementation;



import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

 import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import java.lang.System ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.analyzer.SentimentAnalyzer;
import tn.esprit.springfever.analyzer.SentimentPolarities;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.enums.ClaimStatus;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.UserRepository;
import edu.stanford.nlp.sentiment.SentimentTraining ;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import java.util.Properties;


@Service
@Slf4j
public class ServiceClaimsImpl implements IServiceClaims {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ClaimRepository claimRepository;
    @Autowired
    ClaimMapper claimMapper;
    @Autowired
    JavaMailSender javaMailSender;


@Override
    public String analyzeSentiment(String text) {
    final SentimentPolarities sentimentPolarities = SentimentAnalyzer.getScoresFor(
            text);
    System.out.println(sentimentPolarities);
        return "PositivePolarity: " + sentimentPolarities.getPositivePolarity() +
                "NegativePolarity: " + sentimentPolarities.getNegativePolarity()+
                "NeutralPolarity: " + sentimentPolarities.getNeutralPolarity() +
                "CompoundPolarity: " + sentimentPolarities.getCompoundPolarity() ;
    }

    @Override
    public String sentFeedback( Long idClaim, String feedback) {
        Claim existingClaim = claimRepository.findById(idClaim).orElse(null);
        if(existingClaim!=null) {
            existingClaim.setFeedback(feedback);
            log.info("feedback sent");
            claimRepository.save(existingClaim);
            return this.analyzeSentiment(feedback) ;
        }
        else {
            log.info("claim not found");
            return "claim not found" ;
        }
    }





    @Override
    public Claim addClaim(Claim claim) throws IOException {
        // Send email notification to user
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("New claim submitted");
        message.setText("A new claim has been submitted.");
        message.setTo("springforfever@gmail.com");
        javaMailSender.send(message);
        log.info("claim was successfully added !");
        claimRepository.save(claim);
        return claim;
    }


    // pagination
    @Override
    public List<Claim> getAllClaims() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Claim> claimPage = claimRepository.findAll(pageRequest);
        return claimPage.getContent();
    }
    //////
    @Override
    public boolean deleteClaim(Long idclaim) {
        Claim existingClaim = claimRepository.findById(idclaim).orElse(null);
        if (existingClaim != null) {
            claimRepository.delete(existingClaim);
            log.info("claim deleted");
            return true;
        } else {
            log.info(" this claim is not existing");
        }
        return false;
    }


    /*   why we use  dto to update entity ?

    Now suppose we have more than a hundred phone fields in our object.
    Writing a method that pours the data from DTO to our entity,
    as we did before (set each attribute by its own ) , could be annoying and pretty unmaintainable.

    Nevertheless, we can get over this issue using a mapping strategy,
    and specifically with the MapStruct implementation.

  ***********  ==> so to conclude dto provide maintainability  ***********
     */


    @Override
    public Claim updateClaim(Long idClaim, ClaimDTO claimDto) {
        Claim claim = claimRepository.findById(idClaim).orElse(null);
        if (claim != null) {
            claimMapper.updateClaimFromDto(claimDto, claim);
            claimRepository.save(claim);
            log.info("claim was successfully updated !");
        } else {
            log.info("claim not found !");
        }
        return claim;
    }

    @Override
    public List<Claim> getClaimsByUser(String username) {
        User existingUser = userRepository.findByUsername(username).orElse(null);
        if (existingUser != null) {
            log.info("claims list of the user  : " + existingUser.getUsername());
            return claimRepository.getClaimByUserUsername(username);
        } else {
            log.info("user not found ");
        }

        return null;
    }


    @Cacheable(cacheNames = "findClaimById")
    @Override
    public Claim findById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    @Override
    public Claim treatClaim(Long id, String descision) {
        Claim claim = claimRepository.findById(id).orElse(null);
        if (claim != null) {
            claim.setDecision(descision);
            claim.setDateTreatingClaim(new Date());
            claimRepository.save(claim);
            log.info("claim was treated ");

        } else {
            log.info("claim not found ");
        }
        return claim;
    }

    @Override
    public long getTimeTreatmentClaim(Long id) {
        Claim claim = claimRepository.findById(id).orElse(null);
        // Calculate the period between the two dates
        long diffMillis = claim.getDateTreatingClaim().getTime() - claim.getDateSendingClaim().getTime();
        long diffDays = diffMillis / (24 * 60 * 60 * 1000);
        return diffMillis;
    }

    @Override
    public long predicateTreatmentClaim(Long  id ) {
        Claim claim = claimRepository.findById(id).orElse(null);
        List<Claim> claims = claimRepository.findAllByClaimSubjectAndClaimStatus(claim.getClaimSubject(), ClaimStatus.treated);
        long estimatedPeriod =0 ;
        for(Claim c : claims) {
             estimatedPeriod += (c.getDateTreatingClaim().getTime() - c.getDateSendingClaim().getTime())/(60*60*1000) ;
          }
        long avg = estimatedPeriod/claims.size();
        return avg ;
    }








}


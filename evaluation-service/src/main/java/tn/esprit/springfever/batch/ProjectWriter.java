 package tn.esprit.springfever.batch;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.analyzer.SentimentAnalyzer;
import tn.esprit.springfever.analyzer.SentimentPolarities;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.Log;
import tn.esprit.springfever.repositories.LogRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j

public class ProjectWriter implements ItemWriter<Claim> {
 @Autowired
 LogRepository logRepository ;

    public void write(List<? extends Claim> claims) {

        List<Claim> listeClaim  = new ArrayList<>();
        log.info("size all : " + claims.size());

        double sumpos=0,sumNeg=0,sumComp=0,sumNeut = 0 ;
        for (Claim claim: claims) {
            log.info(claim.toString());
            log.info("liste des feedback : ");
            log.info(claim.getFeedback());
            if(!(claim.getFeedback()==null)) {
                log.info("calim eli feedback mteha  mech null  : " + claim.getIdClaim());
                listeClaim.add(claim);
            }
        }
        log.info("size feedback not null : " + listeClaim.size());


        for (Claim claim : listeClaim) {
            final SentimentPolarities sentimentPolarities = SentimentAnalyzer.getScoresFor(claim.getFeedback());
            sumpos += sentimentPolarities.getPositivePolarity() ;
            sumComp+= sentimentPolarities.getCompoundPolarity();
            sumNeg += sentimentPolarities.getNegativePolarity() ;
            sumNeut+= sentimentPolarities.getNeutralPolarity();
        }


        log.info(String.valueOf(sumComp));
        Log logInfo = new Log() ;
        logInfo.setPositive(sumpos/listeClaim.size());
        logInfo.setNegative(sumNeg/listeClaim.size());
        logInfo.setNeutral(sumNeut/listeClaim.size());
        logInfo.setCompound(sumComp/listeClaim.size());
        logInfo.setDateLog(new Date());
        logRepository.save(logInfo);
        log.info("Feedbacks Bilan :  \n" +
                " Average of Postive feedbacks :  \n" +
                String.valueOf(sumpos/listeClaim.size()) + "\n" +
                " Average of negative feedbacks :   \n" +
                String.valueOf(sumNeg/listeClaim.size()) + "\n" +
                " Average of compound feedbacks :   \n" +
                String.valueOf(sumComp/listeClaim.size()) + "\n" +
                " Average of neutral feedbacks :   \n" +
                String.valueOf(sumNeut/listeClaim.size()) + "\n"

        );








    }
}
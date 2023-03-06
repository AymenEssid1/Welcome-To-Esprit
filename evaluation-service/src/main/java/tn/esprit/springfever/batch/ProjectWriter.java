 package tn.esprit.springfever.batch;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.entities.Log;
import tn.esprit.springfever.repositories.LogRepository;

import java.util.Date;
import java.util.List;

@Slf4j

public class ProjectWriter implements ItemWriter<Faq> {
 @Autowired
 LogRepository logRepository ;

    /* 13. écrire nos données dans la base de données*/
    public void write(List<? extends Faq> faqs) {
        log.info("dans cette étape, nous pourrons stocker nos informations" +
                "dans une autre base de données, un fichier externe ou la meme" +
                " base de données si nous le souhaitons");
        for (Faq faq : faqs) {
           // log.info(faq.toString());
            log.info(String.valueOf(faqs.size()));

             Log log = new Log() ;
            log.setDateLog(new Date());
            logRepository.save(log);
        }


    }
}
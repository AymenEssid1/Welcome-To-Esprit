package tn.esprit.springfever.batch;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.batch.BatchLauncher;

@Component
@Slf4j
@AllArgsConstructor
public class Scheduler {

    private BatchLauncher batchLauncher;

    
    /*1. Lancer le batch (traitement de lots de données) grace au batch runner */

    @Scheduled(fixedDelay = 100000)
    public void perform() throws Exception {

        log.info("Batch programmé pour tourner toutes les 5 minutes");
        batchLauncher.run();
    }
}

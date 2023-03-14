package tn.esprit.springfever.configuration;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.springfever.batch.ProjectProcessor;
import tn.esprit.springfever.batch.ProjectReader;
import tn.esprit.springfever.batch.ProjectWriter;
import tn.esprit.springfever.entities.User;



@Configuration
/*3. Activer le traitement par lot */
@EnableBatchProcessing
@Slf4j
@AllArgsConstructor
public class ProjectBatchConfig {

    /*4. Création des variables de notre batch (nom job, nom step) */
    private static final String JOB_NAME = "listProjectJob";
    private static final String STEP_NAME = "processingStep";

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;


    @Autowired
    private ProjectReader projectReader;
    @Bean
    public Job listProjectsJob(Step step1) {

        return jobBuilderFactory.get(JOB_NAME).start(step1).build();
    }

    /*6 Créer le step associé au job et le lancer */
    @Bean
    public Step projectStep() {

        try {
            return stepBuilderFactory.get(STEP_NAME)
                    .<User, User>chunk(2).reader(projectReader.read())
                    .processor(projectItemProcessor()).writer(projectItemWriter())
                    .exceptionHandler((context, throwable) -> log.error("Skipping record on file. cause="+ throwable.getCause()))
                    .build();
        } catch (Exception e) {
            log.error("End Batch Step");
            return stepBuilderFactory.get(STEP_NAME).chunk(2).build();
        }

    }




    /* 7. étape 1 (ItemReader) fait appel à la classe ProjectReader
     * qui se charge de modifier la logique des données selon
     * nos besoins métiers */



    @Bean
    public ItemProcessor<User, User> projectItemProcessor() {
        return new ProjectProcessor();
    }


    @Bean
    public ItemWriter<User> projectItemWriter() {
        return new ProjectWriter();
    }
}

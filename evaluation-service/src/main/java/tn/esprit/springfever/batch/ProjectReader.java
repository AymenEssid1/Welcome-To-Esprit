package tn.esprit.springfever.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Faq;


@Slf4j
public class ProjectReader implements ItemReader<ItemReader<Faq>> {
    @Autowired
    private IServiceFaq iServiceFaq;


    /*4. Création des variables de notre batch (nom fichier, nom lecteur ) */
    private static final String FILE_NAME = "project.csv";
    private static final String READER_NAME = "projectItemReader";

    /*5. Lister les champs que nous souhaitons parser dans le
     * fichier excel*/
    private String names = "id,question,response,id" ; // todo4


    /*6. Définir la stratégie de délimitation des différents champs*/
    private String delimiter = ",";


    /*10. étape 1 (ItemReader) Créer le reader pour récupérer les données depuis
     * le fichier csv*/
    @Override
    public ItemReader<Faq> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.error("Start Batch Item Reader");
        FlatFileItemReader<Faq> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(FILE_NAME));
        reader.setName(READER_NAME);
        reader.setLinesToSkip(1);
        /*11. récupérer les données ligne par ligne du fichier excel */
        reader.setLineMapper(projectLineMapper());


        return reader;
    }

    /*11. récupérer les données ligne par ligne du fichier excel */
    public LineMapper<Faq> projectLineMapper() {

        log.info("Start Batch Line Mapper");

        final DefaultLineMapper<Faq> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);

        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(names.split(","));
        defaultLineMapper.setLineTokenizer(lineTokenizer);

        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            log.info("**********************");
            log.info(fieldSet.readRawString(1));
            log.info(fieldSet.readRawString(2));


            log.info(String.valueOf(fieldSet.readBoolean(2))) ;
            log.info(String.valueOf(fieldSet.readBoolean(0))) ;
            log.info(fieldSet.readRawString(3));



            if (!fieldSet.readRawString(3).equals("")) {
                // on a récupérer le parametre terminated du fichier projet.csv et verifier que sa valeur est true
                 return iServiceFaq.findFaqById(Long.parseLong(fieldSet.readRawString(3)));

            }
            else {
                return null;
            }

        });
        return defaultLineMapper;
    }
}

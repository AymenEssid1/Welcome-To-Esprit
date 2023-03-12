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
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Claim;


@Slf4j
public class ProjectReader implements ItemReader<ItemReader<Claim>> {
    @Autowired
    private IServiceClaims iServiceClaims;
    private static final String FILE_NAME = "projectClaims.csv";
    private static final String READER_NAME = "projectItemReader";
    private String names = "claimRate,claimStatus,claimSubject,dateSendingClaim,dateTreatingClaim,decision,description,feedback,idClaim" ;
    //claimRate,claimStatus,claimSubject,dateSendingClaim
    private String delimiter = ",";
    @Override
    public ItemReader<Claim> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        log.error("Start Batch Item Reader");
        FlatFileItemReader<Claim> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource(FILE_NAME));
        reader.setName(READER_NAME);
        reader.setLinesToSkip(1);
        reader.setLineMapper(projectLineMapper());
        return reader;
    }
    public LineMapper<Claim> projectLineMapper() {
        log.info("Start Batch Line Mapper");
        final DefaultLineMapper<Claim> defaultLineMapper = new DefaultLineMapper<>();
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(delimiter);
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(names.split(","));
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSet -> {
            log.info("**********************");
            if (!fieldSet.readRawString(8).equals("")) {
                 return iServiceClaims.findClaimById(Long.parseLong(fieldSet.readRawString(8)));
            }
            else {
                return null;
            }

        });
        return defaultLineMapper;
    }
}

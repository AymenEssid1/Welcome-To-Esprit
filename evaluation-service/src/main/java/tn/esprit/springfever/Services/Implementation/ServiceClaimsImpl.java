package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.analyzer.SentimentAnalyzer;
import tn.esprit.springfever.analyzer.SentimentPolarities;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.enums.ClaimStatus;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.UserRepository;

import java.util.*;


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
    @Autowired
    SmsService smsService;


    @Override
    public String analyzeSentiment(String text) {
        final SentimentPolarities sentimentPolarities = SentimentAnalyzer.getScoresFor(
                text);
        return "PositivePolarity: " + sentimentPolarities.getPositivePolarity() +
                "NegativePolarity: " + sentimentPolarities.getNegativePolarity() +
                "NeutralPolarity: " + sentimentPolarities.getNeutralPolarity() +
                "CompoundPolarity: " + sentimentPolarities.getCompoundPolarity();
    }

    @Override
    public String sentFeedback(Long idClaim, String feedback) {
        Claim existingClaim = claimRepository.findById(idClaim).orElse(null);
        if (existingClaim != null) {
            existingClaim.setFeedback(feedback);
            log.info("feedback sent");
            claimRepository.save(existingClaim);
            return this.analyzeSentiment(feedback);
        } else {
            log.info("claim not found");
            return "claim not found";
        }

    }

    //,c@Scheduled(fixedRate = 10000)
    public void bilanFeedback() {
        List<Claim> listeClaim = new ArrayList<>();


        double sumpos = 0, sumNeg = 0, sumComp = 0, sumNeut = 0;
        for (Claim claim : claimRepository.findAll()) {
            if (claim.getFeedback() != null) {
                listeClaim.add(claim);
            }
        }

        for (Claim claim : listeClaim) {
            final SentimentPolarities sentimentPolarities = SentimentAnalyzer.getScoresFor(claim.getFeedback());
            sumpos += sentimentPolarities.getPositivePolarity();
            sumComp += sentimentPolarities.getCompoundPolarity();
            sumNeg += sentimentPolarities.getNegativePolarity();
            sumNeut += sentimentPolarities.getNeutralPolarity();
        }

        log.info("Feedbacks Bilan :  \n" +
                " Average of Postive feedbacks :  \n" +
                sumpos / listeClaim.size() + "\n" +
                " Average of negative feedbacks :   \n" +
                sumNeg / listeClaim.size() + "\n" +
                " Average of compound feedbacks :   \n" +
                sumComp / listeClaim.size() + "\n" +
                " Average of neutral feedbacks :   \n" +
                sumNeut / listeClaim.size() + "\n"

        );


    }


    @Override
    public Claim addClaim(Claim claim) throws IOException {

        if ((this.badWordsFound(claim.getDescription()))) {
            // send sms
            smsService.sendSmsvalide("23583310", "the claim have bad words please respect the rules ");
            // Send email notification to user
            SimpleMailMessage warning = new SimpleMailMessage();
            warning.setSubject("Warning");
            warning.setText("you have not respect the rules of use of our website ");
            warning.setTo("springforfever@gmail.com"); // to change with the email of the user
            javaMailSender.send(warning);

        } else {


            // Send email notification to user
            smsService.sendSmsvalide("23583310", "the claim have been submitted successfully ");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("New claim submitted");
            message.setText("A new claim has been submitted.");
            message.setTo("springforfever@gmail.com"); // to change with the email of the user
            javaMailSender.send(message);
            log.info("claim was successfully added !");
        }

        return claimRepository.save(claim);
    }

    // pagination
    @Override
    public List<Claim> getAllClaims() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        Page<Claim> claimPage = claimRepository.findAll(pageRequest);
        return claimPage.getContent();
    }

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
    public List<Claim> getClaimsByUser(Long id) {
        return claimRepository.findByUser(id);

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
            claim.setClaimStatus(ClaimStatus.treated);
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
        if(claim!=null) {
            if(claim.getDateTreatingClaim()!=null && claim.getDateSendingClaim()!=null) {
                long diffMillis = claim.getDateTreatingClaim().getTime() - claim.getDateSendingClaim().getTime();
                long diffDays = diffMillis / (24 * 60 * 60 * 1000);
                long diffHours = diffMillis / (60 * 60 * 1000);
                log.info("the estimated time :  in hours : " + diffHours +"\n"
                + "in days : " + diffDays + " \n"  );
                return diffHours;
            }
            else
            {
                log.info("claim not yet  treated ");
                return 0 ;
            }
        }
        else {
            log.info("claim not found ");
            return 0 ;
        }
    }

    @Override
    public long predicateTreatmentClaim(Long id) {
        Claim claim = claimRepository.findById(id).orElse(null);
        if(claim!=null) {
            List<Claim> claims = claimRepository.findAllByClaimSubjectAndClaimStatus(claim.getClaimSubject(), ClaimStatus.treated);
            long estimatedPeriod = 0;
            for (Claim c : claims) {
                estimatedPeriod += (c.getDateTreatingClaim().getTime() - c.getDateSendingClaim().getTime()) / (60 * 60 * 1000);
            }
            return estimatedPeriod / claims.size();
        }
        else {
            log.info("claim not found ");
            return 0 ;
        }
    }


    public boolean badWordsFound(String input) throws IOException {
        String userDirectory = System.getProperty("user.dir");

        // Tokenize the claim body
        InputStream modelIn = new FileInputStream(userDirectory+"\\evaluation-service\\en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);
        if (input != null) {
            String[] tokens = tokenizer.tokenize(input);
            for (String word : tokens) {
                for (String badWord : this.excelToStringArray()) {

                    if (word.equals(badWord))
                        return true;
                }
            }
        }
        return false;
    }

    // at 12:00 AM every day
    @Scheduled(cron = "0 0 0 * * ?")
    @Override
    public void deleteClaimHavingBadWords() throws IOException {
        List<Claim> allClaims = claimRepository.findAll();
        for (Claim claim : allClaims) {

            if (this.badWordsFound(claim.getDescription())) {
                this.deleteClaim(claim.getIdClaim());
            }

        }
    }


    public List<String> excelToStringArray() throws IOException {
        List<String> badWords = new ArrayList<>();
        String userDirectory = System.getProperty("user.dir");
        FileInputStream file = new FileInputStream(userDirectory+"\\evaluation-service\\assets\\excel_files\\badwords.xlsx");
        Workbook workbook = new XSSFWorkbook(file);

        // Get the first sheet of the workbook
        Sheet sheet = workbook.getSheetAt(0);
        // Loop through the rows of the sheet
        for (Row row : sheet) {
            badWords.add(row.getCell(0).getStringCellValue());
        }
        return badWords;
    }


    public Claim findClaimById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }


}


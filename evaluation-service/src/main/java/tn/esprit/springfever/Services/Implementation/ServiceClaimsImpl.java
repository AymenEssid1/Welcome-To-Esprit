package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.UserRepository;
 import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ServiceClaimsImpl implements IServiceClaims {

    @Autowired
    UserRepository userRepository ;
    @Autowired
    ClaimRepository claimRepository ;
    @Autowired
    ClaimMapper claimMapper;
    @Autowired
    JavaMailSender javaMailSender;

     @Override
    public Claim addClaim(Claim claim) throws IOException {
             // Send email notification to user
         SimpleMailMessage message = new SimpleMailMessage();
         message.setSubject("New claim submitted");
         message.setText("A new claim has been submitted.");
         message.setTo("springforfever@gmail.com\n");
         javaMailSender.send(message);
          log.info("claim was successfully added !");
         claimRepository.save(claim);
        return claim;
     }

    @Override
    public List<Claim> getAllClaims() {return claimRepository.findAll();}


    @Override
    public boolean deleteClaim(Long idclaim) {
         Claim existingClaim = claimRepository.findById(idclaim).orElse(null);
         if(existingClaim!=null) {
             claimRepository.delete(existingClaim);
             log.info("claim deleted");
             return true ;
         }
      else {log.info(" this claim is not existing");}
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
        }
        else {
            log.info("claim not found !");
         }
        return claim;
    }

    @Override
    public List<Claim> getClaimsByUser(String username ) {
        User existingUser = userRepository.findByUsername(username).orElse(null) ;
        if(existingUser!=null) {
            log.info("claims list of the user  : " + existingUser.getUsername() );
            return claimRepository.getClaimByUserUsername(username);
        }
        else {log.info("user not found ");}

        return  null ;
    }


    @Cacheable(cacheNames = "findClaimById")
    @Override
    public Claim findById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }

    @Override
    public Claim treatClaim(Long id, String descision) {
        Claim claim = claimRepository.findById(id).orElse(null);
        if(claim!=null) {
            claim.setDecision(descision);
            claim.setDateTreatingClaim(new Date());
            claimRepository.save(claim);
            log.info("claim was treated ");

        }
        else {
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
    public long predicateTreatmetnClaim() {
        return 0;
    }
}


package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
  import opennlp.tools.tokenize.Tokenizer;
 import opennlp.tools.tokenize.TokenizerME;
 import opennlp.tools.tokenize.TokenizerModel;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Services.Interfaces.ClaimMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.entities.Claim;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.ClaimRepository;
import tn.esprit.springfever.repositories.UserRepository;

 import java.io.FileInputStream;
 import java.io.IOException;
 import java.io.InputStream;
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


     @Override
    public Claim addClaim(Claim claim) throws IOException {

        // Load the tokenizer model
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);

        // Create a new tokenizer
        Tokenizer tokenizer = new TokenizerME(model);

        // Analyze the keywords from the student's diploma
        String diploma = "Diploma in Computer Science with a specialization in Web Development";
        String[] tokens = tokenizer.tokenize(diploma);

        // Display the separated keywords
        for (String token : tokens)
            log.info(token);
        //log takone
         log.info("claim was successfully added !");
        return claimRepository.save(claim);
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
        log.info(" this claim is not existing");
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
        log.info("claim not found !");
        return  claim;

    }

    @Override
    public List<Claim> getClaimsByUser(String username ) {
        User existingUser = userRepository.findByUsername(username).orElse(null) ;
        if(existingUser!=null) {
            log.info("claims list of the user  : " + existingUser.getUsername() );
            return claimRepository.getClaimByUserUsername(username);
        }
        log.info("user not found ");
        return  null ;
    }


    @Cacheable(cacheNames = "findClaimById")
    @Override
    public Claim findById(Long id) {
        return claimRepository.findById(id).orElse(null);
    }
}


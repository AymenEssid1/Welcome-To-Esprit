package tn.esprit.springfever.Services.Interfaces;


import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.entities.Claim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IServiceClaims {

 public Claim addClaim(Claim claim) throws IOException; // add by user 
 public  List<Claim> getAllClaims() ;
 public  boolean deleteClaim(Long idClaim) ;
 public Claim updateClaim(Long idClaim , ClaimDTO claimDTO) ;
 public  List<Claim> getClaimsByUser(String username) ;
 public Claim findById(Long id) ;
 public Claim treatClaim( Long id , String descision);
 public long getTimeTreatmentClaim(Long id );
 public long predicateTreatmentClaim(Long id ) ;
 public String analyzeSentiment(String text) ;
 public String sentFeedback(Long idClaim,String feedback);
 public  boolean badWordsFound(String input ) throws IOException;
 public void deleteClaimHavingBadWords() throws IOException;

}

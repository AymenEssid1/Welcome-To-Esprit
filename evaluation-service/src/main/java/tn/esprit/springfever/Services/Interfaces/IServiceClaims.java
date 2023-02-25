package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.entities.Claim;

import java.io.IOException;
import java.util.List;

public interface IServiceClaims {

 public Claim addClaim(Claim claim) throws IOException; // add by user 
 public  List<Claim> getAllClaims() ;
 public  boolean deleteClaim(Long idClaim) ;
 public Claim updateClaim(Long idClaim , ClaimDTO claimDTO) ;
 public  List<Claim> getClaimsByUser(String username) ;
 public Claim findById(Long id) ;
 public Claim treatClaim( Long id , String descision);
 public long getTimeTreatmentClaim(Long id );
 public long predicateTreatmetnClaim() ;




}

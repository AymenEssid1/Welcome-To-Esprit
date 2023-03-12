package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.Deliberation;

import java.util.List;

public interface IServiceDeliberation {

 public Deliberation addDeliberation(Deliberation deliberation) ;
 public  List<Deliberation> getAllDeliberations() ;
 public  String deleteDeliberation(Long idDeliberation) ;
 public Deliberation updateDeliberation(Long idDeliberation , Deliberation deliberation ) ;
 public Deliberation getDeliberationOfUser(String username) ;
 public  Deliberation getDeliberationById(Long id);
}

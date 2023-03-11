package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.UserEvaluation;

import java.util.List;

public interface IServiceUser {

 public List<UserEvaluation> getAllUsers() ;
 public String getusernamefromtoken(String header) ;


}

package tn.esprit.springfever.Services.Interfaces;


import tn.esprit.springfever.entities.User;

import java.util.List;

public interface IServiceUser {

 public List<User> getAllUsers() ;
 public String getusernamefromtoken(String header) ;


}

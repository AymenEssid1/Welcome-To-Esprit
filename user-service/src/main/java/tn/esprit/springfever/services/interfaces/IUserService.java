package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.User;

import java.util.List;

public interface IUserService {
    public List<User> getAllUsers() ;
    public String getusernamefromtoken(String header) ;
    public User getuserfromtoken(String header) ;
}

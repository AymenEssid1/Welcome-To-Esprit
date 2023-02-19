package tn.esprit.springfever.Services.Implementation;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceUser;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.UserRepository;

import java.util.List;


@Service
@Slf4j
public class ServiceUserImpl implements IServiceUser {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    public String getusernamefromtoken(String header) {
       // jwtUtils.setJwtSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        return   jwtUtils.getUserNameFromJwtToken(header) ;


    }





}


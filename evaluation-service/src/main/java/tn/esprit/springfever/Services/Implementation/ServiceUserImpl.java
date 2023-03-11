package tn.esprit.springfever.Services.Implementation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import tn.esprit.springfever.Security.jwt.JwtUtils;
 import tn.esprit.springfever.Services.Interfaces.IServiceUser;

import tn.esprit.springfever.entities.UserEvaluation;
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
    public List<UserEvaluation> getAllUsers() {
        return userRepository.findAll();

    }

    public String getusernamefromtoken(String header) {
        return   jwtUtils.getUserNameFromJwtToken(header) ;


    }





}


package tn.esprit.springfever.Services.Implementation;


import com.auth0.jwt.JWT;
 import io.jsonwebtoken.*;
 import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 import tn.esprit.springfever.Security.jwt.JwtUtils;
 import tn.esprit.springfever.Services.Interfaces.IServiceUser;

import com.auth0.jwt.algorithms.Algorithm;
 import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
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
        return   jwtUtils.getUserNameFromJwtToken(header) ;


    }





}


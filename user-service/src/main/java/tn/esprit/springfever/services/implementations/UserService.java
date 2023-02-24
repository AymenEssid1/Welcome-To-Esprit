package tn.esprit.springfever.services.implementations;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.UserRepository;
import tn.esprit.springfever.security.jwt.JwtUtils;
import tn.esprit.springfever.services.interfaces.IUserService;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.List;


@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private RabbitTemplate amqpTemplate;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();

    }

    public String getusernamefromtoken(String header) {
        return   jwtUtils.getUserNameFromJwtToken(header) ;


    }

    @Override
    public User getuserfromtoken(String header) {
        return userRepository.findByUsername(getusernamefromtoken(header)).orElse(null);
    }

    public User getUserFromUserName(String username){
        return userRepository.findByUsername(username).orElse(null);
    }




}


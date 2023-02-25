package tn.esprit.springfever.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tn.esprit.springfever.entities.UserIntrests;
import tn.esprit.springfever.repositories.InterestRepository;

import java.util.List;

@Component
public class UserInterestService {
    @Autowired
    InterestRepository interestRepository;

    public void save (List<UserIntrests> userIntrests){
        float weight = 5;
        for (UserIntrests u:userIntrests){
            u.setWeight(weight);
            interestRepository.save(u);
            weight = weight-(weight/userIntrests.size());
        }

    }
}

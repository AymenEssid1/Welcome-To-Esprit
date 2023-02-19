package tn.esprit.springfever.Services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.Services.Interface.IServiceUser;

public class UserIMP implements IServiceUser {

    @Autowired
    UserRepo userrepo;
}

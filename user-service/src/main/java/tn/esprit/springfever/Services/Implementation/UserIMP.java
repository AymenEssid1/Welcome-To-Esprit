package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Repositories.BadgeRepo;
import tn.esprit.springfever.Repositories.ImageRepository;
import tn.esprit.springfever.Repositories.RoleRepo;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.entities.*;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class UserIMP implements IServiceUser {

    @Autowired
    UserRepo userrepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    BadgeRepo badgeRepo;

    @Override
    public User addUserAndAssignRole(User user, RoleType rolename) {
        Role role = roleRepo.findByRolename(rolename);


        user.getRoles().add(role);
        return userrepo.save(user);
    }

    @Override
    public Badge addBadge(Badge badge, long userid) {
        User user =userrepo.findById(userid).orElse(null);
        badge.setUser(user);
        return badgeRepo.save(badge);
    }



    @Override
    public User addUser(User user) {
        return userrepo.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        User p = userrepo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            user.setUserid(p.getUserid());;
            userrepo.save(user);
        }
        return p;
    }

    @Override
    public String deleteUser(Long user) {
        User p = userrepo.findById(Long.valueOf(user)).orElse(null) ;
        if(p!=null) {
            userrepo.delete(p);
            return "User was successfully deleted !" ;
        }
        return "Not Found ! ";

    }

    @Override
    public List<User> getAllUsers() {
        return userrepo.findAll();
    }

    @Override
    public User getSingleUser(Long id) {
        return userrepo.findById(id).orElse(null);
    }





}

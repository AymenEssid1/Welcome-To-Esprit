package tn.esprit.springfever.Services.Interface;


import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import tn.esprit.springfever.entities.Badge;
import tn.esprit.springfever.entities.Image;
import tn.esprit.springfever.entities.RoleType;
import tn.esprit.springfever.entities.User;

import java.util.List;


public interface IServiceUser {


    public User addUserAndAssignRole(User user, RoleType rolename);

    public Badge addBadge(Badge badge, long userid);

    public User addUser(User user);
    public User updateUser(Long id,User user);
    public String deleteUser(Long user);
    public List<User> getAllUsers();
    public User getSingleUser(Long id);
    public String generateQr(User user);




}

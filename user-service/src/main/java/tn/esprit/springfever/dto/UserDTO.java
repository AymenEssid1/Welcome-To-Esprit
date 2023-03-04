package tn.esprit.springfever.dto;


import lombok.Data;
import tn.esprit.springfever.entities.Badge;
import tn.esprit.springfever.entities.Image;
import tn.esprit.springfever.entities.Role;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {

    private Long userid;
    private String username;
    private String firstname;
    private String lastname;
    private int cin;
    private Date dob;
    private String password;
    private String email;
    private String phoneNumber;

    private Set<Role> roles = new HashSet<>();
    private Image image;
    private Badge badge;

}



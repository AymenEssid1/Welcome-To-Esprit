package tn.esprit.springfever.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.springfever.entities.Badge;
import tn.esprit.springfever.entities.Image;
import tn.esprit.springfever.entities.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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

    private Set<Role> roles = new HashSet<>();
    private Image image;
    private Badge badge;
}



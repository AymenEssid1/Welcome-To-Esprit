package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@ToString
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long userid;

    @NotBlank
    @Size(max = 20)
    private String username;


    @NotBlank
    @Size(max = 20)
    private String firstname;

    @NotBlank
    @Size(max = 20)
    private String lastname;


    @NotBlank
    private int cin;

    @NotBlank
    private Date dob;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Badge badge;




















    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String firstname, String lastname, int cin, Date dob, String password) {

        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.cin = cin;
        this.dob = dob;
        this.password = password;
    }
}
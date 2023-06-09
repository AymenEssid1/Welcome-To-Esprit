package tn.esprit.springfever.entities;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
 import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Data
@Table(name = "fake_user")
public class UserEvaluation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;
     @Size(max = 20)
    private String firstname;

     @Size(max = 20)
    private String lastname;

     @Size(max = 20)
    private String username;

     @Size(max = 50)
    @Email
    private String email;

     @Size(max = 120)
    private String password;

     private Date dob;




    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles_evaluation",
            joinColumns = @JoinColumn(name = "user_idd"),
            inverseJoinColumns = @JoinColumn(name = "role_idd"))
    private Set<RoleEvaluation> roles = new HashSet<>();



    @OneToOne
    Deliberation deliberation ;


    public UserEvaluation() {

    }
    public UserEvaluation(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
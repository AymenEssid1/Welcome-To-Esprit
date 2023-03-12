package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.cache.annotation.CachePut;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@ToString
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long userid;
    @Size(max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 20)
    private String firstname;
    @Size(max = 20)
    private String lastname;

    private int cin;

    private String etatUser;


    @NotBlank
    @Size(max = 8)
    @Size(min = 8)
   @NumberFormat
    private String phoneNumber;

    @Temporal(value=TemporalType.DATE)
    @DateTimeFormat(pattern = "yyy-MM-dd")

    private Date dob;
    @Size(max = 120)
    private String password;



    @JsonIgnore

    private int failedLoginAttempts;


    @JsonIgnore
    private int payment_status;

    @JsonIgnore
    private LocalDateTime creationDate;



    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_achievement",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private Set<Achievement> achis = new HashSet<>();


    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;



    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private Badge badge;


    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    private Ban ban;







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
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", cin=" + cin +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dob=" + dob +
                ", password='" + password + '\'' +
                ", creationDate=" + creationDate +

                '}';
    }
}

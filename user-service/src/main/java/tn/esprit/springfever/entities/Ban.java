package tn.esprit.springfever.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Data
public class Ban implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lastFailedLoginAttempt;

    private LocalDateTime expiryTime;


    @OneToOne
    private User user;


}

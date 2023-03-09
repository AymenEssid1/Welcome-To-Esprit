package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;


@Entity
@ToString
@Data
public class Badge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long badgeid;


    @Column(unique = true, nullable = false)
    private String qrCode;


    @JsonIgnore
    @OneToOne
    private User user;



    public Badge() {
        // Generate the QR code using a random UUID
        this.qrCode = UUID.randomUUID().toString();
    }



}

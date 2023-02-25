package tn.esprit.springfever.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import  tn.esprit.springfever.enums.ClaimSubject;
import  tn.esprit.springfever.enums.ClaimRate;
import  tn.esprit.springfever.enums.ClaimStatus;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@ToString
@Data

public class Claim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    @NotNull
    @Positive
    private Long idClaim;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private ClaimSubject claimSubject ;

    @NotBlank
    @NotNull
    @Size(min = 1, max = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus ;

    @NotNull
    @Size(max = 500)
    private String decision ;

    @Enumerated(EnumType.STRING)
    private ClaimRate claimRate ;

    private Date dateSendingClaim ;

    private Date dateTreatingClaim ;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user ;

    public Claim(Long ClaimId, String test_claim) {
        this.idClaim=ClaimId ;
        this.description= test_claim ;

    }

    public Claim() {

    }
}
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
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@ToString
@Data

public class Claim implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idClaim;
    @Enumerated(EnumType.STRING)
    @NotBlank
    private ClaimSubject claimSubject ;
    @NotBlank
    @Size(max = 500)
    private String desciption;
    @Enumerated(EnumType.STRING)
    private ClaimStatus claimStatus ;
    private String decision ;
    @Enumerated(EnumType.STRING)
    private ClaimRate claimRate ;
    @ManyToOne
    @JsonIgnore
    private User user ;

    public Claim(Long entityId, String test_claim) {
    }
}
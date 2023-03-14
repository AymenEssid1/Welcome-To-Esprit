package tn.esprit.springfever.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.Entretien_Res;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
@Entity
@ToString
@Data
public class Entretien implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long ID_Job_Entretien ;
    @NotNull(message = "La note ne peut pas être nulle.")
    @Min(value = 0, message = "La note doit être supérieure ou égale à 0.")
    @Max(value = 20, message = "La note doit être inférieure ou égale à 20.")
    private Long Note;
    @NotBlank(message = "L'appréciation ne peut pas être vide.")
    @Size(max = 255, message = "L'appréciation ne peut pas dépasser {max} caractères.")
    private String Appreciation ;
    @NotNull(message = "Le résultat ne peut pas être nul.")
    @Enumerated(EnumType.STRING)
    private Entretien_Res Resultat ;


    @OneToOne(mappedBy = "entretien", cascade = CascadeType.ALL, optional = true)
    @JsonIgnore
    private Job_RDV rdv;
}

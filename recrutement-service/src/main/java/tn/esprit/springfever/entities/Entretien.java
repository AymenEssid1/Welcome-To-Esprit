package tn.esprit.springfever.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tn.esprit.springfever.enums.Entretien_Res;

import javax.persistence.*;
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
    private Long Note;
    private String Appreciation ;
    @Enumerated(EnumType.STRING)
    private Entretien_Res Resultat ;
    @ManyToOne
    @JoinColumn(name = "Candidate_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "Jury_ID")
    private User user2;

    @OneToOne(mappedBy = "entretien", cascade = CascadeType.ALL, optional = true)
    private Job_RDV rdv;
}

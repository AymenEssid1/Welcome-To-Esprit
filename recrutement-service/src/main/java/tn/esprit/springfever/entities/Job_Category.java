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
import java.util.List;

@Entity
@ToString
@Data
public class Job_Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long Id_Job_Category;

    @NotBlank(message = "Le nom de catégorie ne peut pas être vide.")
    @Size(max = 50, message = "Le nom de catégorie ne peut pas dépasser {max} caractères.")
    private String Name_Category;

    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Job_Offer> jobOffers;
}

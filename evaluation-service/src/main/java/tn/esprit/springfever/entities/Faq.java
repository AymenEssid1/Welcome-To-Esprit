package tn.esprit.springfever.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import  tn.esprit.springfever.enums.ClaimRate;
import  tn.esprit.springfever.enums.ClaimStatus;
import  tn.esprit.springfever.enums.ClaimSubject;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Data

public class Faq implements Serializable {
    public Faq(Long idFaq, String question, String response) {
        this.idFaq = idFaq;
        this.question = question;
        this.response = response;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter






    private Long idFaq;
    @NotBlank(message = "Le question ne doit pas être vide")
    @Size(min = 3,  max = 5000 ,message = "Le question doit contenir au moins 3 caractères")
    private String question ;
    @NotBlank(message = "La response ne doit pas être vide")
    @Size(min = 3, max = 5000,message = "La response doit contenir au moins 3 caractères")
    private String response ;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FaqCategory> faqCategories = new ArrayList<>();


    public Faq() {

    }
}
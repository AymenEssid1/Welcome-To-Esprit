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
import java.util.Date;

@Entity
@ToString
@Data

public class Event implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter

    private Long idEvent;
    @NotBlank
    private String budget ;
    @NotBlank
    private String espace;
    @NotBlank
    private String materiels ;
    @Enumerated(EnumType.STRING)
    private CategorieEvent typeEvent ;
    @NotBlank
    private Date startDate ;
    @NotBlank
    private Date endDate ;

    @ManyToOne
    @JsonIgnore
    Teams teams;




}

package tn.esprit.springfever.DTO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.springfever.entities.CategorieEvent;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
public class EventDTO implements Serializable{
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

    public EventDTO(Long idEvent, String budget, String espace, String materiels,CategorieEvent typeEvent,Date startDate,Date endDate) {
        this.idEvent = idEvent;
        this.budget = budget;
        this.espace = espace;
        this.materiels = materiels;
        this.typeEvent = typeEvent;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}

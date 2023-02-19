package tn.esprit.springfever.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class RDVDTO {

    private Long idRDV;

    @Size(max = 255)
    private String salle;

    private LocalDate date;

    @JsonProperty("rDVuser")
    private Long rDVuser;

}

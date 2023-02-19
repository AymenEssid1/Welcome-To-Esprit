package tn.esprit.springfever.model;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OptionDTO {

    private Long idOption;

    @Size(max = 255)
    private String nomOption;

}

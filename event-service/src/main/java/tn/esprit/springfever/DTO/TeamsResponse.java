package tn.esprit.springfever.DTO;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString

public class TeamsResponse {

    private Long idTeam;

    @NotBlank
    @Size(max = 30)
    private String NameTeam ;
    @NotBlank
    private String QRcertificat;
    @NotBlank
    private String NiveauEtude ;

    List<UserDTO> users;

    public TeamsResponse(Long idTeam, String NameTeam, String QRcertificat, String NiveauEtude, List<UserDTO> users) {
        this.idTeam = idTeam;
        this.NameTeam = NameTeam;
        this.QRcertificat = QRcertificat;
        this.NiveauEtude = NiveauEtude;
        this.users =users;

    }

}


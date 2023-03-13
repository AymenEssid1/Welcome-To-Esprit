package tn.esprit.springfever.Services.Interfaces;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.WriterException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.DTO.TeamsResponse;
import tn.esprit.springfever.entities.Teams;

import javax.mail.MessagingException;
import java.awt.print.Pageable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IServiceTeams {
    public Teams addTeams(Teams teams) throws IOException;
    public  List<TeamsResponse> getAllTeams() throws JsonProcessingException;
    public  boolean deleteTeams(Long idTeam) ;
    public Teams updateTeams (Long idTeam , TeamsDTO teamsDTO) ;
    public void assignUserToTeams() throws JsonProcessingException;

    public String AssignProjectToTeams(Long idTeam , Long idProject );
    public String AssignImageToTeams(Long idTeam , Long id );

    public List<Teams> getTeamsByProjectId(Long idProject);

    Teams getTeamsWithMaxProjectNote();

  //  public String generateQr(Teams teams);


    public Optional<Teams> getTeamById(Long id);
    public Teams saveTeam(Teams team);
    public byte[] generateQRCode(String teamName, String event) throws WriterException, IOException;


    public void sendOnlineEventInvitation() throws MessagingException, JsonProcessingException;

    private static com.google.zxing.EncodeHintType getQRCodeHints() {
        return null;
    }

}

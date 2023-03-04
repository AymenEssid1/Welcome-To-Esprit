package tn.esprit.springfever.Services.Interfaces;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;

import java.awt.print.Pageable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IServiceTeams {
    public Teams addTeams(Teams teams) throws IOException;
    public  List<Teams> getAllTeams() ;
    public  boolean deleteTeams(Long idTeam) ;
    public Teams updateTeams (Long idTeam , TeamsDTO teamsDTO) ;
    public void assignUserToTeams();

    public String AssignProjectToTeams(Long idTeam , Long idProject );
    public String AssignImageToTeams(Long idTeam , Long id );

    public List<Teams> getTeamsByProjectId(Long idProject);

    Teams getTeamsWithMaxProjectNote();


}

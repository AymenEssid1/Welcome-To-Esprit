package tn.esprit.springfever.Services.Interfaces;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface IServiceTeams {
    public Teams addTeams(Teams teams) throws IOException;
    public  List<Teams> getAllTeams() ;
    public  boolean deleteTeams(Long idTeam) ;
    public Teams updateTeams (Long idTeam , TeamsDTO teamsDTO) ;
    public void assignUserToTeams();

}

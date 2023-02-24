package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.TeamsDTO;
import tn.esprit.springfever.Services.Interfaces.TeamsMapper;
import tn.esprit.springfever.Services.Interfaces.IServiceTeams;
import tn.esprit.springfever.entities.Event;
import tn.esprit.springfever.entities.Teams;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.TeamsRepository;
import tn.esprit.springfever.repositories.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j

public class ServiceTeamsImpl implements IServiceTeams{
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeamsRepository teamsRepository ;
    @Autowired
    TeamsMapper teamsMapper;

    @Override
    public Teams addTeams(Teams teams) {
        log.info("teams was successfully added !");
        return teamsRepository.save(teams);
    }

    /*
    @Override
    public Teams addTeams(Teams teams) throws IOException {

        // Load the tokenizer model
        InputStream modelIn = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);

        // Create a new tokenizer
        Tokenizer tokenizer = new TokenizerME(model);

        // Analyze the keywords from the student's diploma
        String diploma = "Diploma in Computer Science with a specialization in Web Development";
        String[] tokens = tokenizer.tokenize(diploma);

        // Display the separated keywords
        for (String token : tokens)
            log.info(token);
        //log takone
        log.info("teams was successfully added !");
        return teamsRepository.save(teams);
    }
*/
    @Override
    public List<Teams> getAllTeams() {return teamsRepository.findAll();}


    @Override
    public boolean deleteTeams(Long idTeam) {
        Teams existingTeams = teamsRepository.findById(idTeam).orElse(null);
        if(existingTeams!=null) {
            teamsRepository.delete(existingTeams);
            log.info("teams deleted");
            return true ;
        }
        log.info(" this team is not existing");
        return false;
    }


    @Override
    public Teams updateTeams(Long idTeam, TeamsDTO teamsDto) {
        Teams teams = teamsRepository.findById(idTeam).orElse(null);
        if (teams != null) {
            teamsMapper.updateTeamsFromDto(teamsDto, teams);
            teamsRepository.save(teams);
            log.info("teams was successfully updated !");
        }
        log.info("teams not found !");
        return  teams;

    }
/*
    @Override
    public void assignUserToTeams(Long id, Long idTeam) {
        Teams  T1 = teamsRepository.findByIdTeam(idTeam);
        User U1 = userRepository.findById(id).orElse(null);
        System.out.println("teams : "+T1);
        System.out.println("User : "+U1);
        System.out.println("teams: "+T1);
        U1.setTeams(T1);
        userRepository.save(U1);
    }
 */

    @Override
    public void assignUserToTeams() {
        List<User> users = userRepository.findAll();

        int numUsers = users.size();
        int numTeams = (int) Math.ceil((double) numUsers / 5);

        for (int i = 0; i < numTeams; i++) {
            Teams team = new Teams();
            team.setNameTeam("Team " + (i+1)) ;
            team.setQRcertificat("Certificate " + (i+1)) ;
            team.setNiveauEtude("niveau d'étude " + (i+1));
            teamsRepository.save(team);

            for (int j = i*5; j < Math.min((i+1)*5, numUsers); j++) {
                User user = users.get(j);
                user.setTeams(team);
                userRepository.save(user);
            }
        }
    }




}

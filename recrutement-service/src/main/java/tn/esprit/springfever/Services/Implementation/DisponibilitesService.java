package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IDisponibilites;
import tn.esprit.springfever.entities.Disponibilites;
import tn.esprit.springfever.entities.Job_RDV;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.repositories.DisponiblitiesRepository;
import tn.esprit.springfever.repositories.JobRdvRepository;
import tn.esprit.springfever.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DisponibilitesService implements IDisponibilites {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DisponiblitiesRepository disponiblitiesRepository;
    @Autowired
    JobRdvRepository jobRdvRepository;
    /*public List<LocalDateTime> getDisponibilites(Long userId) {
        // Récupération de l'utilisateur
        User user = userRepository.findById(userId).orElse(null);
        Long id = user.getId();

        // Récupération des disponibilités
        List<Disponibilites> disponibilites = disponiblitiesRepository.findByUserId(id);

        // Conversion en liste de paires de dates
        List<LocalDateTime> dateRanges = new ArrayList<>();
        for (Disponibilites dispo : disponibilites) {
            dateRanges.add(dispo.getEnd_date());
            dateRanges.add(dispo.getEnd_date());
        }

        return dateRanges;
    }*/

    public Disponibilites AddDispo(Disponibilites disponibilites) {
        return disponiblitiesRepository.save(disponibilites);
    }

    /*public String AssignCandidateToDisponibilities(Long idDispo, Long idJobRDV) {
        Job_RDV jobRdv=jobRdvRepository.findById(idJobRDV).orElse(null);
        Disponibilites disponibilites = disponiblitiesRepository.findById(idDispo).orElse(null);
        if(jobRdv != null && disponibilites != null) {
            disponibilites.set
            disponiblitiesRepository.save(disponibilites);
            return "OK ";
        }
        return "Not OK";
    }*/
    public String AssignJobRdvTODisponibilities(Long idDispoCandidate,Long idDispoJury,Long idJobRdv){
        Disponibilites dispoCandidate=disponiblitiesRepository.findById(idDispoCandidate).orElse(null);
        Disponibilites dispoJury=disponiblitiesRepository.findById(idDispoJury).orElse(null);
        Job_RDV jobRdv=jobRdvRepository.findById(idJobRdv).orElse(null);
        if(dispoCandidate!=null &&dispoJury!=null &&jobRdv !=null){
            dispoCandidate.setJobRDV(jobRdv);
            dispoJury.setJobRDV(jobRdv);
            disponiblitiesRepository.save(dispoCandidate);
            disponiblitiesRepository.save(dispoJury);
            jobRdv.setDisponibilitesCandidat(dispoCandidate);
            jobRdv.setDisponibilitesJury(dispoJury);
            jobRdvRepository.save(jobRdv);
            return "Ok !";

        }
        return "Not Found !!";

    }


}

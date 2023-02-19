package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IEntretien;
import tn.esprit.springfever.entities.Entretien;
import tn.esprit.springfever.repositories.EntretienRepository;

import java.util.List;

@Service
@Slf4j
public class EntretienService implements IEntretien {
    @Autowired
    EntretienRepository entretienRepository;

    public Entretien AddEntretien(Entretien entretien){
        return entretienRepository.save(entretien);

    }
    public List<Entretien> GetAllEntretiens(){
        return entretienRepository.findAll();
    }
    public Entretien UpdateEntretien(Long ID_Job_Entretien , Entretien entretien){

        Entretien entretien1=entretienRepository.findById(ID_Job_Entretien).orElse(null);
        if(entretien1!=null){
            entretien1.setAppreciation(entretien.getAppreciation());
            entretien1.setNote(entretien.getNote());
            entretien1.setRdv(entretien.getRdv());
            entretien1.setUser(entretien.getUser());
            entretien1.setUser2(entretien.getUser2());
            entretienRepository.save(entretien1);
            log.info("Entity Entretien is Updated !");
            return entretien1;
        }
        log.info("Entrtien Not Found !");
        return entretien1;

    }
    public String DeleteEntretien(Long ID_Job_Entretien){
        Entretien entretien1=entretienRepository.findById(ID_Job_Entretien).orElse(null);
        if(entretien1!=null){
            entretienRepository.delete(entretien1);
            return "Entretien is deleted with Sucess !";
        }
        return "Entretien Not Found !";
    }


}

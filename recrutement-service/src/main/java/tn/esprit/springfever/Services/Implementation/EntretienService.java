package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IEntretien;
import tn.esprit.springfever.entities.Entretien;
import tn.esprit.springfever.entities.Job_Application;
import tn.esprit.springfever.entities.Job_RDV;
import tn.esprit.springfever.repositories.EntretienRepository;
import tn.esprit.springfever.repositories.JobRdvRepository;

import java.util.List;

@Service
@Slf4j
public class EntretienService implements IEntretien {
    @Autowired
    EntretienRepository entretienRepository;
    @Autowired
    JobRdvRepository jobRdvRepository;
    @Autowired
    private JavaMailSender mailSender;

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
    public void sendEmailToDistrubInterviewRes(Long id, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chaima.dammak@espri.tn");
        Entretien entretien=entretienRepository.findById(id).orElse(null);
        String to=entretien.getUser().getEmail();
        System.out.println(to);
        message.setTo(to);
        message.setSubject(subject);
        //body="Hello !! ";
        message.setText(body);
        mailSender.send(message);

    }

    public String AssignRDVToEntretien(Long ID_Job_Entretien , Long ID_Job_DRV ){
        Entretien entretien=entretienRepository.findById(ID_Job_Entretien).orElse(null);
        Job_RDV jobRdv=jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(entretien!=null &&jobRdv!=null){
            entretien.setRdv(jobRdv);
            entretien.setUser(jobRdv.getCandidate());
            entretien.setUser2(jobRdv.getJury());
            entretienRepository.save(entretien);
            log.info("Job RDV , Jury ANd Canddate are affected Successfully !");
            return "Job RDV , Jury ANd Canddate are affected Successfully !";
        }
        log.info("Job RDV Or Entretien are not Found ");
        return "Job RDV Or Entretien are not Found ";
    }


}

package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.Services.Interfaces.IJobRDV;
import tn.esprit.springfever.Services.Interfaces.JobMapper;
import tn.esprit.springfever.entities.Entretien;
import tn.esprit.springfever.entities.Job_Application;
import tn.esprit.springfever.entities.Job_RDV;
import tn.esprit.springfever.repositories.EntretienRepository;
import tn.esprit.springfever.repositories.JobApplicationRepository;
import tn.esprit.springfever.repositories.JobRdvRepository;

import java.util.List;
@Service
@Slf4j
public class JobRdvService implements IJobRDV {
    @Autowired
    JobRdvRepository jobRdvRepository;
    @Autowired
    EntretienRepository entretienRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    public Job_RDV addJobRDV(Job_RDV job_rdv){
        return jobRdvRepository.save(job_rdv);

    }
    public List<Job_RDV> getAllJobRDVs(){
        return jobRdvRepository.findAll();

    }
    /*public Job_RDV updateJobRDV (Long ID_Job_DRV , Job_RDV job_rdv ) {
        Job_RDV jobRdvExisted = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(jobRdvExisted!=null){
            jobRdvExisted.setUser2(job_rdv.getUser2());
            jobRdvExisted.setUser(job_rdv.getUser());
            jobRdvExisted.setEntretien(job_rdv.getEntretien());
            jobRdvExisted.setType_RDV(job_rdv.getType_RDV());
            jobRdvExisted.setSalle_Rdv(job_rdv.getSalle_Rdv());
            jobRdvExisted.setJobApplication(job_rdv.getJobApplication());
            jobRdvRepository.save(jobRdvExisted);
            log.info("Job Offer is updated !");
            return jobRdvExisted ;

        }
        log.info("Job RDV does not exist !! ");
        return jobRdvExisted;
    }*/

    /*public Job_RDV updateJobRDV(Long ID_Job_DRV, Job_RDV_DTO jobRdvDto) {
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if (jobRdv != null) {
            jobMapper.updateClaimFromDto(jobRdv, jobRdvDto);
            log.info(jobRdv.getSalle_Rdv());
            jobRdvRepository.save(jobRdv);
            log.info("Job was successfully updated !");
            return jobRdv;
        }
        log.info("Job not found !");
        return  jobRdv;
    }*/

    public  String deleteJobOffer(Long  ID_Job_DRV) {
        Job_RDV jobRdv=jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(jobRdv!=null){
            jobRdvRepository.delete(jobRdv);
            log.info("Job RDV Is Deleted With Success ! ");
            return "Job RDV Is Deleted With Success ! ";
        }
        log.info("Job RDV Does not Exist !!");
        return "Job RDV Does not Exist !!";

    }

    public String AssignEntretienToRDV( Long ID_Job_Entretien, Long ID_Job_DRV){
        Entretien entretien =entretienRepository.findById(ID_Job_Entretien).orElse(null);
        Job_RDV jobRdv = jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(entretien!=null && jobRdv!=null){
            jobRdv.setEntretien(entretien);
            jobRdvRepository.save(jobRdv);
            return "Entrtien is Affeced To RDV";
        }
        return "Entretien Or Job RDV Are not found";
    }

    public String AssignJobApplicationToRDV(Long Id_Job_Application ,Long ID_Job_DRV){
        Job_Application job_application=jobApplicationRepository.findById(Id_Job_Application).orElse(null);
        Job_RDV jobRdv=jobRdvRepository.findById(ID_Job_DRV).orElse(null);
        if(job_application!=null && jobRdv!=null){
            jobRdv.setJobApplication(job_application);
            jobRdvRepository.save(jobRdv);
            return "Added";
        }
        return "Job RDV OR Job Application are not found ! ";
    }
}

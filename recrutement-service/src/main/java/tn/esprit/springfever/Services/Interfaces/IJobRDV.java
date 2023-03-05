package tn.esprit.springfever.Services.Interfaces;

import com.netflix.discovery.shared.Pair;
import tn.esprit.springfever.DTO.Job_RDV_DTO;
import tn.esprit.springfever.entities.Disponibilites;
import tn.esprit.springfever.entities.Job_Offer;
import tn.esprit.springfever.entities.Job_RDV;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface IJobRDV {

    public Job_RDV addJobRDV(Job_RDV job_rdv);
    public List<Job_RDV> getAllJobRDVs() ;
    //public Job_RDV updateJobRDV (Long ID_Job_DRV , Job_RDV job_rdv ) ;
    public  String deleteJobOffer(Long  ID_Job_DRV) ;
    public String AssignEntretienToRDV( Long ID_Job_Entretien, Long ID_Job_DRV);

    public String AssignJobApplicationToRDV(Long Id_Job_Application ,Long ID_Job_DRV);
    public Job_RDV updateJobRDV(Long ID_Job_DRV, Job_RDV_DTO jobRdvDto);
    public String AssignCandidateToJobRDV(Long ID_Job_DRV , Long Id_Job_Application );
    public String AssignJuryToJobRDV(Long ID_Job_DRV , Long id );
    public List<LocalDateTime> getDisponibilites(Long userId);
    public Disponibilites AddDispo(Disponibilites disponibilites);
    public String AssignUserToDisponibilities(Long idDispo, Long idUser );
    public LocalDateTime findFirstAvailableDateTime(Long dispoCandidate, Long dispoJury,
                                                    int interviewDuration);
    //public String AssignRDVdATETordv(Long dispoCandidate, Long dispoJury,int interviewDuration );
    public String generateJitsiMeetLink(Long id);
    public void updateCandidateLocation(Long idRDV, String address);
    public double calculateDistance(Long idRDV);
    public void FixationRDV(Long id);

}

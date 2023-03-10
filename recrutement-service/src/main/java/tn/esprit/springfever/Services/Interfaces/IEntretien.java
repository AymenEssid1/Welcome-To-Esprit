package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Entretien;

import java.util.List;

public interface IEntretien {

    public Entretien AddEntretien(Entretien entretien);
    public List<Entretien> GetAllEntretiens();
    public Entretien UpdateEntretien(Long ID_Job_Entretien , Entretien entretien);
    public String DeleteEntretien(Long ID_Job_Entretien);
    public void sendEmailToDistrubInterviewRes(Long id, String subject, String body);
    public String AssignRDVToEntretien(Long ID_Job_Entretien , Long ID_Job_DRV );
}

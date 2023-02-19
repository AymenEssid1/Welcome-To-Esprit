package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Entretien;

import java.util.List;

public interface IEntretien {

    public Entretien AddEntretien(Entretien entretien);
    public List<Entretien> GetAllEntretiens();
    public Entretien UpdateEntretien(Long ID_Job_Entretien , Entretien entretien);
    public String DeleteEntretien(Long ID_Job_Entretien);
}

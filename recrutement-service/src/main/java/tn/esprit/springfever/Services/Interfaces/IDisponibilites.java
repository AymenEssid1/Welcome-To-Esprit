package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Disponibilites;

import java.time.LocalDateTime;
import java.util.List;

public interface IDisponibilites {
    //public List<LocalDateTime> getDisponibilites(Long userId);
    public Disponibilites AddDispo(Disponibilites disponibilites);
    //public String AssignUserToDisponibilities(Long idDispo, Long idUser );
    public String AssignJobRdvTODisponibilities(Long idDispoCandidate,Long idDispoJury,Long idJobRdv);
}

package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IDisponibilites;
import tn.esprit.springfever.entities.Disponibilites;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DisponibilitesController {
    @Autowired
    IDisponibilites iDisponibilites;

    @PostMapping("addDisponibilities/")
    public Disponibilites AddDispo(@RequestBody Disponibilites disponibilites){
        return iDisponibilites.AddDispo(disponibilites);


    }
    /*@PutMapping("AssignUserToDisponibilities/{idDispo}/{idUser}")
    public String AssignUserToDisponibilities(@PathVariable("idDispo") Long idDispo, @PathVariable("idUser") Long idUser ){
        return iDisponibilites.AssignUserToDisponibilities(idDispo,idUser);

    }*/
    /*GetMapping("getDisponibilitiesByUser/{idUser}")
    public List<LocalDateTime> getDisponibilites(@PathVariable("idUser") Long userId){
        return iDisponibilites.getDisponibilites(userId);

    }*/
    @PutMapping("AssignJobRdvTODisponibilities/")
    public String AssignJobRdvTODisponibilities(Long idDispoCandidate,Long idDispoJury,Long idJobRdv){
        return iDisponibilites.AssignJobRdvTODisponibilities(idDispoCandidate,idDispoJury,idJobRdv);

    }
}

package tn.esprit.springfever.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IEntretien;
import tn.esprit.springfever.entities.Entretien;

import java.util.List;

@RestController
public class EntretienController {
    @Autowired
    IEntretien iEntretien;




    @PostMapping("addEntretien/")
    public Entretien AddEntretien(@Validated @RequestBody Entretien entretien){
        return iEntretien.AddEntretien(entretien);


    }



    @GetMapping("getAllEntretiens/")
    public List<Entretien> GetAllEntretiens(){
        return iEntretien.GetAllEntretiens();

    }

    @PutMapping("updateEntretien/{id}")
    public Entretien UpdateEntretien(@PathVariable("id") Long ID_Job_Entretien ,@RequestBody Entretien entretien){
        return iEntretien.UpdateEntretien(ID_Job_Entretien,entretien);

    }


    @DeleteMapping("deleteEntretien/{id}")
    public String DeleteEntretien(@PathVariable("id") Long ID_Job_Entretien){
        return iEntretien.DeleteEntretien(ID_Job_Entretien);

    }
}

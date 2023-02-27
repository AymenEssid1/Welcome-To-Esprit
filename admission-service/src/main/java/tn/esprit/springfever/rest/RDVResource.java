package tn.esprit.springfever.rest;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.model.RDVDTO;
import tn.esprit.springfever.model.SalleDTO;
import tn.esprit.springfever.service.RDVService;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.service.SalleService;

import java.util.List;
import java.util.Random;


@RestController
@RequestMapping(value = "/api/rDVs", produces = MediaType.APPLICATION_JSON_VALUE)
public class RDVResource {


    @Autowired
    private SalleService salleService;
    @Autowired
    private  RDVService rDVService;

    public RDVResource( RDVService rDVService) {
        this.rDVService = rDVService;
    }

    @GetMapping
    public ResponseEntity<List<RDVDTO>> getAllRDVs() {
        return ResponseEntity.ok(rDVService.findAll());
    }

    @GetMapping("/{idRDV}")
    public ResponseEntity<RDVDTO> getRDV(@PathVariable  Long idRDV) {
        return ResponseEntity.ok(rDVService.get(idRDV));
    }



    @PutMapping("/{idRDV}")
    public ResponseEntity<Void> updateRDV(@PathVariable  Long idRDV,
            @RequestBody @Valid  RDVDTO rDVDTO) {
        rDVService.update(idRDV, rDVDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idRDV}")
    public ResponseEntity<Void> deleteRDV(@PathVariable  Long idRDV) {
        rDVService.delete(idRDV);
        return ResponseEntity.noContent().build();
    }

}

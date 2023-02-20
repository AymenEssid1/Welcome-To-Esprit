package tn.esprit.springfever.rest;

import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.model.DemandeAdmissionDTO;
import tn.esprit.springfever.service.DemandeAdmissionService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/demandeAdmissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class DemandeAdmissionResource {

    @Autowired
    private  DemandeAdmissionService demandeAdmissionService;

    public DemandeAdmissionResource( DemandeAdmissionService demandeAdmissionService) {
        this.demandeAdmissionService = demandeAdmissionService;
    }

    @GetMapping
    public ResponseEntity<List<DemandeAdmissionDTO>> getAllDemandeAdmissions() {
        return ResponseEntity.ok(demandeAdmissionService.findAll());
    }

    @GetMapping("/{idAdmission}")
    public ResponseEntity<DemandeAdmissionDTO> getDemandeAdmission(
            @PathVariable  Long idAdmission) {
        return ResponseEntity.ok(demandeAdmissionService.get(idAdmission));
    }

    @PostMapping("/{iduser}")
    public ResponseEntity<Long> createDemandeAdmission(
            @RequestBody @Valid  DemandeAdmissionDTO demandeAdmissionDTO,@PathVariable  Long iduser) {
        return new ResponseEntity<>(demandeAdmissionService.create(demandeAdmissionDTO,iduser), HttpStatus.CREATED);
    }

    @PutMapping("/{idAdmission}")
    public ResponseEntity<Void> updateDemandeAdmission(@PathVariable  Long idAdmission,
            @RequestBody @Valid  DemandeAdmissionDTO demandeAdmissionDTO) {
        demandeAdmissionService.update(idAdmission, demandeAdmissionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idAdmission}")
    public ResponseEntity<Void> deleteDemandeAdmission(@PathVariable  Long idAdmission) {
        demandeAdmissionService.delete(idAdmission);
        return ResponseEntity.noContent().build();
    }

}

package tn.esprit.springfever.rest;

import tn.esprit.springfever.model.SpecialiteDTO;
import tn.esprit.springfever.service.SpecialiteService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/specialites", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpecialiteResource {

    private  SpecialiteService specialiteService;

    public SpecialiteResource( SpecialiteService specialiteService) {
        this.specialiteService = specialiteService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialiteDTO>> getAllSpecialites() {
        return ResponseEntity.ok(specialiteService.findAll());
    }

    @GetMapping("/{idSpecialite}")
    public ResponseEntity<SpecialiteDTO> getSpecialite(@PathVariable  Long idSpecialite) {
        return ResponseEntity.ok(specialiteService.get(idSpecialite));
    }

    @PostMapping
    public ResponseEntity<Long> createSpecialite(
            @RequestBody @Valid  SpecialiteDTO specialiteDTO) {
        return new ResponseEntity<>(specialiteService.create(specialiteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{idSpecialite}")
    public ResponseEntity<Void> updateSpecialite(@PathVariable  Long idSpecialite,
            @RequestBody @Valid  SpecialiteDTO specialiteDTO) {
        specialiteService.update(idSpecialite, specialiteDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idSpecialite}")
    public ResponseEntity<Void> deleteSpecialite(@PathVariable  Long idSpecialite) {
        specialiteService.delete(idSpecialite);
        return ResponseEntity.noContent().build();
    }

}

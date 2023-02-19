package tn.esprit.springfever.rest;

import tn.esprit.springfever.model.RDVDTO;
import tn.esprit.springfever.service.RDVService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/api/rDVs", produces = MediaType.APPLICATION_JSON_VALUE)
public class RDVResource {

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

    @PostMapping
    public ResponseEntity<Long> createRDV(@RequestBody @Valid  RDVDTO rDVDTO) {
        return new ResponseEntity<>(rDVService.create(rDVDTO), HttpStatus.CREATED);
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

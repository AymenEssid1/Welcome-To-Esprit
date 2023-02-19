package tn.esprit.springfever.rest;

import tn.esprit.springfever.model.OptionDTO;
import tn.esprit.springfever.service.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/options", produces = MediaType.APPLICATION_JSON_VALUE)
public class OptionResource {

    private  OptionService optionService;

    public OptionResource( OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<List<OptionDTO>> getAllOptions() {
        return ResponseEntity.ok(optionService.findAll());
    }

    @GetMapping("/{idOption}")
    public ResponseEntity<OptionDTO> getOption(@PathVariable  Long idOption) {
        return ResponseEntity.ok(optionService.get(idOption));
    }

    @PostMapping
    public ResponseEntity<Long> createOption(@RequestBody @Valid  OptionDTO optionDTO) {
        return new ResponseEntity<>(optionService.create(optionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{idOption}")
    public ResponseEntity<Void> updateOption(@PathVariable  Long idOption,
            @RequestBody @Valid  OptionDTO optionDTO) {
        optionService.update(idOption, optionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idOption}")
    public ResponseEntity<Void> deleteOption(@PathVariable  Long idOption) {
        optionService.delete(idOption);
        return ResponseEntity.noContent().build();
    }

}

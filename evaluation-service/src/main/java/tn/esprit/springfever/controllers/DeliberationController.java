package tn.esprit.springfever.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IServiceDeliberation;
import tn.esprit.springfever.entities.Deliberation;

import java.util.List;

@RestController
@RequestMapping("/deliberations")
public class DeliberationController {


    @Autowired
    IServiceDeliberation deliberationService;


    @PostMapping("/addDeliberation")
    public ResponseEntity<Deliberation> addDeliberation(@RequestBody Deliberation deliberation) {
        Deliberation savedDeliberation = deliberationService.addDeliberation(deliberation);
        return new ResponseEntity<>(savedDeliberation, HttpStatus.CREATED);
    }

    @GetMapping("/getAllDeliberations")
    public ResponseEntity<List<Deliberation>> getAllDeliberations() {
        List<Deliberation> deliberations = deliberationService.getAllDeliberations();
        return new ResponseEntity<>(deliberations, HttpStatus.OK);
    }

    @GetMapping("/getDeliberationById/{id}")
    public ResponseEntity<Deliberation> getDeliberationById(@PathVariable("id") Long id) {
        Deliberation deliberation = deliberationService.getDeliberationById(id);
        return new ResponseEntity<>(deliberation, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDeliberation/{id}")
    public ResponseEntity<String> deleteDeliberation(@PathVariable("id") Long id) {
        String result = deliberationService.deleteDeliberation(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("updateDeliberation/{id}")
    public ResponseEntity<Deliberation> updateDeliberation(@PathVariable("id") Long id, @RequestBody Deliberation deliberation) {
        Deliberation updatedDeliberation = deliberationService.updateDeliberation(id, deliberation);
        return new ResponseEntity<>(updatedDeliberation, HttpStatus.OK);
    }

    @GetMapping("/getDeliberationOfUser/{username}")
    public ResponseEntity<Deliberation> getDeliberationOfUser(@PathVariable("username") String username) {
        Deliberation deliberation = deliberationService.getDeliberationOfUser(username);
        return new ResponseEntity<>(deliberation, HttpStatus.OK);
    }
}

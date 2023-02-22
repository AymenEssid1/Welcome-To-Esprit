package tn.esprit.springfever.Controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import   tn.esprit.springfever.Services.Interfaces.*;
import tn.esprit.springfever.entities.Mcq;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/mcq")
public class McqController {

    @Autowired
    private IServiceMcq mcqService;

    @PostMapping("/add")
    public ResponseEntity<Mcq> addMcq(@RequestBody Mcq mcq) {
       Mcq addeMcq = mcqService.addMcq(mcq) ;
        return new ResponseEntity<>(addeMcq, HttpStatus.CREATED);
    }

    @GetMapping("/getAllMcqs")
    public ResponseEntity<List<Mcq>> getAllMcqs() {
        return ResponseEntity.ok(mcqService.getAllMcqs());
    }

    @GetMapping("/getMcq/{idMcq}")
    public  Mcq  getMcq(@PathVariable Long idMcq) {
        return  mcqService.getMcq(idMcq) ;

    }

    @PutMapping("/updateMcq/{idMcq}")
    public ResponseEntity<Mcq> updateMcq(@PathVariable Long idMcq, @RequestBody Mcq mcq) {
        return ResponseEntity.ok(mcqService.updateMcq(idMcq, mcq));
    }

    @DeleteMapping("/deleteMcq/{idMcq}")
    public ResponseEntity<String> deleteMcq(@PathVariable Long idMcq) {
        return ResponseEntity.ok(mcqService.deleteMcq(idMcq));
    }


}
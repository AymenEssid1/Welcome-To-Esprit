package tn.esprit.springfever.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import   tn.esprit.springfever.Services.Interfaces.*;
import tn.esprit.springfever.entities.Mcq;
import java.io.IOException;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import tn.esprit.springfever.Exceptions.ValidationExceptionHandler;
import javax.validation.Valid;
@RestController
@RequestMapping("/mcq")
public class McqController {

    @Autowired
    private IServiceMcq mcqService;

    @PostMapping("/add")
    public ResponseEntity<?> addMcq(@Valid @RequestBody Mcq mcq, BindingResult result) {
        if (result.hasErrors()) {
            ValidationExceptionHandler.ValidationErrorResponse response = new ValidationExceptionHandler.ValidationErrorResponse();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError error : fieldErrors) {
                response.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Mcq addedMcq = mcqService.addMcq(mcq);
        return new ResponseEntity<>(addedMcq, HttpStatus.CREATED);
    }


    @GetMapping("/getAllMcqs")
    @CrossOrigin(origins = "http://localhost:4200")
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

    @PostMapping("/generate/{diplomaTitle}")
    public Mcq generateMcq(@PathVariable String diplomaTitle) throws IOException {
        return mcqService.generateMcq(diplomaTitle);
    }



}
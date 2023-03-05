package tn.esprit.springfever.Controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Exceptions.ValidationExceptionHandler;
import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.Services.Interfaces.IServiceUser;
import tn.esprit.springfever.entities.Claim;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RequestMapping("/Claims")
@RestController(value = "/claims")
@Api( tags = "claims")
@CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
public class ClaimController {

    @Autowired
    IServiceUser iServiceUser;
    @Autowired
    IServiceClaims iServiceClaims;
    @Autowired
    private JwtUtils jwtUtils;

    /*********  add claim  ***********/
    @ApiOperation(value = "This method is used to add claim ")

    @PostMapping("/add")
    public ResponseEntity<?> addClaim(@Valid @RequestBody Claim claim, BindingResult result) throws IOException {
        if(result.hasErrors()) {
            ValidationExceptionHandler.ValidationErrorResponse response = new ValidationExceptionHandler.ValidationErrorResponse();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for(FieldError error : fieldErrors) {
                response.addError(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Claim addedClaim = iServiceClaims.addClaim(claim);
        return new ResponseEntity<>(addedClaim, HttpStatus.OK);
    }
    /*********  update claim  ***********/
    @PutMapping("/update/{idClaim}")
    @ResponseBody
    public Claim updateClaim(@PathVariable Long idClaim, @RequestBody ClaimDTO claimDTO )  {return  iServiceClaims.updateClaim(idClaim , claimDTO);}
    /*********  get all claims   ***********/
    @GetMapping("/getAllClaims")
    @ResponseBody
    public List<Claim> getAllClaims()  {return  iServiceClaims.getAllClaims();}
    /*********  delete claim  ***********/
    @DeleteMapping("/deleteClaim/{idClaim}")
    @ResponseBody
    public  boolean deleteClaim(@PathVariable Long idClaim)  {return  iServiceClaims.deleteClaim(idClaim);}
    /*********  get all claims  by user id (this method will be changed
     * we will get the list not by id but by username that will be extracted from the token )  ***********/
    @GetMapping("/getClaimsByUser")
    @ResponseBody
    public List<Claim> getClaimsByUser( @RequestHeader("AUTHORIZATION") String header)  {
      String token = header.substring(7);
      String username =  iServiceUser.getusernamefromtoken(token);
      return  iServiceClaims.getClaimsByUser(username);
    }

    @PutMapping("treatClaim/{id}")
    public Claim treatClaim(@PathVariable Long id, @RequestParam String decision) {
        return iServiceClaims.treatClaim(id, decision);
    }

    @GetMapping("getTimeTreatmentClaim/{id}")
    public long getTimeTreatmentClaim(@PathVariable Long id) {
        return iServiceClaims.getTimeTreatmentClaim(id);
    }

    @GetMapping("predictTreatmentPeriod/{id}")
    @CrossOrigin(origins = "http://localhost:4200" , allowCredentials = "true")
    public ResponseEntity<Long> predictTreatmentPeriod(@PathVariable Long id) {
        Long predictedPeriod = iServiceClaims.predicateTreatmentClaim(id);
        return ResponseEntity.ok(predictedPeriod);
    }

    @PostMapping("/sentiment")
    public ResponseEntity<String> analyzeSentiment(@RequestBody String text) {
        String sentiment = iServiceClaims.analyzeSentiment(text);
        return ResponseEntity.ok(sentiment);
    }

    @PutMapping("/{id}/feedback")
    public String sentFeedback(@PathVariable("id") Long idClaim, @RequestBody String feedback) {
        return iServiceClaims.sentFeedback(idClaim,feedback);

    }
     @PostMapping(value="/badWordsCheck/{claim}" )

    public boolean badWordsFound(@PathVariable("claim") String claimBody ) throws IOException {
        return  iServiceClaims.badWordsFound(claimBody  );

    }


}



/*
    @DeleteMapping("/delete/{recipe-id}")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestHeader("AUTHORIZATION") String header, @PathVariable("recipe-id") Long recipeId) {
        String username = userService.getusernamefromtoken(header);
        String msg = favorisService.delete(username, recipeId);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);

    }

 */
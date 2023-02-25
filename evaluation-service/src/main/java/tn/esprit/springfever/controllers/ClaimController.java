package tn.esprit.springfever.Controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.DTO.ClaimDTO;
import tn.esprit.springfever.Security.jwt.JwtUtils;
import tn.esprit.springfever.Services.Interfaces.IServiceClaims;
import tn.esprit.springfever.Services.Interfaces.IServiceUser;
import tn.esprit.springfever.entities.Claim;

import java.io.IOException;
import java.util.List;


@RequestMapping("/Claims")
@RestController(value = "/claims")
@Api( tags = "claims")

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
    @ResponseBody
    public Claim addClaim(@RequestBody Claim claim) throws IOException {return  iServiceClaims.addClaim(claim);}
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
    public ResponseEntity<Long> predictTreatmentPeriod(@PathVariable Long id) {
        Long predictedPeriod = iServiceClaims.predicateTreatmetnClaim(id);
        return ResponseEntity.ok(predictedPeriod);
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
package tn.esprit.springfever.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.springfever.Services.Interfaces.IServiceUser;

@RestController
@RequestMapping("/User")
public class UserController {


    @Autowired
    IServiceUser iServiceUser;

/*
    @PostMapping("/add")
    @ResponseBody
    public void addFormateur(@RequestBody Formateur formateur) {
        iServiceFormateur.ajouterFormateur(formateur);
    }

    @GetMapping(value = "/getFormateurRemunerationByDate/{idFormateur}/{startDate}/{finDate}")
    @ResponseBody
    public float getFormateurRemunerationByDate(@PathVariable("idFormateur") int idFormateur ,  @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate ,  @PathVariable("finDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date finDate) {
        return iServiceFormateur.getFormateurRemunerationByDate(idFormateur ,startDate ,finDate);
    }


*/

}

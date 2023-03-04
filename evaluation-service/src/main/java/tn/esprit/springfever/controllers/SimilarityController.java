package tn.esprit.springfever.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Interfaces.IStringsimilarity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



@RestController
@RequestMapping("/simu")
public class SimilarityController {


    @Autowired
    IStringsimilarity iStringsimilarity ;

    @GetMapping("/{diplomaTitle}/{question}")
    public double calculateSimilarity(@PathVariable String diplomaTitle, @PathVariable String question) throws IOException {
        return iStringsimilarity.calculateSimilarity(diplomaTitle, question);
    }





}
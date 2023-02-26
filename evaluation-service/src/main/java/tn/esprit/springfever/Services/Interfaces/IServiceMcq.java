package tn.esprit.springfever.Services.Interfaces;

import tn.esprit.springfever.entities.Mcq;

import java.io.IOException;
import java.util.List;

public interface IServiceMcq {

        Mcq addMcq(Mcq mcq);
        List<Mcq> getAllMcqs();
        String deleteMcq(Long idMcq);
        Mcq updateMcq(Long idMcq, Mcq mcq);
        Mcq getMcq(Long idMcq);
        Mcq generateMcq(String diplomaTitle) throws IOException ;
       // Mcq generateMcq(String diplomaTitle) throws IOException ;

}



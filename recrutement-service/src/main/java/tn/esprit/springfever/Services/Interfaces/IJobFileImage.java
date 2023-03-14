package tn.esprit.springfever.Services.Interfaces;

import org.springframework.core.io.FileSystemResource;
import tn.esprit.springfever.entities.Image_JobOffer;

public interface IJobFileImage {
    public Image_JobOffer save(byte[] bytes, String imageName) throws Exception ;
    public FileSystemResource find(Long imageId) ;
}

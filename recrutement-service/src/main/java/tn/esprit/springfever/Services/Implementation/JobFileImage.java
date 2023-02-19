package tn.esprit.springfever.Services.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.Services.Interfaces.IJobFileImage;
import tn.esprit.springfever.entities.Image_JobOffer;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.Image_JobOfferRepository;
@Service
public class JobFileImage implements IJobFileImage {

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Autowired
    Image_JobOfferRepository image_jobOfferRepository;

    public Image_JobOffer save(byte[] bytes, String imageName) throws Exception {
        String location = fileSystemRepository.save(bytes, imageName);
        return image_jobOfferRepository.save(new Image_JobOffer(imageName, location));
    }
    public FileSystemResource find(Long imageId) {
        Image_JobOffer image = image_jobOfferRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }
}

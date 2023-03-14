package tn.esprit.springfever.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.springfever.domain.Image;
import tn.esprit.springfever.repos.FileSystemRepository;
import tn.esprit.springfever.repos.ImageRepository;


@Service
public class FileLocationService  {


    @Autowired
    FileSystemRepository fileSystemRepository;
    @Autowired
    ImageRepository imageDataRepository;

    public Image save(MultipartFile file) throws Exception {
        String location = fileSystemRepository.save(file);
        return imageDataRepository.save(new Image(file.getOriginalFilename(), location));
    }
    public FileSystemResource find(Long imageId) {
        Image image = imageDataRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return fileSystemRepository.findInFileSystem(image.getLocation());
    }

}

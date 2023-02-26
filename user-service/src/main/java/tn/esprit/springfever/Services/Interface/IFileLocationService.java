package tn.esprit.springfever.Services.Interface;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Image;
import tn.esprit.springfever.entities.User;

public interface IFileLocationService {
    public Image save(MultipartFile file) throws Exception ;
    public FileSystemResource find(Long imageId) ;

}

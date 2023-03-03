package tn.esprit.springfever.Services.Interfaces;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.ImageDataRepository;


public interface IFileLocationService {
    public ImageData save(MultipartFile file) throws Exception ;
    public FileSystemResource find(Long imageId) ;



}

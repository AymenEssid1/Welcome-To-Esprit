package tn.esprit.springfever.Services.Interfaces;
import org.springframework.core.io.FileSystemResource;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.entities.ImageData;
import tn.esprit.springfever.repositories.FileSystemRepository;
import tn.esprit.springfever.repositories.ImageDataRepository;


public interface IFileLocationService {
    public ImageData save(byte[] bytes, String imageName) throws Exception ;
    public FileSystemResource find(Long imageId) ;

}

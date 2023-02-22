package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.entities.AdMedia;

public interface IAdMediaService {
    public AdMedia save(MultipartFile file, Ad Ad) throws Exception ;
    public FileSystemResource find(Long imageId) ;
    public void delete(Long id);
}

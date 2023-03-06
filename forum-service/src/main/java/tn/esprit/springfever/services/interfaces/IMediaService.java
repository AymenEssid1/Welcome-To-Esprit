package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Media;

public interface IMediaService {
    public Media save(MultipartFile file) throws Exception ;
    public FileSystemResource find(Long imageId) ;
    public Media findPath(Long imageId) ;
    public void delete(Long id);
}

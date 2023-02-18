package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import tn.esprit.springfever.entities.PostMedia;

import java.io.IOException;
import java.util.List;

public interface IPostMediaService {
    public PostMedia save(byte[] bytes, String imageName) throws Exception ;
    public FileSystemResource find(Long imageId) ;
}

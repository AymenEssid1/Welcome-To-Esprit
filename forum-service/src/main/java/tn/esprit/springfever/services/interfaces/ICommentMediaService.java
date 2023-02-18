package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import tn.esprit.springfever.entities.CommentMedia;
public interface ICommentMediaService {
    public CommentMedia save(byte[] bytes, String imageName) throws Exception ;
    public FileSystemResource find(Long imageId) ;
}

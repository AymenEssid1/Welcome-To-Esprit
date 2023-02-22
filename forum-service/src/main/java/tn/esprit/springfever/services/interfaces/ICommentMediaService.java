package tn.esprit.springfever.services.interfaces;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Comment;
import tn.esprit.springfever.entities.CommentMedia;

public interface ICommentMediaService {
    public CommentMedia save(MultipartFile file, Comment comment) throws Exception ;
    public FileSystemResource find(Long imageId) ;
    public void delete(Long id);
}

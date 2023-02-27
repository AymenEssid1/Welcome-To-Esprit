package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Likes;

import java.util.List;

public interface ILikesService {
    public Likes addLike(Likes like);
    public Likes updatePostLike(Long id, Long reaction, Long user);
    public String deletePostLike(Long id, Long user);
    public List<Likes> findByUser(Long user);
    public Likes findById(Long id);
}

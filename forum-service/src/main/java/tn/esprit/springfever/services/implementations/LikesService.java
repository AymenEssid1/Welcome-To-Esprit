package tn.esprit.springfever.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Likes;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.repositories.LikesRepository;
import tn.esprit.springfever.repositories.ReactionRepository;
import tn.esprit.springfever.services.interfaces.ILikesService;

import java.util.List;

@Service
public class LikesService implements ILikesService {
    @Autowired
    private LikesRepository repo;
    @Autowired
    private ReactionRepository reactionRepository;
    @Override
    public Likes updatePostLike(Long id,Long reaction, Long user) {
        Likes p = repo.findById(Long.valueOf(id)).orElse(null) ;
        Reaction r = reactionRepository.findById(reaction).orElse(null);
        if(p!=null) {
            if (r!=null && p.getUser()== user){
                p.setType(r);
            }
        }
        return p;
    }

    @Override
    public Likes addLike(Likes like) {
        return repo.save(like);
    }

    @Override
    public String deletePostLike(Long id, Long user) {
        Likes p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null && p.getUser()==user) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    public List<Likes> findByUser(Long user) {
        return repo.findByUser(user);
    }
    @Override
    public Likes findById(Long id){
        return repo.findById(id).orElse(null);
    }

}

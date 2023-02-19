package tn.esprit.springfever.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.repositories.AdRepository;
import tn.esprit.springfever.services.interfaces.IAdService;

import java.util.List;

@Service
@Slf4j
public class AdService implements IAdService {
    @Autowired
    private AdRepository repo;
    @Override
    public Ad addAd(Ad ad) {
        return repo.save(ad);
    }

    @Override
    public Ad updateAd(Long id, Ad ad) {
        Ad p = repo.findById(Long.valueOf(id)).orElse(null) ;
        if(p!=null) {
            ad.setId(p.getId());
            repo.save(ad);
        }
        return p;
    }

    @Override
    public String deleteAd(Long ad) {
        Ad p = repo.findById(Long.valueOf(ad)).orElse(null) ;
        if(p!=null) {
            repo.delete(p);
            return "Post was successfully deleted !" ;
        }
        return "Not Found ! ";
    }

    @Override
    public List<Ad> getAllAds() {
        return repo.findAll();
    }
}

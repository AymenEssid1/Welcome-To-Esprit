package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Ad;

import java.util.List;

public interface IAdService {
    public Ad addAd(Ad ad);
    public Ad updateAd(Long id,Ad ad);
    public String deleteAd(Long ad);
    public List<Ad> getAllAds();
}

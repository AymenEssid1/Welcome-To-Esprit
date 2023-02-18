package tn.esprit.springfever.services.interfaces;

import tn.esprit.springfever.entities.Ad;

import java.util.List;

public interface IAdService {
    public Ad addAd(Ad ad);
    public Ad updateAd(int id,Ad ad);
    public String deleteAd(int ad);
    public List<Ad> getAllAds();
}

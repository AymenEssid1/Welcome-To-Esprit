package tn.esprit.springfever.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.entities.AdViews;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostViews;

import java.util.List;

@EnableJpaRepositories
public interface AdViewsRepository extends JpaRepository<AdViews,Long> {
    public List<AdViews> findByAdAndUser(Ad ad, Long user);
}

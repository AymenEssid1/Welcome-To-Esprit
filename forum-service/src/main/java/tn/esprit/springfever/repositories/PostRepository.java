package tn.esprit.springfever.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Post;


import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface PostRepository extends JpaRepository<Post,Long> {
    public List<Post> findByUser(Long id);
}
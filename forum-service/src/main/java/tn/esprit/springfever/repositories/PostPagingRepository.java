package tn.esprit.springfever.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import tn.esprit.springfever.entities.Post;

public interface PostPagingRepository extends PagingAndSortingRepository<Post, Long> {
    public Page<Post> findByUser(PageRequest pageable, Long id);
}

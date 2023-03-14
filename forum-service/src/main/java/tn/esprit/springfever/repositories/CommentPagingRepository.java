package tn.esprit.springfever.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.PagingAndSortingRepository;
import tn.esprit.springfever.entities.Comment;


public interface CommentPagingRepository extends PagingAndSortingRepository<Comment, Long> {
    public Page<Comment> findByUser(PageRequest pageable, Long id);
}

package tn.esprit.springfever.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import tn.esprit.springfever.entities.Ad;

public interface AdPagingRepository extends PagingAndSortingRepository<Ad, Long> {
}

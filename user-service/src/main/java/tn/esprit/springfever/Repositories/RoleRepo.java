package tn.esprit.springfever.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.RoleType;
import tn.esprit.springfever.entities.User;

import java.util.List;
import java.util.Optional;


    @EnableJpaRepositories
    @Repository
    public interface RoleRepo extends JpaRepository<Role,Long> {

        public Role findByRolename(RoleType roletype);


    }


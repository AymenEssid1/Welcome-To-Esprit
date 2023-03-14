package tn.esprit.springfever.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("tn.esprit.springfever.domain")
@EnableJpaRepositories("tn.esprit.springfever.repos")
@EnableTransactionManagement
public class DomainConfig {
}

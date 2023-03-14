package tn.esprit.springfever.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import tn.esprit.springfever.entities.User;


@Slf4j
public class ProjectProcessor implements ItemProcessor<User , User> {
	@Override
	public User process(User c) {
		return c;
	}
}

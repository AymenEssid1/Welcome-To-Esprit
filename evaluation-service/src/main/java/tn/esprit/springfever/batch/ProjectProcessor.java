package tn.esprit.springfever.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import tn.esprit.springfever.entities.Claim;


@Slf4j
public class ProjectProcessor implements ItemProcessor<Claim , Claim> {
	@Override
	public Claim process(Claim c) {
		log.info("Start Batch Item Processor");
		return c;
	}
}

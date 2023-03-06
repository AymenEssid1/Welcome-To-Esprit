package tn.esprit.springfever.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import tn.esprit.springfever.Services.Interfaces.IServiceFaq;
import tn.esprit.springfever.entities.Faq;
import tn.esprit.springfever.repositories.FaqRepository;


@Slf4j
public class ProjectProcessor implements ItemProcessor<Faq, Faq> {
	/*12. logique métier de notre job*/

	@Autowired
	IServiceFaq iServiceFaq ;


	@Override
	public Faq process(Faq Faq) {
		log.info("Start Batch Item Processor");

		return Faq;
	}
}

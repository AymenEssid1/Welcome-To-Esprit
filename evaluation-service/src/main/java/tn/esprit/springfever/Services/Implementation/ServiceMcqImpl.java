package tn.esprit.springfever.Services.Implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Services.Interfaces.IServiceMcq;
import tn.esprit.springfever.entities.Mcq;
import tn.esprit.springfever.repositories.McqRepository;

import java.util.List;

@Service
@Slf4j
public class ServiceMcqImpl implements IServiceMcq {

    @Autowired
    private McqRepository mcqRepository;

    @Override
    public Mcq addMcq(Mcq mcq) {
        return mcqRepository.save(mcq);
    }

    @Override
    public List<Mcq> getAllMcqs() {
        return mcqRepository.findAll();
    }

    @Override
    public String deleteMcq(Long idMcq) {
        Mcq existingMcq = mcqRepository.findById(idMcq).orElse(null);
        if (existingMcq != null) {
            mcqRepository.delete(existingMcq);
            return "Mcq with ID " + idMcq + " deleted successfully";
        }
        return "Mcq with ID " + idMcq + " not found";
    }

    @Override
    public Mcq updateMcq(Long idMcq, Mcq mcq) {
        Mcq existingMcq = mcqRepository.findById(idMcq).orElse(null);
        if (existingMcq != null) {
            existingMcq.setMcqTitle(mcq.getMcqTitle());
            existingMcq.setDuration(mcq.getDuration());
            existingMcq.setQuestions(mcq.getQuestions());
            mcqRepository.save(existingMcq);
        }
        return existingMcq;
    }

    @Override
    public Mcq getMcq(Long idMcq) {
        return mcqRepository.findById(idMcq).orElse(null);
    }


}

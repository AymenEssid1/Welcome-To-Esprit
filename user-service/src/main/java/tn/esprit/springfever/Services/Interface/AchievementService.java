package tn.esprit.springfever.Services.Interface;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AchievementService {

    public void createAchievementForTopThreeActiveUsers() ;
    public List<Long> getUserDetailsFromId() throws JsonProcessingException;
}

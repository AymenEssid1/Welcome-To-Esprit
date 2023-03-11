package tn.esprit.springfever.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.springfever.Repositories.AchievmentRepo;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.Services.Interface.AchievementService;
import tn.esprit.springfever.entities.Achievement;
import tn.esprit.springfever.entities.User;
import tn.esprit.springfever.tools.ResourceNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {



    @Autowired
    private UserRepo userRepository;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private AchievmentRepo achievmentRepo;




    @GetMapping("/Most_Liked_Users")
    public List<User> getusers(){
        List<User> topUsers = userRepository.mostLikedUsers();

        Achievement achievement = new Achievement();
        achievement.setIcon(Achievement.Icon.STAR);
        achievement.setName("highly rated user");
        for(User user : topUsers) {
            user.getAchis().add(achievement);
            achievmentRepo.save(achievement);
        }

        return topUsers;


    }

    @GetMapping("/Statistics")

    public String Stats(){

        List<Object> avgPostsPerDayList = userRepository.averagepostsperday();
        List<Object> avgUsersPerYearList = userRepository.averageUsersCreatedPerYear();
        List<String> bannedUsersList = userRepository.bannedUsers();

        String ch = "Average posts per day : " + avgPostsPerDayList.get(0).toString() + "\n" +
                "Average new number of new users by year " + avgUsersPerYearList.get(0).toString() + "\n" +
                "Users grouped by age : "+userRepository.UsersByAge()+"\n\n"+
                "List of banned users : " + bannedUsersList.toString();


        return ch;
    }

    // Get all achievements
    @GetMapping
    public List<Achievement> getAllAchievements() {
        return achievmentRepo.findAll();
    }

    // Create a new achievement
    @PostMapping
    public Achievement createAchievement(@RequestBody Achievement achievement) {
        return achievmentRepo.save(achievement);
    }

    // Get an achievement by ID
    @GetMapping("/{id}")
    public Achievement getAchievementById(@PathVariable Long id) throws ResourceNotFoundException {
        return achievmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));
    }

    // Update an achievement by ID
    @PutMapping("/{id}")
    public Achievement updateAchievement(@PathVariable Long id, @RequestBody Achievement achievementData) throws ResourceNotFoundException {
        Achievement achievement = achievmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));

        achievement.setName(achievementData.getName());
        achievement.setIcon(achievementData.getIcon());

        return achievmentRepo.save(achievement);
    }

    // Delete an achievement by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAchievement(@PathVariable Long id) throws ResourceNotFoundException {
        Achievement achievement = achievmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", "id", id));

        achievmentRepo.delete(achievement);

        return ResponseEntity.ok().build();
    }

}

package tn.esprit.springfever.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import tn.esprit.springfever.Repositories.BadgeRepo;
import tn.esprit.springfever.Services.Interface.BadgeService;
import tn.esprit.springfever.Services.Interface.IServiceUser;
import tn.esprit.springfever.entities.Badge;
import tn.esprit.springfever.entities.Role;
import tn.esprit.springfever.entities.User;

import java.util.List;

@RestController
@RequestMapping("/badges")
public class BadgeController {


    @Autowired
    private BadgeRepo badgeRepository;

    @Autowired
    IServiceUser iServiceUser;


    @PostMapping("/add badge/{id}")
    public Badge saveBadge( @PathVariable(value ="id" )Long userId) {
        User u=iServiceUser.getSingleUser(userId);
        Badge badge = new Badge();
        badge.setUser(u);
        badge.setQrCode(iServiceUser.generateQr(u));

        u.setBadge(badge);
        return badgeRepository.save(badge);
    }



    @DeleteMapping ("/Deletebadge")
    public void deleteBadgeById(Long badgeId) {
        badgeRepository.deleteById(badgeId);
    }

    @GetMapping("/getby/{id}")
    public Badge getBadgeById(Long badgeId) {
        return badgeRepository.findById(badgeId)
                .orElseThrow(() -> new NotFoundException("Badge not found with id " + badgeId));
    }

    @GetMapping("/allbadges")
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

}


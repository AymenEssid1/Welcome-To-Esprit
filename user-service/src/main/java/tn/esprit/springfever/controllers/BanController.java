package tn.esprit.springfever.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Repositories.BanRepository;
import tn.esprit.springfever.entities.Ban;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bans")
public class BanController {

    @Autowired
    private BanRepository banRepository;

    // GET all Bans
    @GetMapping("/getAllBans/")
    public List<Ban> getAllBans() {
        return banRepository.findAll();
    }

    // GET a specific Ban by ID
    @GetMapping("/getbanby/{id}")
    public ResponseEntity<Ban> getBanById(@PathVariable(value = "id") Long banId) {
        Optional<Ban> ban = banRepository.findById(banId);
        if (ban.isPresent()) {
            return ResponseEntity.ok().body(ban.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // CREATE a new Ban
    @PostMapping("/addBan")
    public Ban createBan(@RequestBody Ban ban) {
        return banRepository.save(ban);
    }

    // UPDATE an existing Ban
    @PutMapping("/updateBan/{id}")
    public ResponseEntity<Ban> updateBan(@PathVariable(value = "id") Long banId,
                                         @RequestBody Ban banDetails) {
        Optional<Ban> optionalBan = banRepository.findById(banId);
        if (optionalBan.isPresent()) {
            Ban ban = optionalBan.get();
            ban.setLastFailedLoginAttempt(banDetails.getLastFailedLoginAttempt());
            ban.setExpiryTime(banDetails.getExpiryTime());
            Ban updatedBan = banRepository.save(ban);
            return ResponseEntity.ok(updatedBan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE a Ban
    @DeleteMapping("/delete/{id}")
    public String deleteBan(@PathVariable(value = "id") Long banId) {
        Ban optionalBan = banRepository.findById(banId).orElse(null);

        System.out.println(optionalBan);
        if (optionalBan!=null) {
           // Ban ban = optionalBan.get();
            optionalBan.setUser(null);
            banRepository.fasakh(optionalBan.getId());
            System.out.println("mrigel");
            return "ok";
        } else {
            return "Not ok";
        }
    }
}


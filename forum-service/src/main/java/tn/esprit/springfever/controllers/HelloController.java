package tn.esprit.springfever.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Hello World")
@Service
@RequestMapping("/api/forum")
public class HelloController {
    @GetMapping
    public String hello(){
        return "Hello from Forum Service";
    }
}

package tn.esprit.springfever.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Reaction;
import tn.esprit.springfever.services.interfaces.IReactionService;

import java.util.List;

@RestController
@RequestMapping("reacts")
@Tag(name = "Reactions Module")
@Api(tags = "Reactions Module")
@Service
public class ReactionController {
    @Autowired
    private IReactionService service;

    @ApiOperation(value = "This method is used to add a reaction ")
    @PostMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Reaction> addReaction(@RequestParam String name, @RequestParam(name = "file", required = false) MultipartFile image) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(image, name));
    }

    @ApiOperation(value = "This method is used to delete a post ")
    @DeleteMapping(value = "/")
    @ResponseBody
    public ResponseEntity<String> deleteReaction(Long id) {
        service.delete(id);
        return ResponseEntity.ok().body("Reaction deleted!");
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Reaction>> getAll() {
        return ResponseEntity.ok().body(service.getAll());

    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Reaction> getReactionById(Long id) {
        return ResponseEntity.ok().body(service.getById(id));

    }

    @ApiOperation(value = "This method is used to add a reaction ")
    @PutMapping(value = "/", consumes = "multipart/form-data", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Reaction> updateReaction(@RequestParam Long id, @RequestParam(required = false) String name, @RequestParam(name = "file", required = false) MultipartFile image) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.updateReaction(id, name, image));
    }

    @GetMapping(value = "/media/{imageId}")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "*/*"))
    @ApiResponse(responseCode = "404", description = "Image not found")
    @Schema(format = "binary")
    public ResponseEntity<FileSystemResource> getMediaById(@PathVariable Long imageId) {
        try {
            FileSystemResource fileSystemResource = service.find(imageId);
            MediaType mediaType = MediaTypeFactory.getMediaType(fileSystemResource).orElse(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().contentType(mediaType).body(fileSystemResource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

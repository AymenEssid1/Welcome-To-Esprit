package tn.esprit.springfever.controllers;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.entities.CommentLike;
import tn.esprit.springfever.services.interfaces.ICommentLikeService;

@RestController
@RequestMapping("/comment/like")
@Api(tags = "CommentLikes Module")
@Tag(name = "CommentLikes Module")
public class CommentLikeController {
    @Autowired
    private ICommentLikeService service;
    @ApiOperation(value = "This method is used to like a post ")
    @PostMapping(value = "/")
    @ResponseBody
    public ResponseEntity<CommentLike> like(CommentLike pl){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCommentLike(pl));
    }
    @ApiOperation(value = "This method is used to unlike a post")
    @DeleteMapping(value = "/")
    public ResponseEntity<String> unlike(Long id){
        return ResponseEntity.ok().body(service.deleteCommentLike(id));
    }
    @ApiOperation(value = "This method is used to change the type of likes")
    @PutMapping(value = "/")
    @ResponseBody
    public ResponseEntity<CommentLike> change(Long id, CommentLike pl){
        return ResponseEntity.ok().body(service.updateCommentLike(id, pl));
    }

}

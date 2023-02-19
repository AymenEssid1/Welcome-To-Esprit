package tn.esprit.springfever.payload.response;

import lombok.Data;
import tn.esprit.springfever.entities.Post;
import tn.esprit.springfever.entities.PostMedia;

import java.util.List;

@Data
public class PostResponse {
    private Post post;
    private List<PostMediaResponse> files;

    public PostResponse(Post post, List<PostMediaResponse> files) {
        this.post = post;
        this.files = files;
    }

    public Post getPost() {
        return post;
    }

    public List<PostMediaResponse> getFiles() {
        return files;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setFiles(List<PostMediaResponse> files) {
        this.files = files;
    }
}


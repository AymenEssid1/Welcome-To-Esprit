package tn.esprit.springfever.utils;

import lombok.SneakyThrows;
import tn.esprit.springfever.entities.Post;

import java.util.Comparator;

public class PostComparator implements Comparator<Post> {
    @SneakyThrows
    @Override
    public int compare(Post file1, Post file2) {
        double size1 = file1.getSimilarity();
        double size2 = file2.getSimilarity();
        if (size1 < size2) {
            return -1;
        } else if (size1 > size2) {
            return 1;
        } else {
            return 0;
        }
    }
}

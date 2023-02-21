package tn.esprit.springfever.utils;

import lombok.SneakyThrows;
import tn.esprit.springfever.entities.CommentMedia;
import tn.esprit.springfever.entities.PostMedia;

import java.util.Comparator;

public class CommentMediaComparator implements Comparator<CommentMedia> {
    @SneakyThrows
    @Override
    public int compare(CommentMedia file1, CommentMedia file2) {
        long size1 = file1.getContent().length;
        long size2 = file2.getContent().length;
        if (size1 < size2) {
            return -1;
        } else if (size1 > size2) {
            return 1;
        } else {
            return 0;
        }
    }
}

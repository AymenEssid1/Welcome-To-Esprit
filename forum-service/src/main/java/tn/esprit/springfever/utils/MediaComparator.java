package tn.esprit.springfever.utils;

import lombok.SneakyThrows;
import tn.esprit.springfever.entities.Media;

import java.util.Comparator;

public class MediaComparator implements Comparator<Media>{
    @SneakyThrows
    @Override
    public int compare(Media file1, Media file2) {
        int size1 = file1.getContent().length;
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

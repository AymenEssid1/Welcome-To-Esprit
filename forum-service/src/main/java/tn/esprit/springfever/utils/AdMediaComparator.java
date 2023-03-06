package tn.esprit.springfever.utils;

import lombok.SneakyThrows;
import tn.esprit.springfever.entities.AdMedia;

import java.util.Comparator;

public class AdMediaComparator implements Comparator<AdMedia> {
    @SneakyThrows
    @Override
    public int compare(AdMedia file1, AdMedia file2) {
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

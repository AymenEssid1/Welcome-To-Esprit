package tn.esprit.springfever.utils;

import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;

public class MultipartFileSizeComparator implements Comparator<MultipartFile> {
    @SneakyThrows
    @Override
    public int compare(MultipartFile file1, MultipartFile file2) {
        int size1 = file1.getBytes().length;
        long size2 = file2.getBytes().length;
        if (size1 < size2) {
            return -1;
        } else if (size1 > size2) {
            return 1;
        } else {
            return 0;
        }
    }
}

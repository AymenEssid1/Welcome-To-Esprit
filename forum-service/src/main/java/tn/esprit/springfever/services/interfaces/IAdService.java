package tn.esprit.springfever.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.entities.Ad;
import tn.esprit.springfever.enums.Channel;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IAdService {
    public ResponseEntity<?> addAd(Channel channel, float cost, Date startDate,
                                Date endDate, int initialTarget, String name,
                                Long targetPopulation, List<MultipartFile> images, HttpServletRequest authentication) throws IOException;
    public ResponseEntity<?> updateAd(Long id,Channel channel, float cost, Date startDate,
                       Date endDate, int initialTarget, String name,
                       Long targetPopulation, List<MultipartFile> images, HttpServletRequest authentication) throws IOException;
    public ResponseEntity<String> deleteAd(Long ad, HttpServletRequest request) throws JsonProcessingException;
    public Ad getSingleAd(Long id, HttpServletRequest request);
    public List<Ad> getAllLazy(int page, int size, HttpServletRequest request);
}

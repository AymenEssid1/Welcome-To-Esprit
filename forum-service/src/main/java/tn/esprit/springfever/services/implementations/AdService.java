package tn.esprit.springfever.services.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.springfever.dto.UserDTO;
import tn.esprit.springfever.entities.*;
import tn.esprit.springfever.enums.Channel;
import tn.esprit.springfever.repositories.AdPagingRepository;
import tn.esprit.springfever.repositories.AdRepository;
import tn.esprit.springfever.repositories.AdViewsRepository;
import tn.esprit.springfever.repositories.TargetPopulationRepository;
import tn.esprit.springfever.services.interfaces.IAdMediaService;
import tn.esprit.springfever.services.interfaces.IAdService;
import tn.esprit.springfever.services.interfaces.IUserService;
import tn.esprit.springfever.services.interprocess.RabbitMQMessageSender;
import tn.esprit.springfever.utils.AdMediaComparator;
import tn.esprit.springfever.utils.MediaComparator;
import tn.esprit.springfever.utils.MultipartFileSizeComparator;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdService implements IAdService {
    @Autowired
    private AdRepository repo;
    @Autowired
    private IAdMediaService mediaService;

    @Autowired
    private IUserService userService;

    @Autowired
    private TargetPopulationRepository targetPopulationRepository;

    @Autowired
    private AdPagingRepository pagerepo;

    @Autowired
    private AdViewsRepository adViewsRepository;

    @Override
    public ResponseEntity<?> addAd(Channel channel, float cost, Date startDate,
                                   Date endDate, int initialTarget, String name,
                                   Long targetPopulation, List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        if (authentication != null && authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            UserDTO user = userService.getUserDetailsFromToken(
                    authentication.getHeader(HttpHeaders.AUTHORIZATION));
            if (user != null && (user.getRoles().contains("SUPER_ADMIN") || !user.getRoles().contains("FORUM_ADMIN"))) {
                Ad c = new Ad();
                TargetPopulation t = targetPopulationRepository.findById(targetPopulation).orElse(null);
                if (t != null) {
                    c.setTypeOfAudience(t);
                }

                c.setChannel(channel);
                c.setCost(cost);
                c.setEndDate(endDate);
                c.setStartDate(startDate);
                c.setInitialTargetNumberOfViews(initialTarget);

                c.setName(name);
                Ad newAd = repo.save(c);
                if (images != null) {
                    for (MultipartFile image : images) {
                        if (!image.isEmpty()) {
                            try {
                                AdMedia savedImageData = mediaService.save(image, newAd);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                }
                return ResponseEntity.ok().body(newAd);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have the right to post an ad");
            }

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have to login");
    }


    @Override
    @CachePut("ad")
    public ResponseEntity<?> updateAd(Long id, Channel channel, float cost, Date startDate,
                                      Date endDate, int initialTarget, String name,
                                      Long targetPopulation, List<MultipartFile> images, HttpServletRequest authentication) throws IOException {
        if (authentication != null && authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            UserDTO user = userService.getUserDetailsFromToken(
                    authentication.getHeader(HttpHeaders.AUTHORIZATION));
            if (user != null && (user.getRoles().contains("SUPER_ADMIN") || !user.getRoles().contains("FORUM_ADMIN"))) {

                TargetPopulation t = targetPopulationRepository.findById(targetPopulation).orElse(null);
                if (t == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("the population does not exist");
                }
                Ad c = repo.findById(id).orElse(null);
                if (c == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("the advrtisement does not exist");
                } else {
                    if (channel != null) {
                        c.setChannel(channel);
                    }
                    if (cost > 0) {
                        c.setCost(cost);
                    }
                    if (startDate != null) {
                        c.setStartDate(startDate);
                    }
                    if (endDate != null) {
                        c.setEndDate(endDate);
                    }
                    if (initialTarget > 0) {
                        c.setInitialTargetNumberOfViews(initialTarget);
                    }
                    if (name != null) {
                        c.setName(name);
                    }
                    c.setTypeOfAudience(t);
                    List<AdMedia> mediaList = c.getMedia();
                    if (mediaList != null && images != null) {
                        Collections.sort(images, new MultipartFileSizeComparator());
                        Collections.sort(mediaList, new AdMediaComparator());
                        for (AdMedia m : new ArrayList<>(mediaList)) {
                            for (MultipartFile f : new ArrayList<>(images)) {
                                if (m.getContent().length == f.getBytes().length) {
                                    images.remove(f);
                                    mediaList.remove(m);
                                    break;
                                }
                            }
                        }
                        for (AdMedia m : mediaList) {
                            mediaService.delete(m.getId());
                        }
                    }
                    if (images != null) {
                        if (!images.isEmpty()) {
                            for (MultipartFile image : images) {
                                if (!image.isEmpty()) {
                                    try {
                                        AdMedia savedImageData = mediaService.save(image, c);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }
                    }
                    repo.save(c);
                    return ResponseEntity.ok().body(c);
                }

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have the right to post an ad");
            }

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have to login");
    }

    @Override
    @CacheEvict("ad")
    public ResponseEntity<String> deleteAd(Long ad, HttpServletRequest authentication) throws JsonProcessingException {
        if (authentication != null && authentication.getHeader(HttpHeaders.AUTHORIZATION) != null) {
            UserDTO user = userService.getUserDetailsFromToken(
                    authentication.getHeader(HttpHeaders.AUTHORIZATION));
            if (user != null && (user.getRoles().contains("SUPER_ADMIN") || !user.getRoles().contains("FORUM_ADMIN"))) {
                Ad p = repo.findById(Long.valueOf(ad)).orElse(null);
                if (p != null) {
                    repo.delete(p);
                    return ResponseEntity.ok().body("Ad deleted!");
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ad Not Found");

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have the right to post an ad");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You have to login!");
    }

    @Override
    @Cacheable("ad")
    public Ad getSingleAd(Long id, HttpServletRequest request) {
        Ad ad = repo.findById(id).orElse(null);
        if (ad!=null){
            incrementViews(ad, request);
            return ad;
        }
        return null;
    }

    @Override
    @Cacheable("ad")
    public List<Ad> getAllLazy(int page, int size, HttpServletRequest request) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());
        List<Ad> list = pagerepo.findAll(pageable).getContent();
        list.stream().map((Ad ad) -> {
            incrementViews(ad, request);
            return repo.save(ad);
        }).collect(Collectors.toList());;
        return list;
    }

    public void incrementViews(Ad ad, HttpServletRequest request) {
        if (request != null) {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                try {
                    Long user = (Long) userService
                            .getUserDetailsFromToken(authHeader)
                            .getId();
                    if (adViewsRepository.findByAdAndUser(ad, user) == null) {
                        adViewsRepository.save(
                                new AdViews(user, ad, LocalDateTime.now())
                        );
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
    }
}

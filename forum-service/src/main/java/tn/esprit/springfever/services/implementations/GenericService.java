package tn.esprit.springfever.services.implementations;

import org.springframework.http.HttpHeaders;
import tn.esprit.springfever.services.interfaces.IGenericService;

public class GenericService implements IGenericService {
    @Override
    public HttpHeaders createHeadersWithBearerToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}

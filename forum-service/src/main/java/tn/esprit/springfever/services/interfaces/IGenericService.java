package tn.esprit.springfever.services.interfaces;

import org.springframework.http.HttpHeaders;

public interface IGenericService {
    public HttpHeaders createHeadersWithBearerToken(String token);
}

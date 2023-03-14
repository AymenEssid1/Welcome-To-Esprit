package tn.esprit.springfever.configuration;
import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.auth.oauth2.UserCredentials;
import com.stripe.exception.oauth.OAuthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.validation.ValidationException;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class GoogleAdsConfig {
    @Value("${google.ads.developerToken}")
    private String developerToken;
    @Value("${google.ads.clientId}")
    private String clientId;
    @Value("${google.ads.clientSecret}")
    private String clientSecret;
    @Value("${google.ads.refreshToken}")
    private String refreshToken;
    @Bean
    public GoogleAdsClient getGoogleAdsClient() throws FileNotFoundException, OAuthException, IOException, ValidationException {
        return GoogleAdsClient.newBuilder().setCredentials(
                        UserCredentials.newBuilder()
                                .setClientId(clientId)
                                .setClientSecret(clientSecret)
                                .setRefreshToken(refreshToken)
                                .build())
                .setDeveloperToken(developerToken)
                .build();
    }
}
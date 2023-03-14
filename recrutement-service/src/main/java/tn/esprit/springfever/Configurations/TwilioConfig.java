package tn.esprit.springfever.Configurations;


import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class TwilioConfig {
    @Value("${twilio.account.sid}")
    private String ACCOUNT_SID;

    @Value("${twilio.auth.token}")
    private String AUTH_TOKEN;
    @Value("${twilio.number}")
    private String TWILIO_NUMBER;

    @Bean
    public void initTwilio() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public String getTwilioNumber() {
        return TWILIO_NUMBER;
    }

    @Bean
    public TwilioRestClient twilioRestClient() {
        return new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
    }




}

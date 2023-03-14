package tn.esprit.springfever.Services.Implementation;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SmsService {
    public void sendSmsvalide(String phoneNumber , String message  ) {
        final String ACCOUNT_SID = "AC6a3e6ce115b493a809c015ec86e294db";
        final String AUTH_TOKEN = "2aac4c47f9331d3019433665346d59bf";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message msg = Message.creator(new PhoneNumber("+216"+phoneNumber),new PhoneNumber("+15673456353"),(message)).create();

    }


}

package tn.esprit.springfever.Services.Implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;



    @Service
    public class StripeService {



        @Value("${stripe.secret.key}")
        private String stripeSecretKey;

        public Charge chargeCreditCard(String token, BigDecimal amount) throws StripeException {
            Stripe.apiKey = stripeSecretKey;
            System.out.println(stripeSecretKey);

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount.intValue());
            chargeParams.put("currency", "usd");
            chargeParams.put("source", token);


            return Charge.create(chargeParams);
        }
    }



package tn.esprit.springfever.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Services.Implementation.StripeService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/charge")
    public ResponseEntity<?> chargeCreditCard(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            BigDecimal amount = new BigDecimal(request.get("amount").toString());

            Charge charge = stripeService.chargeCreditCard(token, amount);


            return ResponseEntity.ok().body(charge);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    public String getStringFromBraces(String input) {
        int startIndex = input.indexOf("{");
        if (startIndex == -1) {
            // If there's no "{" in the input string, return an empty string
            return "";
        }
        int endIndex = input.lastIndexOf("}");
        if (endIndex == -1) {
            // If there's no "}" in the input string, return an empty string
            return "";
        }
        // Use Math.min to ensure that endIndex doesn't precede the startIndex
        endIndex = Math.max(endIndex, startIndex);
        return input.substring(startIndex, endIndex + 1);
    }

    public static String extractLinesContainingKeywords(String input) {
        StringBuilder result = new StringBuilder();
        String[] lines = input.split("\\r?\\n");
        for (String line : lines) {
            if (line.contains("status")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("brand")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("last4")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("exp_month")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("exp_year")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("amount")) {
                result.append(line).append("\n");
            }
        }
        for (String line : lines) {
            if (line.contains("receipt_url")) {
                result.append(line).append("\n");
            }
        }
        return result.toString();
    }

    @PostMapping("/chargeV2")
    public ResponseEntity<String> chargeCreditCardV2(@RequestParam String request,@RequestParam int amountt ) {
        try {
            String token = request;
            BigDecimal amount = BigDecimal.valueOf(amountt);

            Charge charge = stripeService.chargeCreditCard(token, amount);
            //System.out.println(ResponseEntity.ok().body(charge));
            String chargee=extractLinesContainingKeywords(getStringFromBraces(charge.toString()));

            return ResponseEntity.ok().body(chargee);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}

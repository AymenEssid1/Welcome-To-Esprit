package tn.esprit.springfever.configuration;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.springfever.Repositories.BanRepository;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.entities.Ban;
import tn.esprit.springfever.entities.RoleType;
import tn.esprit.springfever.entities.User;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



    @Service
    @EnableScheduling
    @Slf4j
    public class PaymentService {

        @Autowired
        private UserRepo userRepository;

        @Autowired
        private BanRepository banRepository;
        @Autowired
        private SMS_service sms_service;


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

        @Value("${stripe.secret.key}")
        private String stripeSecretKey;

        public Charge chargeCreditCard(String token, BigDecimal amount) throws StripeException {
            Stripe.apiKey = stripeSecretKey;
            System.out.println(stripeSecretKey);

            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", amount.intValue()*100);
            chargeParams.put("currency", "usd");
            chargeParams.put("source", token);


            return Charge.create(chargeParams);
        }


        //@Scheduled(cron = "0 0 0 * * 0")  //rusn every week
        //@Scheduled(cron = "0 0/3 * * * ?") // Runs every 3 minutes for testing purposes
       // @Scheduled(cron = "*/5 * * * * *")
        public void timeoutAccounts() {
            System.out.println("Running timeoutAccounts()...");
            List<User> users = userRepository.findAll();
            for (User user : users) {
                // Check if the user has the STUDENT role and their payment status is 0
                boolean isStudentWithUnpaidStatus = user.getRoles().stream()
                        .anyMatch(role -> role.getRolename() == RoleType.STUDENT)
                        && user.getPayment_status() == 0;

                // Check if the user was created more than 30 days ago
                boolean isCreatedMoreThan30DaysAgo = user.getCreationDate()
                        .plus(30, ChronoUnit.SECONDS)
                        .isBefore(LocalDateTime.now());

                System.out.printf("User %d: isStudentWithUnpaidStatus=%s, isCreatedMoreThan30DaysAgo=%s%n",
                        user.getUserid(), isStudentWithUnpaidStatus, isCreatedMoreThan30DaysAgo);

                if (isStudentWithUnpaidStatus && isCreatedMoreThan30DaysAgo) {
                    // Check if the user has an active ban
                    Ban activeBan = banRepository.findByUserAndExpiryTimeAfter(user, LocalDateTime.now());
                    if (activeBan == null) {
                        // Timeout the user's account by setting the expiry time to a value that indicates an indefinite timeout
                        Ban ban = new Ban();
                        ban.setUser(user);
                        ban.setExpiryTime(LocalDateTime.now().plusDays(9999));
                        // Save the ban object to the database
                        banRepository.save(ban);
                        System.out.printf("User %d timed out%n", user.getUserid());
                    } else {
                        System.out.printf("User %d already has an active ban%n", user.getUserid());
                        sms_service.sendSmsvalide(user.getPhoneNumber());

                    }
                }
            }
            System.out.println("Finished timeoutAccounts()");
        }



    }



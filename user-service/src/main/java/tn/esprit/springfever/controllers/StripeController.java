package tn.esprit.springfever.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.springfever.Repositories.BanRepository;
import tn.esprit.springfever.Repositories.UserRepo;
import tn.esprit.springfever.configuration.PaymentService;
import tn.esprit.springfever.entities.Ban;
import tn.esprit.springfever.entities.User;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    UserRepo userrepo;
    @Autowired
    private BanRepository banRepository;




    @PostMapping("/chargeV2")
    public ResponseEntity<String> chargeCreditCardV2(@RequestParam String request,@RequestParam int amountt ,@RequestParam String username ) {
        User u=userrepo.findByUsername(username).orElse(null);
        if (u==null){return ResponseEntity.badRequest().body("user not found");}
        if(u.getPayment_status()==1){return ResponseEntity.badRequest().body("already paid");}
        try {
            String token = request;
            BigDecimal amount = BigDecimal.valueOf(amountt);

            Charge charge = paymentService.chargeCreditCard(token, amount);
            //System.out.println(ResponseEntity.ok().body(charge));
            String chargee=paymentService.extractLinesContainingKeywords(paymentService.getStringFromBraces(charge.toString()));
            u.setPayment_status(1);

            userrepo.save(u);


            Ban b=banRepository.findBanByUser(u);
            if(b!=null)
            {banRepository.fasakh(b.getId());}

            return ResponseEntity.ok().body(chargee);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        }
    }
}

package com.auction.app.controller;

import com.auction.app.services.UserService;
import com.auction.app.services.payment.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.SetupIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"https://still-castle-19196.herokuapp.com","https://js.stripe.com"})
@RestController
@RequestMapping("/payment")
public class PaymentController {

    private StripeClient stripeClient;

    private final UserService userService;

    @Autowired
    PaymentController(StripeClient stripeClient, UserService userService) {
        this.stripeClient = stripeClient;
        this.userService = userService;
    }

    @PostMapping("/chargeCard")
    public ResponseEntity<Object> chargeCard(HttpServletRequest request) throws Exception {
        String token = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return new ResponseEntity<>(this.stripeClient.chargeCreditCard(token, amount).toJson(),HttpStatus.OK);

    }

    @PostMapping("/createCustomer")
    public ResponseEntity<Object> createCustomer(HttpServletRequest request) throws Exception {
        String email = request.getHeader("email");
        String token = request.getHeader("token");
        return new ResponseEntity<>(this.stripeClient.createCustomer(token, email).toJson(), HttpStatus.OK);
    }

    @PostMapping("/chargeCustomerCard")
    public Charge chargeCustomerCard(HttpServletRequest request) throws Exception {
        String customerId = request.getHeader("token");
        Double amount = Double.parseDouble(request.getHeader("amount"));
        return this.stripeClient.chargeCustomerCard(customerId, amount);
    }
    @GetMapping("/setupIntent")
    public ResponseEntity<Object> getSetupIntent() {
        Map<String, Object> params = new HashMap<>();
        // The default usage is off_session
        params.put("usage", "on_session");
        try {
            SetupIntent setupIntent = SetupIntent.create(params);
            return new ResponseEntity<>(setupIntent.toJson(), HttpStatus.OK);
        } catch (StripeException e) {
            e.printStackTrace();
            return null;
        }
    }

}
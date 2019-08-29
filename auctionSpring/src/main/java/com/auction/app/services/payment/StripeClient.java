package com.auction.app.services.payment;

import com.stripe.Stripe;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentSource;
import org.hibernate.dialect.SybaseDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeClient {

    private Logger logger = LoggerFactory.getLogger(StripeClient.class);
    @Autowired
    StripeClient() {
        Stripe.apiKey = "sk_test_umoiDJyxoJIfVJKgWyOtTKTO00hh2DBcCM";
    }

    public Charge chargeCreditCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", (int)(amount * 100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);

        try{
        return Charge.create(chargeParams);}
        catch(Exception e){
            logger.error("Erorr:"+e.toString());
            return null;
        }

    }

    public Customer createCustomer(String token, String email) throws Exception {
        /*Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("email", email);
        Map<String, Object> cardParams = new HashMap<String, Object>();
        cardParams.put("source", token);
        Stripe.apiKey = "sk_test_uTAM1qndRDbiJRowe8dJf6x9";
        Customer newCustomer = Customer.create(customerParams);
        newCustomer.getSources().create(cardParams);*/
        Stripe.apiKey = "sk_test_uTAM1qndRDbiJRowe8dJf6x9";
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "Customer for jenny.rosen@example.com");
        customerParams.put("email", email);
        customerParams.put("source",token);
        return Customer.create(customerParams);
    }

    private Customer getCustomer(String id) throws Exception {
        return Customer.retrieve(id);
    }
    private String getCustomerCardInfo(String id) throws Exception {
        return getCustomer(id).getDefaultSource();
    }

    public Charge chargeNewCard(String token, double amount) throws Exception {
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", Math.round(amount*100));
        chargeParams.put("currency", "USD");
        chargeParams.put("source", token);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }

    public Charge chargeCustomerCard(String customerId, double amount) throws Exception {
        String sourceCard = getCustomer(customerId).getDefaultSource();
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", Math.round(amount*100));
        chargeParams.put("currency", "USD");
        chargeParams.put("customer", customerId);
        chargeParams.put("source", sourceCard);
        Charge charge = Charge.create(chargeParams);
        return charge;
    }
}

package com.minor_project.minor_project.Controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @PostMapping("/upi")
    public ResponseEntity<Map<String, String>> createPaymentOrder(@RequestBody Map<String, Object> paymentRequest) {
        try {
            // Initialize Razorpay Client
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKey, razorpaySecret);

            // Parse and convert the amount to the smallest currency unit (paise)
            int amount = (int) paymentRequest.get("amount") * 100;

            // Create Order Request
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_123456");

            // Create Razorpay Order
            Order order = razorpayClient.orders.create(orderRequest);

            // Prepare Response
            Map<String, String> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("razorpayKey", razorpayKey);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage())); // Provide detailed error message
        }
    }
}


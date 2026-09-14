package com.smartremind.payment_service.configuration;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "razorpay")
@Getter
@Setter
public class RazorPayProperties {

    private String baseUrl;
    private String keyId;
    private String keySecret;





}

package com.smartremind.payment_service.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RazorPayClientConfiguration {


    private  final RazorPayProperties razorPayProperties ;

    public RazorPayClientConfiguration(RazorPayProperties razorPayProperties ){
        this.razorPayProperties = razorPayProperties;
    }


    @Bean
    public RestClient razorPayRestClient(){

        return RestClient.builder().
                baseUrl(razorPayProperties.getBaseUrl())
                .defaultHeaders(headers->headers
                        .setBasicAuth(
                                razorPayProperties.getKeyId()
                                ,razorPayProperties.getKeySecret()

                        )).build();

    }




}

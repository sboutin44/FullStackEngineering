package com.seb.email.routing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Base64;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;

@Service
public class EmailService {

        //mailgun
        private final String MAILGUN_USERNAME = "api";
        private final String MAILGUN_API_KEY = "key-13b5c4c1ac6418806aa1d263beab8456";
        private final String MAILGUN_TOKEN = Base64.getEncoder().encodeToString((MAILGUN_USERNAME + ":" + MAILGUN_API_KEY).getBytes());
        private final String MAILGUN_DOMAIN = "sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org";
        private final String MAILGUN_URL = "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";


        private final String SENDINBLUE_API_KEY = "BfkK0wPT1SODqFxN";
        private final String SENDINBLUE_DOMAIN = "";

        private RestTemplate restTemplate;

        public void SendMessageViaMAILGUN (MyEmail email)
        {
            restTemplate = new RestTemplate();
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
            restTemplateBuilder.basicAuthorization(MAILGUN_USERNAME,MAILGUN_API_KEY);
            restTemplate = restTemplateBuilder.build();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(httpHeaders.AUTHORIZATION,"Basic " + MAILGUN_TOKEN);

            //httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String,String> body = new LinkedMultiValueMap<String,String>();

            // Formatting the http body
            body.add("from",email.getFrom());
            body.add("to",email.getTo());
            body.add("subject",email.getSubject());
            body.add("text",email.getBody());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

            ResponseEntity<String> response = restTemplate.postForEntity(MAILGUN_URL, request, String.class);
            HttpStatus HttpStatus = response.getStatusCode();

        }


}

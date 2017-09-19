package com.seb.email.routing;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class EmailService {

    //mailgun
    private final String MAILGUN_USERNAME = "api";
    private final String MAILGUN_API_KEY = "key-13b5c4c1ac6418806aa1d263beab8456";
    private final String MAILGUN_TOKEN = Base64.getEncoder().encodeToString((MAILGUN_USERNAME + ":" + MAILGUN_API_KEY).getBytes());
    private final String MAILGUN_DOMAIN = "sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org";
    private final String MAILGUN_URL = "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";

    private final String SPARKPOST_API_KEY = "cf9a6bdc909a4c06fdd9f8cb7c1c2ae77397e63a";
    private final String SPARKPOST_TOKEN = Base64.getEncoder().encodeToString(SPARKPOST_API_KEY.getBytes());
    //private final String SPARKPOST_DOMAIN = ;
    private final String SPARKPOST_URL = "https://api.sparkpost.com/api/v1/transmissions";

    private RestTemplate restTemplate;

    public void SendMessageViaMAILGUN(MyEmail email) {
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(httpHeaders.AUTHORIZATION, "Basic " + MAILGUN_TOKEN);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();

        /* Formatting the http body */
        body.add("from", email.getFrom());
        body.add("to", email.getTo());
        body.add("subject", email.getSubject());
        body.add("text", email.getBody());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        /* Send the request to email service provider */
        ResponseEntity<String> response = restTemplate.postForEntity(MAILGUN_URL, request, String.class);
        HttpStatus HttpStatus = response.getStatusCode();
    }

    public void SendMessageViaSPARKPOST(MyEmail email) throws JSONException {
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(httpHeaders.AUTHORIZATION, SPARKPOST_TOKEN);

        /* For SPARKPOST the data are send through a Json file */
        JSONObject jsonFileToSend = new JSONObject();
        JSONObject options = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject recipients = new JSONObject();
        JSONObject contentValue = new JSONObject();

        options.put("sandbox", "true");
        contentValue.put("from", email.getFrom());
        contentValue.put("subject", email.getSubject());
        contentValue.put("text", email.getBody());
        content.put("content", contentValue);
        recipients.put("address", email.getTo());

        jsonFileToSend.put("options", options);
        jsonFileToSend.put("content", content);
        jsonFileToSend.put("recipients", new JSONArray(recipients));

        HttpEntity<JSONObject> request = new HttpEntity<>(jsonFileToSend, httpHeaders);

        // Send the request to email service provider
        ResponseEntity<String> response = restTemplate.postForEntity(SPARKPOST_URL, request, String.class);
        HttpStatus HttpStatus = response.getStatusCode();
    }
}

package com.seb.email.routing;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Base64;

@Service
public class EmailService {

    public enum Provider {
        MAILGUN,
        ELASTICEMAIL
    }

    /* mailgun */
    private final String MAILGUN_USERNAME = "api";
    private final String MAILGUN_API_KEY = "key-19bec70f90e27e8e5e87dbe94d16e6ff";
    private final String MAILGUN_TOKEN = Base64.getEncoder().encodeToString((MAILGUN_USERNAME + ":" + MAILGUN_API_KEY).getBytes());
    private final String MAILGUN_DOMAIN = "sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org";
    private final String MAILGUN_URL = "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";

    /* SPARKPOST */
    private final String SPARKPOST_API_KEY = "6994d7d60486eb9fa983018c5a6c6fb3375cbc5a";
    private final String SPARKPOST_TOKEN = Base64.getEncoder().encodeToString(SPARKPOST_API_KEY.getBytes());
    private final String SPARKPOST_URL = "https://api.sparkpost.com/api/v1/transmissions";

    /* ElasticEmail */
    private final String ELASTICEMAIL_USERNAME = null;
    private final String ELASTICEMAIL_URL = "https://api.elasticemail.com/v2/email/send";
    private final String ELASTICEMAIL_API_KEY = "6e9b7aab-4e2d-4c7a-be98-2562b2575340";
    private final String ELASTICEMAIL_TOKEN = Base64.getEncoder().encodeToString(ELASTICEMAIL_API_KEY.getBytes());


    private RestTemplate restTemplate;


    public HttpStatus Send(MyEmail email, Provider provider) throws UnsupportedEncodingException {
        HttpStatus httpStatus;

        if (provider == Provider.MAILGUN) {
            //CheckStatus(MAILGUN)
            httpStatus = SendMessageViaMAILGUN(email);
        } else {
            //ElasticEmail
            //CheckStatus(MAILGUN)
            httpStatus = SendMessageViaElasticEmail(email);
        }

        return httpStatus;
    }

    public HttpStatus SendMessageViaMAILGUN(MyEmail email) {
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
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus;
    }

    public HttpStatus SendMessageViaElasticEmail(MyEmail email) throws UnsupportedEncodingException {

        String isTransactional = "true";
        String encoding = "UTF-8";
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String content = "apikey=" + URLEncoder.encode(ELASTICEMAIL_API_KEY, encoding);
        content += "&from=" + URLEncoder.encode(email.getFrom(), encoding);
        content += "&fromName=" + URLEncoder.encode(email.getFromName(), encoding);
        content += "&subject=" + URLEncoder.encode(email.getSubject(), encoding);
        content += "&bodyHtml=" + URLEncoder.encode(email.getBody(), encoding);
        content += "&to=" + URLEncoder.encode(email.getTo(), encoding);
        content += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);

        HttpEntity<String> request = new HttpEntity<>(content, httpHeaders);

        // Send the request to email service provider
        ResponseEntity<String> response = restTemplate.postForEntity(ELASTICEMAIL_URL, request, String.class);
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus;
    }

    public String SendTest2(MyEmail email) throws IOException {

        String userName = "sboutin";
        String apiKey = ELASTICEMAIL_API_KEY;
        String from = "sboutin44@me.com";
        String fromName = "Sebastien";
        String subject = "Test";
        String body = "coucou sebastien";
        String to = "seb.boutin44@gmail.com";
        String isTransactional = "true";

        String encoding = "UTF-8";

        String data = "apikey=" + URLEncoder.encode(apiKey, encoding);
        data += "&from=" + URLEncoder.encode(from, encoding);
        data += "&fromName=" + URLEncoder.encode(fromName, encoding);
        data += "&subject=" + URLEncoder.encode(subject, encoding);
        data += "&bodyHtml=" + URLEncoder.encode(body, encoding);
        data += "&to=" + URLEncoder.encode(to, encoding);
        data += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);

        URL url = new URL(ELASTICEMAIL_URL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String result = rd.readLine();
        wr.close();
        rd.close();

        return result;
    }
}

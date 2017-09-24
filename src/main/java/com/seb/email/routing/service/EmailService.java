package com.seb.email.routing.service;

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

    /* mailgun config */
    private final String MAILGUN_USERNAME = "api";
    private final String MAILGUN_API_KEY = "key-" + Keys.MAILGUN_API_KEY_RAW;
    private final String MAILGUN_TOKEN =
            Base64.getEncoder().encodeToString((MAILGUN_USERNAME + ":" + MAILGUN_API_KEY).getBytes());
    private final String MAILGUN_URL =
            "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";

    /* ElasticEmail config */
    private final String ELASTICEMAIL_URL = "https://api.elasticemail.com/v2/email/send";
    private final String ELASTICEMAIL_API_KEY =
            Keys.ELASTICEMAIL_API_KEY_RAW.substring(0, 8) + "-" +
                    Keys.ELASTICEMAIL_API_KEY_RAW.substring(8,12) + "-" +
                    Keys.ELASTICEMAIL_API_KEY_RAW.substring(12,16) + "-" +
                    Keys.ELASTICEMAIL_API_KEY_RAW.substring(16,20) + "-" +
                    Keys.ELASTICEMAIL_API_KEY_RAW.substring(20);

    private RestTemplate restTemplate;




    private void switchProvider ( ){

    }

    public HttpStatus SendMessageViaMAILGUN(MyEmail email)  {
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

    public HttpStatus SendMessageViaMAILGUN_urlencoded(MyEmail email) throws UnsupportedEncodingException {
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String encoding = "UTF-8";
        String content = "key=" + URLEncoder.encode(Keys.MAILGUN_API_KEY_RAW, encoding);
        content += "&from=" + URLEncoder.encode(email.getFrom(), encoding);
        content += "&fromName=" + URLEncoder.encode(email.getFromName(), encoding);
        content += "&subject=" + URLEncoder.encode(email.getSubject(), encoding);
        content += "&bodyHtml=" + URLEncoder.encode(email.getBody(), encoding);
        content += "&to=" + URLEncoder.encode(email.getTo(), encoding);

        HttpEntity<String> request = new HttpEntity<>(content, httpHeaders);

        // Send the request to email service provide
        ResponseEntity<String> response = restTemplate.postForEntity(MAILGUN_URL, request, String.class);
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus;
    }

    public HttpStatus SendMessageViaElasticEmail(MyEmail email) throws UnsupportedEncodingException {
        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String isTransactional = "true";
        String encoding = "UTF-8";
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

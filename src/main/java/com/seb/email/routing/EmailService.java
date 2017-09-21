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

    /* mailgun */
    private final String MAILGUN_USERNAME = "api";
    private final String MAILGUN_API_KEY = "key-13b5c4c1ac6418806aa1d263beab8456";
    private final String MAILGUN_TOKEN = Base64.getEncoder().encodeToString((MAILGUN_USERNAME + ":" + MAILGUN_API_KEY).getBytes());
    private final String MAILGUN_DOMAIN = "sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org";
    private final String MAILGUN_URL = "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";

    /* SPARKPOST */
    private final String SPARKPOST_API_KEY = "cf9a6bdc909a4c06fdd9f8cb7c1c2ae77397e63a";
    private final String SPARKPOST_TOKEN = Base64.getEncoder().encodeToString(SPARKPOST_API_KEY.getBytes());
    private final String SPARKPOST_URL = "https://api.sparkpost.com/api/v1/transmissions";

    /* ElasticEmail */
    private final String ELASTICEMAIL_USERNAME = null;
    private final String ELASTICEMAIL_URL = "https://api.elasticemail.com/v2/email/send";
    private final String ELASTICEMAIL_API_KEY = "3555985e-6969-4491-be44-2873ba689636";
    private final String ELASTICEMAIL_TOKEN = Base64.getEncoder().encodeToString(ELASTICEMAIL_API_KEY.getBytes());


    private RestTemplate restTemplate;


    //public Send ( )

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

    public void SendMessageViaElasticEmail(MyEmail email) throws JSONException, UnsupportedEncodingException {

        String apiKey = ELASTICEMAIL_API_KEY;
        String from = email.getFrom();
        String fromName = email.getFromName();
        //String subject = email.;
        String body = "coucou sebastien";
        String to = "seb.boutin44@gmail.com";
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
        HttpStatus HttpStatus = response.getStatusCode();
    }

    public String SendTest(MyEmail email) throws IOException {

        String userName = "sboutin";
        String apiKey = ELASTICEMAIL_API_KEY;
        String from = "sboutin44@me.com";
        String fromName = "Sebastien";
        String subject = "Test";
        String body = "coucou sebastien";
        String to = "seb.boutin44@gmail.com";
        String isTransactional = "true";

        String encoding = "UTF-8";

        String content = "apikey=" + URLEncoder.encode(apiKey, encoding);
        content += "&from=" + URLEncoder.encode(from, encoding);
        content += "&fromName=" + URLEncoder.encode(fromName, encoding);
        content += "&subject=" + URLEncoder.encode(subject, encoding);
        content += "&bodyHtml=" + URLEncoder.encode(body, encoding);
        content += "&to=" + URLEncoder.encode(to, encoding);
        content += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);

        URL url = new URL(ELASTICEMAIL_URL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(content);
        wr.flush();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String result = rd.readLine();
        wr.close();
        rd.close();

        return result;
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

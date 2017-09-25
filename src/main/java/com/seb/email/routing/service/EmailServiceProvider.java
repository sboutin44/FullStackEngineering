package com.seb.email.routing.service;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

public class EmailServiceProvider {
    /* Class representing an email service provider, eg. Mailgun or Elasticemail.
     *
     * This class provides a global sending method and two "sub" sending methods for each provider
     * supported by this program. When an instance is created, a provider must be set among
     * an enumeration of all the supported providers (actually, just Mailgun and Elasticemail.
     * Since all email providers don't provide exactly the same POST request format in their API
     * documentations, a special method is proposed for each of them, and a global send method
     * is used outide the class.
     */

    // Supported providers
    public enum Providers {
        MAILGUN,
        ELASTICEMAIL
    }

    private Providers provider; // THIS instance provider
    private RestTemplate restTemplate;

    public String username;
    public String apiKey;
    public String token;
    public String url;
    public final String URL_MAILGUN =
            "https://api.mailgun.net/v3/sandbox572e948ff4c242c49dfb2c627fef1b23.mailgun.org/messages";
    public final String URL_ELASTICEMAIL = "" +
            "https://api.elasticemail.com/v2/email/send";

    // Constructor
    public EmailServiceProvider(Providers providerName) {
        /* Parameter mandatory for the send function */

        this.provider = providerName;
    }

    public HttpStatus send(MyEmail email) throws UnsupportedEncodingException {
        /* Global send function */

        HttpStatus status = null;

        if (this.provider == Providers.MAILGUN) {
            this.url = URL_MAILGUN;
            status = SendMessageViaMAILGUN(email);
        }

        if (this.provider == Providers.ELASTICEMAIL) {
            this.url = URL_ELASTICEMAIL;
            status = SendMessageViaElasticEmail(email);
        }

        return status;
    }

    public HttpStatus SendMessageViaElasticEmail(MyEmail email) throws UnsupportedEncodingException {
        /* Send a message through a POST request to ElasticEmail
         *
         * NOTE: The url is not a parameter, and can be set to a 'localhost' url by the appropriate setter
         * so that we can test this function without really sending emails to ElasticEmail servers.
         */

        apiKey = Keys.ELASTICEMAIL_API_KEY_RAW.substring(0, 8) + "-" +
                Keys.ELASTICEMAIL_API_KEY_RAW.substring(8, 12) + "-" +
                Keys.ELASTICEMAIL_API_KEY_RAW.substring(12, 16) + "-" +
                Keys.ELASTICEMAIL_API_KEY_RAW.substring(16, 20) + "-" +
                Keys.ELASTICEMAIL_API_KEY_RAW.substring(20);

        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        // Content-type
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Request body
        String isTransactional = "true";
        String encoding = "UTF-8";
        String content = "apikey=" + URLEncoder.encode(apiKey, encoding);
        content += "&from=" + URLEncoder.encode(email.getFrom(), encoding);
        content += "&fromName=" + URLEncoder.encode(email.getFromName(), encoding);
        content += "&subject=" + URLEncoder.encode(email.getSubject(), encoding);
        content += "&bodyHtml=" + URLEncoder.encode(email.getBody(), encoding);
        content += "&to=" + URLEncoder.encode(email.getTo(), encoding);
        content += "&isTransactional=" + URLEncoder.encode(isTransactional, encoding);

        HttpEntity<String> request = new HttpEntity<>(content, httpHeaders);

        // POST request
        ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus;
    }

    public HttpStatus SendMessageViaMAILGUN(MyEmail email) {
        /* Send a message through a POST request to ElasticEmail
         *
         * NOTE: The url is not a parameter, and can be set to a 'localhost' url by the appropriate setter
         * so that we can test this function without really sending emails to Mailgun servers.
         */

        username = "api";
        apiKey = "key-" + Keys.MAILGUN_API_KEY_RAW;
        token = Base64.getEncoder().encodeToString((username + ":" + apiKey).getBytes());
        //url = URL_MAILGUN;

        restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        // Authentication
        httpHeaders.add(httpHeaders.AUTHORIZATION, "Basic " + token);

        // Content-type
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Request body
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("from", email.getFromName() + "<" + email.getFrom() + ">");
        body.add("to", email.getTo());
        body.add("subject", email.getSubject());
        body.add("text", email.getBody());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, httpHeaders);

        // POST request
        ResponseEntity<String> response = restTemplate.postForEntity(this.url, request, String.class);
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus;
    }

    public Providers getProvider() {
        return provider;
    }

    public void setProvider(Providers provider) {
        this.provider = provider;
    }

    public void setApiKey(String api_key) {
        this.apiKey = api_key;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.seb.email.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.NotBlank;
import org.json.JSONException;
import org.json.JSONObject;

public class MyEmail {
    /* A class modelizing the content of a Json email for the exercice.
     *
     * An instance of MyEmail is created with the "email" method when a POST
     * request is sent to the "/email" endpoint. The following flow applies:
     *
     *      1)  A POST request is send to the /email endpoint
     *      2)  @RequestMapping maps the /email endpoint to the email method
     *      3)  @RequestBody maps the Json provided in the request into the "email" method
     *          parameter, and creates an instance of MyEmail. @Valid makes sure the Json is
     *          valid. During the creation of the MyEmail instance, the fields are parsed
     *          with those provided in the Json.
     *      3)  Validation of the email fields are done with annotations above them.
     */

    @NotBlank
    @org.hibernate.validator.constraints.Email      // Check if the address is really an address
    private String to;

    @NotBlank
    @JsonProperty("to_name")
    private String toName;

    @NotBlank
    @org.hibernate.validator.constraints.Email
    private String from;

    @NotBlank
    @JsonProperty("from_name")
    private String fromName;

    @NotBlank
    private String subject;

    @NotBlank
    @JsonDeserialize(using = HtmlJsonDeserializer.class) // Parse the html body in a plaintext body
    private String body;

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToName() {
        return toName;
    }

    public String getFrom() {
        return from;
    }

    public String getFromName() {
        return fromName;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

}

package com.seb.email.routing;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.Valid;

public class MyEmail {

    @NotBlank
    @Email
    private String to;

    @NotBlank
    @JsonProperty("to_name")
    private String toName;

    @NotBlank
    @Email
    private String from;

    @NotBlank
    @JsonProperty("from_name")
    private String fromName;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    public String getTo() {
        return to;
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
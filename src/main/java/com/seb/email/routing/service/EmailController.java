package com.seb.email.routing.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
public class EmailController {
    /* Class handling POST requests.
     *
     */

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public HttpStatus email(@RequestBody @Valid MyEmail myEmail) throws UnsupportedEncodingException {
        /* Maps a JSON file to an instance myEmail.
         *
         * This method acts, in the scope of this exercise, as our "main" method: sending a POST request
         * to the </email> endpoint will map the http request body to an instance of MyMail, given as a parameter.
         * With this email, and given an initial enumeration of email service providers (in this scope Mailgun and
         * Elasticemail), this method tries to send the email to all the providers in the order of the declared
         * enumeration. If the provider sends back a server error, the loop iterates on the next provider.
         *
         * Note:
         * The @RequestBody annotation maps the body of the POST request to the MyEmail class.
         * The @Valid annotation validates the good format of the Json file provided in the request.
         */

        HttpStatus httpStatus = null;

        for (EmailServiceProvider.Providers aProvider : EmailServiceProvider.Providers.values()) {
            EmailServiceProvider provider = new EmailServiceProvider(aProvider);
            httpStatus = provider.send(myEmail);

            if ('5' == httpStatus.toString().charAt(0)) { // Check if we have a 5xx Server Error
                continue; // We try the next email provider in our enumeration
            } else {
                break; // The error is not on the server side, thus the error is sent back to the client.
            }
        }

        //Send the statuto the HTTP Client
        return httpStatus;
    }
}

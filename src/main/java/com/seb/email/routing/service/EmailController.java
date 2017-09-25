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
        /* Map a JSON file sent to the </email> endpoint with the Email instance myEmail.
         *
         */
        HttpStatus httpStatus = null;

        for (EmailServiceProvider.Providers aProvider : EmailServiceProvider.Providers.values()) {
            EmailServiceProvider provider = new EmailServiceProvider(aProvider);
            httpStatus = provider.send(myEmail);

            if ('5' == httpStatus.toString().charAt(0)) { // Check if we have a 5xx Server Error
                continue; // We try the next email provider in our enumeration
            } else {
                break;
            }
        }

        //Send the statuto the HTTP Client
        return httpStatus;
    }
}

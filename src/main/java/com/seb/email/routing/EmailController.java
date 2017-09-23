package com.seb.email.routing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
public class EmailController {

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public HttpStatus email(@RequestBody @Valid MyEmail myEmail) throws UnsupportedEncodingException {
        /* Map a JSON file sent to the </email> endpoint with the Email instance myEmail.
         *
         */


        EmailServiceProvider provider = new EmailServiceProvider(EmailServiceProvider.Providers.MAILGUN);
        HttpStatus status = provider.send(myEmail);

        //Send the response to the HTTP Client
        return status;
    }
}

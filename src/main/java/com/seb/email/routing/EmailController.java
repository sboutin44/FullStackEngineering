package com.seb.email.routing;

import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.IOException;

@RestController
public class EmailController {

    @RequestMapping(value = "/myEmail", method = RequestMethod.POST)
    public MyEmail email(@RequestBody @Valid MyEmail myEmail) throws JSONException, IOException {

        EmailService emailService = new EmailService();
        EmailService.Provider emailServiceProvider = EmailService.Provider.ELASTICEMAIL;
        emailService.Send( myEmail, emailServiceProvider );

        return myEmail; //Send the response to the HTTP Client
    }
}

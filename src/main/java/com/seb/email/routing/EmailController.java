package com.seb.email.routing;

import org.hibernate.validator.constraints.NotBlank;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class EmailController {
    @RequestMapping(value = "/myEmail", method = RequestMethod.POST)
    public MyEmail email(@RequestBody @Valid MyEmail myEmail) throws JSONException {
        EmailService emailService = new EmailService();

        //emailService.SendMessageViaMAILGUN(myEmail);
        emailService.SendMessageViaSPARKPOST(myEmail);


        return myEmail; //Send the response to the HTTP Client
    }
}

package com.seb.email.routing;

import org.hibernate.validator.constraints.NotBlank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class EmailController {

    @RequestMapping(value = "/myEmail", method = RequestMethod.POST)
    public MyEmail email(@RequestBody @Valid MyEmail myEmail) {
        // html parsing
        Document doc = Jsoup.parse(myEmail.getBody());

        return myEmail; //Send the response to the HTTP Client
    }
}

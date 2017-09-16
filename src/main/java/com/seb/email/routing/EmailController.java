package com.seb.email.routing;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class EmailController {

    @RequestMapping(value = "/myEmail", method = RequestMethod.POST)
    public MyEmail email(@RequestBody MyEmail myEmail) {
        return myEmail;
    }
}

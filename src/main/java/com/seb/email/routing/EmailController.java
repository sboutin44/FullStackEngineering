package com.seb.email.routing;

import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
public class EmailController {

    @RequestMapping(value = "/myEmail", method = RequestMethod.POST)
    public MyEmail email(@RequestBody @Valid MyEmail myEmail) throws JSONException, IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        EmailService emailService = new EmailService();
        EmailService.Provider emailServiceProvider = EmailService.Provider.ELASTICEMAIL;

        emailService.Send( myEmail, emailServiceProvider );

        //Send the response to the HTTP Client
        return myEmail;
    }
}

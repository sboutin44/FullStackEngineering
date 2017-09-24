package com.seb.email.routing.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {"/fakeservice"})
public class FakeService {

    @GetMapping(value = {""})
    public HttpStatus fakeservice() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("fakeservice");
        HttpStatus status = HttpStatus.OK;

        return status;
    }
}
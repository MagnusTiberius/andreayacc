package com.magnustiberius.andrea.yacc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {
   @RequestMapping(value = "/index")
   public String index() {
      return "index";
   }
}
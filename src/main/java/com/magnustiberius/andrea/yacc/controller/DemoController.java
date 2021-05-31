package com.magnustiberius.andrea.yacc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.magnustiberius.andrea.yacc.dao.User;

@Controller
public class DemoController {
   
	@RequestMapping(value = "/index")
   public String index() {
      return "index";
   }
   
   @RequestMapping(value="/save", method=RequestMethod.POST)    
   public ModelAndView save(@ModelAttribute User user)  
   {    
   ModelAndView modelAndView = new ModelAndView();    
   modelAndView.setViewName("user-data");        
   modelAndView.addObject("user", user);      
   return modelAndView;    
   }    
}
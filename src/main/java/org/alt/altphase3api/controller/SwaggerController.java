package org.alt.altphase3api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerController {
  @GetMapping({"/api/docs", "/docs", "/swagger-ui"})
  public String redirectToSwagger() {
    return "redirect:/swagger-ui.html";
  }
}

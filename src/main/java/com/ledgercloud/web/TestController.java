package com.ledgercloud.web;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/error")
    public String test(){
        throw new com.ledgercloud.shared.exceptions.BusinessException("BAD_REQUEST", "Error de prueba");
    }
}

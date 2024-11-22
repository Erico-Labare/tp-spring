package fr.diginamic.hello.restControleurs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloRestControleur {
    @GetMapping
    public String direHello() {
        return "Hello";
    }
}

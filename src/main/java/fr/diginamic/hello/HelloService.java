package fr.diginamic.hello;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String salutations() {
        return "Je suis la classe de service et je vous dis Bonjour";
    }
}

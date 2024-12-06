package fr.diginamic.hello.viewController;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping
    public String getIndex(Model model, Authentication authentication) {
        model.addAttribute("authentication", authentication);
        return "index";
    }
}

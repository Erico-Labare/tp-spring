package fr.diginamic.hello.services;

import fr.diginamic.hello.entities.UserAccount;
import fr.diginamic.hello.repository.UserAccountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userAccountRepository.count() == 0) {
            UserAccount user = new UserAccount(
                    "user",
                    passwordEncoder.encode("password"),
                    "ROLE_USER"
            );
            UserAccount admin = new UserAccount(
                    "admin",
                    passwordEncoder.encode("password"),
                    "ROLE_ADMIN"
            );

            userAccountRepository.save(user);
            userAccountRepository.save(admin);
        }
        }
}

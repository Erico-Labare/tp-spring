package fr.diginamic.hello.mapper;

import fr.diginamic.hello.entities.UserAccount;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserAccountMapper {
    public static UserDetails toUserDetails (UserAccount userAccount) {
        return User.builder().username(userAccount.getUsername()).password(userAccount.getPassword()).authorities(userAccount.getAuthorities()).build();
    }
}

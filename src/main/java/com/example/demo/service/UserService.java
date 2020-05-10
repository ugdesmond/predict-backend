

package com.example.demo.service;
import com.example.demo.jwt.JwtService;
import com.example.demo.model.User;
import com.example.demo.repository.UserLogic;
import com.example.demo.support.DateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class UserService {


    private JwtService jwtService;
    private DateGenerator dateGenerator;
    private UserLogic userLogic;

    @Autowired
    public UserService(JwtService jwtService, DateGenerator dateGenerator, UserLogic userLogic
    ) {

        this.jwtService = jwtService;
        this.dateGenerator = dateGenerator;
        this.userLogic = userLogic;

    }


    public User isLoginValid(String username, String pass) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(pass)) {
            return null;
        }
        List<User> userList = userLogic.getByColumnName("username", username);
        if (userList == null || userList.isEmpty()) {
            return null;
        }
        User u = userList.get(0);
        if (!u.getPassword().equals(BCrypt.hashpw(pass, u.getSalt()))) {
            return null;
        }

        return u;
    }

    public User createUserToken(String username, String secret) {
        User u = userLogic.getByColumnName("username", username).get(0);
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("user",u);
        String token = jwtService.createToken(u.getUsername(),userMap,secret, dateGenerator.getExpirationDate());
        u.setToken(token);
        return u;
    }

    public User validateUser(String token, String secret) {
        return jwtService.isValid(token,secret);
    }
}

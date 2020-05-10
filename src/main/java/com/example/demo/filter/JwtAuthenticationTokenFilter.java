package com.example.demo.filter;


import com.example.demo.Secfactory.UsernamePasswordAuthenticationTokenFactory;
import com.example.demo.model.User;
import com.example.demo.repository.UserLogic;
import com.example.demo.security.SecurityAppContext;
import com.example.demo.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String SERVER_KEY = "Server-Key";
    private static final String UTF_8 = "UTF-8";
    //Begin index for bear. bc index of bearer is 7
    private static final int BEGIN_INDEX = 7;
    private final Logger logger = Logger.getLogger(this.getClass());


    private UserService userService;

    private UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory;

    private SecurityAppContext securityAppContext;
    private UserLogic userLogic;


    @Autowired
    public JwtAuthenticationTokenFilter(UserService userService, SecurityAppContext securityAppContext, UsernamePasswordAuthenticationTokenFactory usernamePasswordAuthenticationTokenFactory, UserLogic userLogic) {
        this.securityAppContext = securityAppContext;
        this.userService = userService;
        this.usernamePasswordAuthenticationTokenFactory = usernamePasswordAuthenticationTokenFactory;
        this.userLogic = userLogic;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(AUTHORIZATION);
        String serverKey = request.getHeader(SERVER_KEY);
        SecurityContext context = securityAppContext.getContext();
        if (authToken != null) {
            try {
                authToken = new String(authToken.substring(BEGIN_INDEX).getBytes(), UTF_8);

                if (context.getAuthentication() == null) {
                    logger.info("Checking authentication for token " + authToken);
                    User u = userService.validateUser(authToken, request.getRemoteAddr());
                    if (u != null) {
                        Authentication authentication = usernamePasswordAuthenticationTokenFactory.create(u);
                        context.setAuthentication(authentication);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                logger.error(e.getMessage());
            }

        }
        if (serverKey != null) {
            List<User> userDetail = userLogic.getByColumnName("password", serverKey);
            if (!userDetail.isEmpty()) {
                Authentication authentication = usernamePasswordAuthenticationTokenFactory.create(userDetail.get(0));
                context.setAuthentication(authentication);
            }

        }
        chain.doFilter(request, response);
    }

}
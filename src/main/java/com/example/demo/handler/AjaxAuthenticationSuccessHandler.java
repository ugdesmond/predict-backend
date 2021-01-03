
package com.example.demo.handler;


import com.example.demo.controller.MessageResponse;
import com.example.demo.controller.UserController;
import com.example.demo.model.User;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.support.DateGenerator;
import com.example.demo.utils.Utility;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    final HeaderHandler headerHandler;
    final UserService userService;
    private final Utility utility;
    private final DateGenerator dateGenerator;
    Gson gson = new Gson();
    private Logger logger = Logger.getLogger(UserController.class);

    public AjaxAuthenticationSuccessHandler(HeaderHandler headerHandler, UserService userService, Utility utility, DateGenerator dateGenerator) {
        this.headerHandler = headerHandler;
        this.userService = userService;
        this.utility = utility;
        this.dateGenerator = dateGenerator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = userService.createUserToken(authentication.getName(), request.getRemoteAddr());
        UserDTO userDTO = utility.convertToDto(user, UserDTO.class);
        userDTO.setExpiresIn(dateGenerator.getExpirationDate());
        MessageResponse<UserDTO> messageResponse = new MessageResponse<>();
        messageResponse.setStatus(HttpStatus.OK.value());
        messageResponse.setMessage("Logged in successfully");
        messageResponse.setData(userDTO);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(gson.toJson(messageResponse));
        response.setStatus(HttpServletResponse.SC_OK);
        headerHandler.process(request, response);
    }

}

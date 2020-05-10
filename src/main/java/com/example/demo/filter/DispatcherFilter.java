package com.example.demo.filter;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@Component
public class DispatcherFilter extends OncePerRequestFilter {

    private final Logger logger = Logger.getLogger(this.getClass());
    // add the values you want to redirect for
    private Pattern pattern = Pattern.compile("/mangersickness/*|/managersymptom/*|/sicknesssymptom*|/login*");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (this.pattern.matcher(request.getRequestURI()).matches()) {
            RequestDispatcher rd = request.getRequestDispatcher("/");
            rd.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}
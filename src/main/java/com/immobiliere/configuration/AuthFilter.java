package com.immobiliere.configuration;

import java.io.IOException;

import com.immobiliere.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        User user = (User) req.getSession().getAttribute("user");
        if (user == null && !req.getRequestURI().endsWith("/login")) {
            res.sendRedirect("/login");
        } else {
            chain.doFilter(request, response);
        }
    }

	
}


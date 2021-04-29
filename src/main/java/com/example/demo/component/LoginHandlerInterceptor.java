package com.example.demo.component;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        Object user = request.getSession().getAttribute("loginUser");
        if(user == null){
            request.setAttribute("msg", "Permission denied");
            request.getRequestDispatcher("/login.html").forward(request, response);
            return false;
        }
        else{
            return true;
        }
    }
}

package ua.datalink.jms.server.controller;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor extends HandlerInterceptorAdapter {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        logger.debug("Adding user name after controller " + handler.toString());
        Object  uncastUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if((uncastUser instanceof User) && (modelAndView != null)){
            modelAndView.getModelMap().addAttribute("user", ((User)uncastUser).getUsername());
        }
    }
}

package ua.datalink.jms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ua.datalink.jms.server.service.JMSService;
import ua.datalink.jms.server.service.ProcessingService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    ProcessingService processingService;
    @Autowired
    JMSService jmsService;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        modelAndView.addObject("status", jmsService.getStatus());
        modelAndView.addObject("get", processingService.getGetRequests());
        modelAndView.addObject("get_all", processingService.getGetAllRequests());
        modelAndView.addObject("put", processingService.getPutRequests());
        modelAndView.addObject("delete", processingService.getDeleteRequests());
    }
}

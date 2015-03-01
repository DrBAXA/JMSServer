package ua.datalink.jms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.datalink.jms.server.service.JMSService;

@Controller
public class ServerController {

    @Autowired
    private JMSService server;

    @RequestMapping("/")
    public String hello(){
        return "index";
    }

    @RequestMapping("/start")
    public String start(){
        server.start();
        return "redirect:";
    }

    @RequestMapping("/stop")
    public String stop(){
        server.stop();
        return "redirect:";
    }
}

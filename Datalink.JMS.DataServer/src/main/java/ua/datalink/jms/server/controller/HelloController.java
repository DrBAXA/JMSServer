package ua.datalink.jms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.datalink.jms.server.service.JMSService;

/**
 *
 */
@Controller
public class HelloController {

    @Autowired
    JMSService server;

    @RequestMapping("/")
    public String hello(ModelMap modelMap){
        modelMap.addAttribute("status", "false");
        return "hello";
    }

    @RequestMapping("/start")
    public String start(ModelMap modelMap){
        modelMap.addAttribute("status", "true");
        server.start();
        return "hello";
    }

    @RequestMapping("/stop")
    public String stop(ModelMap modelMap){
        modelMap.addAttribute("status", "false");
        server.stop();
        return "hello";
    }
}

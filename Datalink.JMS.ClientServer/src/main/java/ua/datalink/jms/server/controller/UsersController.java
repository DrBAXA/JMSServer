package ua.datalink.jms.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.datalink.jms.server.entity.User;
import ua.datalink.jms.server.service.ProcessingService;

/**
 *
 */
@Controller
@RequestMapping("/")
public class UsersController {

    @Autowired
    ProcessingService processingService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap modelMap){
        modelMap.addAttribute("user", new User());
        return "index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAll(ModelMap modelMap){
        try {
            modelMap.addAttribute("user", new User());
            modelMap.addAttribute("users", processingService.getAll());
        } catch (Exception e) {
            modelMap.addAttribute("result", e.getMessage());
            modelMap.addAttribute("resultStatus", "red");
        }
        return "index";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public String get(@PathVariable("id") int id,
                      ModelMap modelMap){
        try {
            modelMap.addAttribute("user", new User());
            modelMap.addAttribute("requestedUser", processingService.get(id));
        } catch (Exception e) {
            modelMap.addAttribute("result", e.getMessage());
            modelMap.addAttribute("resultStatus", "red");
        }
        return "index";
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public String add(User user,
                      ModelMap modelMap){
        try {
            modelMap.addAttribute("user", new User());
            modelMap.addAttribute("result", processingService.put(user));
            modelMap.addAttribute("resultStatus", "green");
        } catch (Exception e) {
            modelMap.addAttribute("result", e.getMessage());
            modelMap.addAttribute("resultStatus", "red");
        }
        return "index";
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public org.springframework.http.ResponseEntity<String> delete(@PathVariable("id") int id){
        try {
            return new org.springframework.http.ResponseEntity<String>(processingService.delete(id), HttpStatus.OK);
        } catch (Exception e) {
            return new org.springframework.http.ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

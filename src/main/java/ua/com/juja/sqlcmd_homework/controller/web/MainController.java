package ua.com.juja.sqlcmd_homework.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Sims on 17/12/2015.
 */

@Controller
public class MainController {
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }
}

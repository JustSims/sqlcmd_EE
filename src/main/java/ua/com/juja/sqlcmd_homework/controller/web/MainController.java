package ua.com.juja.sqlcmd_homework.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sims on 17/12/2015.
 */

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main() {
        return "redirect:menu";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }

    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connect(HttpSession session, Model model) {
        String page = (String) session.getAttribute("from-page");
        session.removeAttribute("from-page");
        model.addAttribute("connection", new Connection(page));
        if (getManager(session) == null) {
            return "connect";
        } else {
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connecting(@ModelAttribute("connection")Connection connection,
                             HttpSession session, Model model) {

        try {
            DatabaseManager manager = service.connect(connection.getDbName(),
                    connection.getUserName(),
                    connection.getPassword());
            session.setAttribute("db_manager", manager);
            return "redirect:" + connection.getFromPage();
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(Model model) {
        model.addAttribute("items", service.commandsList());
        return "menu";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null){
            session.setAttribute("from-page", "/list");
            return "redirect:connect";
        }
        try {
            model.addAttribute("tables", manager.getTableData());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "list";
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public String find(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null){
            session.setAttribute("from-page", "/find");
            return "redirect:connect";
        }
        return "tableName";
    }

    @RequestMapping(value = "/find", params = { "tableName" },
            method = RequestMethod.POST)
    public String finding(Model model, @RequestParam(value = "tableName") String tableName, HttpSession session){
        DatabaseManager manager = getManager(session);

        List<String> tableData = null;
        try {
            tableData = manager.find(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("table", getLists(tableData));
        return "find";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(Model model, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        return "clear";
    }

    @RequestMapping(value = "/clear", params = { "tableName"}, method = RequestMethod.POST)
    public String clearing(Model model, @RequestParam(value = "tableName") String tableName, HttpSession session){
        DatabaseManager manager = getManager(session);
        try {
            manager.clear(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        model.addAttribute("actionURL", "addRecord");
        return "tableName";
    }

    @RequestMapping(value = "/addRecord", params = { "tableName"}, method = RequestMethod.POST)
    public String addingRecord(Model model, @RequestParam(value = "tableName") String tableName, HttpSession session){
        DatabaseManager manager = getManager(session);
        try {
            model.addAttribute("columnCount", getColumnCount(manager, tableName));
            model.addAttribute("tableName", tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "create";
    }

    @RequestMapping(value = "/create", params = { "tableName" }, method = RequestMethod.POST)
    public String creating(HttpSession session, @RequestParam(value = "tableName") String tableName,
                           HttpServletRequest req) {

        try {
            DatabaseManager manager = getManager(session);
            Map<String, Object> inputData = new HashMap<>();
            for (int index = 1; index <= getColumnCount(manager, tableName); index++) {
                inputData.put(req.getParameter("columnName" + index),
                        req.getParameter("columnValue" + index));
                manager.create(tableName, inputData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @RequestMapping(value = "/deleteRecord", method = RequestMethod.GET)
    public String delete(HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        return "deleteRecord";
    }

    @RequestMapping(value = "/deleteRecord", method = RequestMethod.POST)
    public String deleting(Model model,
                           @RequestParam(value = "tableName") String tableName,
                           @RequestParam(value = "keyName") String keyName,
                           @RequestParam(value = "keyValue") String keyValue,
                           HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        try {
            manager.deleteRecord(tableName, keyName, keyValue);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

    private List<List<String>> getLists(List<String> tableData) {
        List<List<String>> table = new ArrayList<>(tableData.size() - 1);
        int columnCount = Integer.parseInt(tableData.get(0));
        for (int current = 1; current < tableData.size(); ) {
            List<String> row = new ArrayList<>(columnCount);
            for (int rowIndex = 0; rowIndex < columnCount; rowIndex++) {
                row.add(tableData.get(current++));
            }
            table.add(row);
        }
        return table;
    }

    private int getColumnCount(DatabaseManager manager, String tableName) throws Exception {
        return Integer.parseInt(manager.find(tableName).get(0));
    }

}

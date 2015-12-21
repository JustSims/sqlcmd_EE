package ua.com.juja.sqlcmd_homework.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public String connect(HttpSession session) {
        if (getManager(session) == null) {
            return "connect";
        } else {
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connecting(HttpServletRequest req, HttpSession session) {
        DatabaseManager manager;
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        try {
            manager = service.connect(databaseName, userName, password);
            session.setAttribute("db_manager", manager);
            return "redirect:menu";
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(HttpServletRequest req) {
        req.setAttribute("items", service.commandsList());
        return "menu";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest req, HttpSession session) {
        DatabaseManager manager = getManager(session);
        if (manager == null){
            return "redirect:connect";
        }
        try {
            req.setAttribute("tables", manager.getTableData());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "list";
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
     public String find(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        return "tableName";
    }

    @RequestMapping(value = "/find", method = RequestMethod.POST)
    public String finding(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);

        String tableName = req.getParameter("tableName");
        List<String> tableData = null;
        try {
            tableData = manager.find(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        req.setAttribute("table", getLists(tableData));
        return "find";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        return "clear";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public String clearing(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);
        String tableName = req.getParameter("tableName");
        try {
            manager.clear(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }


    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }
        req.setAttribute("actionURL", "addRecord");
        return "tableName";
    }

    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public String addingRecord(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);
        try {
            String tableName = req.getParameter("tableName");
            req.setAttribute("columnCount", getColumnCount(manager, tableName));
            req.setAttribute("tableName", tableName);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String creating(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);
        String tableName = req.getParameter("tableName");
        Map<String, Object> inputData = new HashMap<>();
        try {
            for (int index = 1; index <= getColumnCount(manager, tableName); index++) {
                inputData.put(req.getParameter("columnName" + index),
                        req.getParameter("columnValue" + index));
            }
            manager.create(tableName, inputData);
        } catch (SQLException e) {
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
    public String deleting(HttpServletRequest req, HttpSession session){
        DatabaseManager manager = getManager(session);

        if (manager == null){
            return "redirect:connect";
        }

        String tableName = req.getParameter("tableName");
        String keyName = req.getParameter("keyName");
        String keyValue = req.getParameter("keyValue");
        try {
            manager.deleteRecord(tableName, keyName, keyValue);
        } catch (SQLException e) {
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

    private int getColumnCount(DatabaseManager manager, String tableName) throws SQLException {
        return Integer.parseInt(manager.find(tableName).get(0));
    }

}

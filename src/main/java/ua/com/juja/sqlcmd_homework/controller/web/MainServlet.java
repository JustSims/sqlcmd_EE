package ua.com.juja.sqlcmd_homework.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.service.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sims on 03/11/2015.
 */
public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = getAction(req);
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        try {
        if (action.startsWith("/connect")) {
            if (manager == null) {
                jsp("connect", req, resp);
                return;
            } else {
                redirect("menu", resp);
            }
            return;
        }
        if (manager == null) {
            String url = "connect";
            redirect(url, resp);
            return;
        }
        if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.commandsList());
            jsp("menu", req, resp);
        } else if (action.startsWith("/help")) {
            jsp("help", req, resp);
        } else if (action.startsWith("/list")) {
            list(manager, req, resp);
        } else if (action.startsWith("/clear")) {
            jsp("clear", req, resp);
        } else if (action.startsWith("/create")) {
            req.setAttribute("actionURL", "addRecord");
            jsp("tableName", req, resp);
        } else if (action.startsWith("/table")) {
            jsp("createTable", req, resp);
        } else if (action.startsWith("/find")) {
            jsp("tableName", req, resp);
        } else if (action.startsWith("/delete")) {
            jsp("delete", req, resp);
        } else if (action.startsWith("/logout")) {
            jsp("logout", req, resp);
        } else {
            jsp("error", req, resp);
        }
        } catch (Exception e){
             error(req, resp, e);
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        try {
            if (action.startsWith("/connect")) {
                connect(req, resp);
            } else if (action.startsWith("/clear")) {
                clear(manager, req, resp);
            } else if (action.startsWith("/addRecord")) {
                prepare(req, manager);
                jsp("create", req, resp);
            } else if (action.startsWith("/find")) {
                find(manager, req, resp);
            } else if (action.startsWith("/create")) {
                create(manager, req, resp);
            } else if (action.startsWith("/delete")) {
                delete(manager, req, resp);
            }
        } catch (Exception e){
            error(req, resp, e);
        }

    }

    private void connect(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        DatabaseManager manager;
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        manager = service.connect(databaseName, userName, password);
        req.getSession().setAttribute("db_manager", manager);
        redirect("menu", resp);
        req.getRequestDispatcher("error.jsp").forward(req, resp);
    }

    private void delete(DatabaseManager manager, HttpServletRequest req,
                        HttpServletResponse resp) throws Exception {
        String tableName = req.getParameter("tableName");
        String keyName = req.getParameter("keyName");
        String keyValue = req.getParameter("keyValue");
            manager.deleteRecord(tableName, keyName, keyValue);
            jsp("success", req, resp);
    }


    private void create(DatabaseManager manager, HttpServletRequest req,
                        HttpServletResponse resp) throws Exception {
        String tableName = req.getParameter("tableName");
        Map<String, Object> inputData = new HashMap<>();
            for (int index = 1; index <= getColumnCount(manager, tableName); index++) {
                inputData.put(req.getParameter("columnName" + index),
                        req.getParameter("columnValue" + index));
            }
            manager.create(tableName, inputData);
            jsp("success", req, resp);
    }

    private void find(DatabaseManager manager, HttpServletRequest req,
                      HttpServletResponse resp) throws Exception {
        String tableName = req.getParameter("tableName");
        List<String> tableData = manager.find(tableName);
        req.setAttribute("table", getLists(tableData));
        jsp("find", req, resp);
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

    private void prepare(HttpServletRequest req,
                         DatabaseManager manager) throws Exception{
        String tableName = req.getParameter("tableName");
            req.setAttribute("columnCount", getColumnCount(manager, tableName));
            req.setAttribute("tableName", tableName);
    }

    private int getColumnCount(DatabaseManager manager, String tableName) throws SQLException {
        return Integer.parseInt(manager.find(tableName).get(0));
    }

    private void clear(DatabaseManager manager, HttpServletRequest req,
                       HttpServletResponse resp) throws Exception {
        String tableName = req.getParameter("tableName");
            manager.clear(tableName);
            jsp("success", req, resp);
        }

    private void list(DatabaseManager manager, HttpServletRequest req,
                      HttpServletResponse resp) throws Exception {
            Set<String> tableNames = manager.getTableData();
            req.setAttribute("tables", tableNames);
            jsp("list", req, resp);
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        req.setAttribute("message", e.getMessage());
        try {
            e.printStackTrace();
            jsp("error", req, resp);
        } catch (Exception e1) {
            e.printStackTrace();
        }
    }

    private void redirect(String url, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(resp.encodeRedirectURL(url));
    }

    private void jsp(String jsp, HttpServletRequest req,
                     HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(jsp + ".jsp").forward(req, resp);
    }

}

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");
        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
                return;
            } else {
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            }
            return;
        }
        if (manager == null){
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return;
        }

        if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.commandsList());
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        } else if (action.startsWith("/list")) {
            list(manager, req, resp);
        } else if (action.startsWith("/clear")) {
            req.getRequestDispatcher("clear.jsp").forward(req, resp);
        } else if (action.startsWith("/create")) {
            req.setAttribute("actionURL", "addRecord");
            req.getRequestDispatcher("tableName.jsp").forward(req, resp);
        } else if (action.equals("/table")) {
            req.getRequestDispatcher("createTable.jsp").forward(req, resp);
        } else if (action.startsWith("/find")) {
            req.getRequestDispatcher("tableName.jsp").forward(req, resp);
        } else if (action.startsWith("/delete")) {
            req.getRequestDispatcher("delete.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
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

        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                manager = service.connect(databaseName, userName, password);
                req.getSession().setAttribute("db_manager", manager);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.equals("/clear")) {
            clear(manager, req, resp);
        } else if (action.equals("/addRecord")) {
            String page = "create.jsp";
            forward(req, resp, manager, page);
        } else if (action.equals("/table")) {
            table(manager, req, resp);
        } else if (action.equals("/find")) {
            find(manager, req, resp);
        } else if (action.equals("/create")) {
            create(manager, req, resp);
        } else if (action.equals("/delete")) {
        delete(manager, req, resp);
        }
    }

    private void delete(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        String keyName = req.getParameter("keyName");
        String keyValue = req.getParameter("keyValue");
        try {
            service.deleteRecord(manager, tableName, keyName, keyValue);
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        } catch (ServletException | SQLException | IOException e) {
            error(req, resp, e);
        }
    }


    private void create(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        Map<String, Object> inputData = new HashMap<>();
        try {
            for (int index = 1; index <= getColumnCount(manager, tableName); index++) {
                inputData.put(req.getParameter("columnName" + index),
                        req.getParameter("columnValue" + index));
            }
            service.create(manager, tableName, inputData);
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        } catch (ServletException | SQLException | IOException e) {
            error(req, resp, e);
        }
    }

    private void find(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        try {
            List<String> tableData = service.find(manager, tableName);
            List<List<String>> table = new ArrayList<>(tableData.size() - 1);
            int columnCount = Integer.parseInt(tableData.get(0));
            for (int current = 1; current < tableData.size();) {
                List<String> row = new ArrayList<>(columnCount);
                for (int rowIndex = 0; rowIndex < columnCount; rowIndex++) {
                    row.add(tableData.get(current++));
                }
                table.add(row);
            }
            req.setAttribute("table", table);
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } catch (ServletException | SQLException | IOException e) {
            error(req, resp, e);
        }
    }

    private void table(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        String primaryKey = req.getParameter("primaryKey");

        Map<String, Object> columnParameters = new HashMap<>();
        for (int index = 1; index < columnCount; index++) {
            columnParameters.put(req.getParameter("columnName" + index),
                    req.getParameter("columnType" + index));
        }
        try {
            service.table(manager, tableName, primaryKey, columnParameters);
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        } catch (ServletException | SQLException | IOException e) {
            error(req, resp, e);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, DatabaseManager manager, String page) {
        String tableName = req.getParameter("tableName");
        try {
            req.setAttribute("columnCount", getColumnCount(manager, tableName));
            req.setAttribute("tableName", tableName);
            req.getRequestDispatcher(page).forward(req, resp);
        } catch (ServletException | IOException | SQLException e) {
            error(req, resp, e);
        }
    }

    private int getColumnCount(DatabaseManager manager, String tableName) throws SQLException {
        return Integer.parseInt(manager.getTableData(tableName).get(0));
    }

    private void clear(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        String tableName = req.getParameter("tableName");
        try {
            service.clear(manager, tableName);
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        } catch (ServletException | SQLException | IOException e) {
            error(req, resp, e);
        }
    }

    private void list(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        Set<String> tableNames = manager.getTableNames();
        req.setAttribute("tables", tableNames);
        try {
            req.getRequestDispatcher("list.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            error(req, resp, e);
        }
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        req.setAttribute("message", e.getMessage());
        try {
            e.printStackTrace();
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (ServletException | IOException e1) {
            e.printStackTrace();
        }
    }

}

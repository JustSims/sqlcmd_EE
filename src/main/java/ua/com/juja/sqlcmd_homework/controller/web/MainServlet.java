package ua.com.juja.sqlcmd_homework.controller.web;

import ua.com.juja.sqlcmd_homework.model.DatabaseManager;
import ua.com.juja.sqlcmd_homework.service.Service;
import ua.com.juja.sqlcmd_homework.service.ServiceImplemented;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sims on 03/11/2015.
 */
public class MainServlet extends HttpServlet {

    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImplemented();
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
        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("table", service.find(manager, tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);
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
        }

    }

    private void clear(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String tableName = req.getParameter("tableName");
            service.clear(manager, tableName);
            req.getRequestDispatcher("success.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void list(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) {
        Set<String> tableNames = manager.getTableNames();
        req.setAttribute("tables", tableNames);
        try {
            req.getRequestDispatcher("list.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            try {
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            } catch (ServletException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }

}
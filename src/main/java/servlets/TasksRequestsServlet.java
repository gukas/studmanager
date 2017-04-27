package servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dbservice.ContextListener;
import templater.PageGenerator;

public class TasksRequestsServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("tasks.html", null));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {

    }
}

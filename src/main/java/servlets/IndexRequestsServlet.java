package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import freemarker.ext.beans.HashAdapter;
import templater.PageGenerator;

public class IndexRequestsServlet extends HttpServlet{

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        // TODO вынести копипасту про получение списка студентов
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
        DataSource ds = getDs(request.getServletContext());
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT s.id, s.surname, s.name, s.birthday, g.nomer as `group`, s.mark FROM student s LEFT JOIN group_st g on g.id = s.group_id");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> student = new HashMap<String, Object>();
                student.put("id", rs.getString("id"));
                student.put("surname", rs.getString("surname"));
                student.put("name", rs.getString("name"));
                student.put("birthday", rs.getString("birthday"));
                student.put("group", rs.getString("group"));
                student.put("mark", rs.getString("mark"));
                students.add(student);
            }
            System.out.println("Get student!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pageVariables.put("students", students);

        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        String message = request.getParameter("message");

        response.setContentType("text/html;charset=utf-8");

        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        pageVariables.put("message", message == null ? "" : message);

        response.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }
}

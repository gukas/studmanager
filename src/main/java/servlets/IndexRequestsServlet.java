package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dbservice.ContextListener;
import templater.PageGenerator;


public class IndexRequestsServlet extends HttpServlet{

    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("students", students);

        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }
}

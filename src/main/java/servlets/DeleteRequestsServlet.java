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

import templater.PageGenerator;

public class DeleteRequestsServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();

        Integer id = Integer.parseInt(request.getParameter("id"));
        // TODO валидация полученных значений
        DataSource ds = getDs(request.getServletContext());
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT surname, name, birthday FROM student WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String surname = rs.getString("surname");
            String name = rs.getString("name");
            String birthday = rs.getString("birthday");
            pageVariables.put("surname", surname);
            pageVariables.put("name", name);
            pageVariables.put("birthday", birthday);
            pageVariables.put("id", id);
            System.out.println("Get student!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("delete.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();

        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        pageVariables.put("id", id);
        // TODO валидация полученных значений
        DataSource ds = getDs(request.getServletContext());
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM student WHERE id = ?");
            ps.setString(1, id);
            ps.execute();
            System.out.println("Delete student!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // TODO вынести копипасту про получение списка студентов
        List<Map<String, Object>> students = new ArrayList<Map<String, Object>>();
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

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));

    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }
}

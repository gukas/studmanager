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

public class EditRequestsServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        Integer id = Integer.parseInt(request.getParameter("id"));

        DataSource ds = getDs(request.getServletContext());
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT surname, name, sex, birthday, nationality, address, group_id FROM student WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            String surname = rs.getString("surname");
            String name = rs.getString("name");
            String sex = rs.getString("sex");
            String birthday = rs.getString("birthday");
            String nationality = rs.getString("nationality");
            String address = rs.getString("address");
            String group_id = rs.getString("group_id");
            pageVariables.put("surname", surname);
            pageVariables.put("name", name);
            pageVariables.put("sex", sex);
            pageVariables.put("birthday", birthday);
            pageVariables.put("nationality", nationality);
            pageVariables.put("address", address);
            pageVariables.put("group_id", group_id);
            pageVariables.put("id", id);
            System.out.println("Get student!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("edit.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        String message = request.getParameter("message");

        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        pageVariables.put("message", message == null ? "" : message);

        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");
        String nationality = request.getParameter("nationality");
        String address = request.getParameter("address");
        String group_id = request.getParameter("group_id");
        String id = request.getParameter("id");

        Map<String, String[]> params = request.getParameterMap();
        // TODO валидация полученных значений
        DataSource ds = getDs(request.getServletContext());
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE student SET surname = ?, name = ?, sex = ?, birthday = ?, nationality = ?, address = ?, group_id = ? "
                            + "WHERE id = ?");
            ps.setString(1, surname);
            ps.setString(2, name);
            ps.setString(3, sex);
            ps.setString(4, birthday);
            ps.setString(5, nationality);
            ps.setString(6, address);
            ps.setString(7, group_id);
            ps.setString(8, id);
            ps.execute();
            System.out.println("Update student!");
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

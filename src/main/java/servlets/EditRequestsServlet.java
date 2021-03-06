package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import dbservice.ContextListener;
import templater.PageGenerator;

import static dbservice.Validation.validateAddress;
import static dbservice.Validation.validateBirthday;
import static dbservice.Validation.validateGroupId;
import static dbservice.Validation.validateName;
import static dbservice.Validation.validateNationality;
import static dbservice.Validation.validateSex;
import static dbservice.Validation.validateSurname;


public class EditRequestsServlet extends HttpServlet{
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();

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
            String nationality =
                    rs.getString("nationality");
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
            HttpServletResponse response) throws ServletException, IOException
    {
        String surname = request.getParameter("surname");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String birthday = request.getParameter("birthday");
        String nationality = request.getParameter("nationality");
        String address = request.getParameter("address");
        String group_id = request.getParameter("group_id");
        String id = request.getParameter("id");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("surname", surname);
        pageVariables.put("name", name);
        pageVariables.put("sex", sex);
        pageVariables.put("birthday", birthday);
        pageVariables.put("nationality", nationality);
        pageVariables.put("address", address);
        pageVariables.put("group_id", group_id);
        pageVariables.put("id", id);

        // валидация полученных значений
        Map<String, String> errors = new HashMap<>();
        if (!validateSurname(surname)) {
            errors.put("surname", "Error surname!");
        }
        if (!validateName(name)) {
            errors.put("name", "Error name!");
        }
        if (!validateSex(sex)) {
            errors.put("sex", "Error sex!");
        }
        if (!validateBirthday(birthday)) {
            errors.put("birthday", "Error birthday!");
        }
        if (!validateNationality(nationality)) {
            errors.put("nationality", "Error nationality!");
        }
        if (!validateAddress(address)) {
            errors.put("address", "Error address!");
        }
        if (!validateGroupId(group_id)) {
            errors.put("group_id", "Error group!");
        }
        pageVariables.put("error", errors);

        if (errors.isEmpty()) {
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

            response.sendRedirect("/");
        } else {
            // setContentType должен быть перед getWriter
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(PageGenerator.instance().getPage("edit.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }
}

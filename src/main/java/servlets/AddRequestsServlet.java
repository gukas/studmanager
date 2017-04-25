package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbservice.ContextListener;
import templater.PageGenerator;

import static dbservice.Validation.validateAddress;
import static dbservice.Validation.validateBirthday;
import static dbservice.Validation.validateGroupId;
import static dbservice.Validation.validateMark;
import static dbservice.Validation.validateName;
import static dbservice.Validation.validateNationality;
import static dbservice.Validation.validateSex;
import static dbservice.Validation.validateSurname;

public class AddRequestsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("add.html", null));

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
        String mark = request.getParameter("mark");
        String group_id = request.getParameter("group_id");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("surname", surname);
        pageVariables.put("name", name);
        pageVariables.put("sex", sex);
        pageVariables.put("birthday", birthday);
        pageVariables.put("nationality", nationality);
        pageVariables.put("address", address);
        pageVariables.put("mark", mark);
        pageVariables.put("group_id", group_id);

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
        if (!validateMark(mark)) {
            errors.put("mark", "Error mark!");
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
                        "INSERT INTO student (surname, name, sex, birthday, nationality, address, mark, group_id) "
                                + "VALUES (?,?,?,?,?,?,?,?)");
                ps.setString(1, surname);
                ps.setString(2, name);
                ps.setString(3, sex);
                ps.setString(4, birthday);
                ps.setString(5, nationality);
                ps.setString(6, address);
                ps.setString(7, mark);
                ps.setString(8, group_id);
                ps.execute();
                System.out.println("Insert student!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            response.sendRedirect("/");
        } else {
            // setContentType должен быть перед getWriter
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(PageGenerator.instance().getPage("add.html", pageVariables));
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }
}

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

import templater.PageGenerator;

public class AddRequestsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = createPageVariablesMap(request);
        pageVariables.put("message", "");


        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("add.html", pageVariables));

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
        String mark = request.getParameter("mark");
        String group_id = request.getParameter("group_id");

        Map<String, String[]> params = request.getParameterMap();
        // TODO валидация полученных значений
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

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("add.html", pageVariables));
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

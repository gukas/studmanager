package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

public class ReportRequestsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Map<String, Object> pageVariables = new HashMap<>();
        List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
        DataSource ds = getDs(request.getServletContext());

        int report_id = Integer.parseInt(request.getParameter("id"));
        switch (report_id) {
            case 1:
                records = getSql("SELECT name, surname, mark FROM student", ds);
                break;
            case 2:
                records = getSql("SELECT s.name, s.surname, g.nomer, s.mark FROM student s LEFT JOIN group_st g on g.id = s.group_id", ds);
                break;
            case 3:
                records = getSql("SELECT s.name, s.surname, s.birthday, s.mark, g.nomer FROM student s LEFT JOIN group_st g on g.id = s.group_id", ds);
                break;
            // TODO все остальные отчеты
        }

        pageVariables.put("records", records);

        // setContentType должен быть перед getWriter
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("report.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {}

    private List<LinkedHashMap<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
        Map<String, Object> record = new LinkedHashMap<String, Object>();

        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        record.clear();
        for (int i = 1; i <= columnCount; i++ ) {
            record.put(rsmd.getColumnName(i), rsmd.getColumnName(i));
        }
        records.add(new LinkedHashMap<>(record));

        while (rs.next()) {
            record.clear();
            for (int i = 1; i <= columnCount; i++ ) {
                record.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
            }
            records.add(new LinkedHashMap<>(record));
        }
        return records;
    }

    private List<LinkedHashMap<String, Object>> getSql(String sql, DataSource ds) {
        List<LinkedHashMap<String, Object>> records = new ArrayList<LinkedHashMap<String, Object>>();
        try {
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            records = resultSetToList(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static DataSource getDs(ServletContext ctxt) {
        return (DataSource) ctxt.getAttribute(ContextListener.DS_PROPERTY_NAME);
    }
}

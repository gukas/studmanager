package dbservice;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

@WebListener
public class ContextListener implements ServletContextListener {
    public static final String DS_PROPERTY_NAME="APP_DATASOURCE";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute(DS_PROPERTY_NAME, createDataSorce());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private static DataSource createDataSorce () {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl("jdbc:mysql://localhost:3306/college?serverTimezone=UTC&useSSL=false&characterEncoding=UTF-8");
        mysqlDataSource.setUser("root");
        mysqlDataSource.setPassword("root");
        mysqlDataSource.setZeroDateTimeBehavior("convertToNull");
        return mysqlDataSource;
    }
}

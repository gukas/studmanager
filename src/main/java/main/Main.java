package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import dbservice.ContextListener;
import servlets.DeleteRequestsServlet;
import servlets.EditRequestsServlet;
import servlets.IndexRequestsServlet;
import servlets.AddRequestsServlet;
import servlets.ReportRequestsServlet;
import servlets.TasksRequestsServlet;

class Main {
    public static void main(String[] args) throws Exception {
        IndexRequestsServlet indexRequestsServlet = new IndexRequestsServlet();
        AddRequestsServlet addRequestsServlet = new AddRequestsServlet();
        EditRequestsServlet editRequestsServlet = new EditRequestsServlet();
        DeleteRequestsServlet deleteRequestsServlet = new DeleteRequestsServlet();
        TasksRequestsServlet tasksRequestsServlet = new TasksRequestsServlet();
        ReportRequestsServlet reportRequestsServlet = new ReportRequestsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(indexRequestsServlet), "/");
        context.addServlet(new ServletHolder(addRequestsServlet), "/add");
        context.addServlet(new ServletHolder(editRequestsServlet), "/edit");
        context.addServlet(new ServletHolder(deleteRequestsServlet), "/delete");
        context.addServlet(new ServletHolder(tasksRequestsServlet), "/tasks");
        context.addServlet(new ServletHolder(reportRequestsServlet), "/report");
        context.addEventListener(new ContextListener());

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}

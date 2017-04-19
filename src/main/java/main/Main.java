package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.ContextListener;
import servlets.EditRequestsServlet;
import servlets.IndexRequestsServlet;
import servlets.AddRequestsServlet;

class Main {
    public static void main(String[] args) throws Exception {
        IndexRequestsServlet indexRequestsServlet = new IndexRequestsServlet();
        AddRequestsServlet addRequestsServlet = new AddRequestsServlet();
        EditRequestsServlet editRequestsServlet = new EditRequestsServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(indexRequestsServlet), "/");
        context.addServlet(new ServletHolder(addRequestsServlet), "/add");
        context.addServlet(new ServletHolder(editRequestsServlet), "/edit/*");
        context.addEventListener(new ContextListener());

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}

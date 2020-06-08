package br.com.alce;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class SaleService {

    public static void main(String[] args) throws Exception {

        var server = new Server(8888);

        var context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new SaleServlet()), "/sale");

        server.setHandler(context);

        server.start();
        server.join();
    }
}

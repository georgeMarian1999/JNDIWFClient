package com.servletsWF;



import com.repo.MovieRepositoryR;
import javax.naming.Context;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/deleteMovie")
public class DeleteMovieServlet extends HttpServlet {

    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        JNDIProps.put("java.naming.provider.url","http-remoting://localhost:8080");
    }
    static final String JNDI = "EJBServer/MovieRepositoryBean!com.repo.MovieRepositoryR";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Context context;
        MovieRepositoryR movieRepository = null;
        try {
            context = new InitialContext(JNDIProps);
            movieRepository = (MovieRepositoryR) context.lookup(JNDI);

        } catch (NamingException e) {
            e.printStackTrace();
        }
        try {
            String id = request.getParameter("deleteId");
            int ID = 0;
            if(id != null) {
                ID = Integer.parseInt(id);
            }
            else {
                request.getRequestDispatcher("/error").forward(request, response);
            }
            if (movieRepository != null) {
                movieRepository.deleteById(ID);
                System.out.println("Movie with id "+id+" deleted");
                request.getRequestDispatcher("/").forward(request, response);
            }
            else  {
                request.getRequestDispatcher("/error").forward(request, response);
            }
        }catch (Exception e){
            request.getRequestDispatcher("/").forward(request, response);
        }

    }
}

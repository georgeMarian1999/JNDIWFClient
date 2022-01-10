package com.servletsWF;

import com.model.Movie;
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

@WebServlet("/addMovieLogic")
public class AddServlet extends HttpServlet {

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
            String title = request.getParameter("title");
            int rating = Integer.parseInt(request.getParameter("rating"));
            String genre = request.getParameter("genre");
            if (movieRepository != null) {
                Movie movie = new Movie();
                movie.setTitle(title);
                movie.setRating(rating);
                movie.setGenre(genre);
                movieRepository.save(movie);
            }

            System.out.println("New movie saved");
            request.getRequestDispatcher("/addSuccess").forward(request, response);

        }catch (Exception e){
            e.printStackTrace();
            request.getRequestDispatcher("/error").forward(request, response);
        }

    }
}

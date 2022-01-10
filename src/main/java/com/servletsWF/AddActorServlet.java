package com.servletsWF;




import com.model.Actor;
import com.repo.ActorRepositoryR;
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

@WebServlet("/addActorLogic")
public class AddActorServlet extends HttpServlet {

    static final Properties JNDIProps = new Properties();
    static {
        JNDIProps.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        JNDIProps.put("java.naming.provider.url","http-remoting://localhost:8080");
    }
    static final String ACTORS_JNDI = "EJBServer/ActorsRepositoryBean!com.repo.ActorRepositoryR";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Context context;
        MovieRepositoryR movieRepository = null;
        ActorRepositoryR actorRepository = null;
        try {
            context = new InitialContext(JNDIProps);
            actorRepository = (ActorRepositoryR) context.lookup(ACTORS_JNDI);

        } catch (NamingException e) {
            e.printStackTrace();
        }


        try {

            String name = request.getParameter("name");
            int age = Integer.parseInt(request.getParameter("age"));
            String gender = request.getParameter("gender");
            int movieID = Integer.parseInt(request.getParameter("movieID"));
            if (actorRepository != null) {
                Actor actor = new Actor();
                actor.setName(name);
                actor.setGender(gender);
                actor.setAge(age);
                actorRepository.save(actor, movieID);
            }

            request.setAttribute("name", null);
            request.getRequestDispatcher("/actors").forward(request, response);

        }catch (Exception e){
            request.getRequestDispatcher("/error").forward(request, response);
        }

    }
}

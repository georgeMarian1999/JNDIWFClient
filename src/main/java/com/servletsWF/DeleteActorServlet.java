package com.servletsWF;




import com.repo.ActorRepositoryR;

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

@WebServlet("/deleteActor")
public class DeleteActorServlet extends HttpServlet {

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
        ActorRepositoryR actorRepository = null;
        try {
            context = new InitialContext(JNDIProps);
            actorRepository = (ActorRepositoryR) context.lookup(ACTORS_JNDI);

        } catch (NamingException e) {
            e.printStackTrace();
        }

        try {
            String id = request.getParameter("deleteActorId");
            int ID = 0;
            if(id != null) {
                ID = Integer.parseInt(id);
            }
            else {
                request.getRequestDispatcher("/error").forward(request, response);
            }
            if (actorRepository != null) {
                actorRepository.deleteById(ID);
                System.out.println("Actor with id "+id+" deleted");
            }
            else {
                request.getRequestDispatcher("/error").forward(request, response);
            }

            request.getRequestDispatcher("/").forward(request, response);

        }catch (Exception e){
            request.getRequestDispatcher("/").forward(request, response);
        }

    }
}

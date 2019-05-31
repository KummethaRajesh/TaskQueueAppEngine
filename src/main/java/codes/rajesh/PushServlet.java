package codes.rajesh;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@WebServlet("pushServlet")
public class PushServlet extends HttpServlet {
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		String randomId=String.valueOf((int)(Math.random() * 50 + 1));
		String personName=request.getParameter("name");
		String personEmail=request.getParameter("email");
		
		Logger log=Logger.getLogger(PushServlet.class.getName());
		log.info("Servlet Person:\nname: "+personName+", email: "+personEmail+", Id: "+randomId);
		
		Queue queue=QueueFactory.getQueue("store-push-queue");
		queue.add(TaskOptions.Builder.withUrl("/pushWorker")
				.param("name", personName)
				.param("email", personEmail)
				.param("id", randomId));
		
		response.getWriter().print("User creation job has been assigned to queue");
	}
}

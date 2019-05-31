package codes.rajesh;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@WebServlet("pushWorker")
public class PushWorker extends HttpServlet {
	public void doPost(HttpServletRequest request,HttpServletResponse response) {
		
		String personId=request.getParameter("id");
		String personName=request.getParameter("name");
		String personEmail=request.getParameter("email");
		
		Logger log=Logger.getLogger(PushWorker.class.getName());
		log.info("Worker Person:\nname: "+personName+", email: "+personEmail+", Id: "+personId);
		
		Entity personEntity=new Entity("Person",personId);
		personEntity.setProperty("id",personId);
		personEntity.setProperty("name", personName);
		personEntity.setProperty("email", personEmail);
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(personEntity);
		response.setStatus(response.SC_OK);
	}
}

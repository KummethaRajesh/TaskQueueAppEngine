package codes.rajesh;

import java.util.logging.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LogEntry.Builder;

import codes.rajesh.enhancers.LogEnhancer;


@WebServlet("/logData")
public class LogDataServlet extends HttpServlet {
	
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		final Logger logger = Logger.getLogger(LogDataServlet.class.getName());
		logger.info("I am info ");
		logger.severe("I am severe ");

	}

}

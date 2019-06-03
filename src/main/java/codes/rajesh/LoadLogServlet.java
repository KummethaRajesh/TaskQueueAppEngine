package codes.rajesh;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import com.google.api.gax.paging.Page;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.Logging.EntryListOption;
import com.google.cloud.logging.LoggingOptions;

@WebServlet("/loadLog")
public class LoadLogServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out=response.getWriter();
		response.setContentType("text/html");
		
		LoggingOptions loggingOptions = LoggingOptions.getDefaultInstance();

		String logName = LoadLogServlet.class.getName();

		try (Logging logging = loggingOptions.getService()) {

		  String logFilter = "logName=projects/" + loggingOptions.getProjectId() + "/logs/" + logName;

		  
		  Page<LogEntry> entries = logging.listLogEntries(
		      EntryListOption.filter(logFilter));
		  do {
		    for (LogEntry logEntry : entries.iterateAll()) {
		      out.write(logEntry.toString());
		    }
		    entries = entries.getNextPage();
		  } while (entries != null);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

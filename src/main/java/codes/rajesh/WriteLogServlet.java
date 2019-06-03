package codes.rajesh;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.api.gax.paging.Page;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.Logging.EntryListOption;
import com.google.cloud.logging.Logging.WriteOption;
import com.google.cloud.logging.Payload.JsonPayload;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.LoggingOptions;

@WebServlet("writeLog")
public class WriteLogServlet extends HttpServlet {
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		Logging logging=LoggingOptions.getDefaultInstance().getService();
		
		List<LogEntry> logEntries = new ArrayList<>();
		
		logEntries.add(LogEntry.of(StringPayload.of("I am data")));
		
		Map<String,Object> map = new HashMap<>();
		map.put("name", "Rajesh");
		
		logEntries.add(LogEntry.of(JsonPayload.of(map)));
		
		logging.write(logEntries, WriteOption.logName("tomLog"),WriteOption.resource(MonitoredResource.newBuilder("global").build()));
		
		
	}
}

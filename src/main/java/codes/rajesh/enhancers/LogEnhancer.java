package codes.rajesh.enhancers;

import javax.servlet.annotation.WebServlet;

import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.LoggingEnhancer;

//@WebServlet("/enhanceServlet")
public class LogEnhancer implements LoggingEnhancer {

  @Override
  public void enhanceLogEntry(LogEntry.Builder logEntry) {
    
    logEntry.addLabel("name", "sample");
  }
}
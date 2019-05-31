
package codes.rajesh;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pullServletWorker")
public class TaskqueueServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(TaskqueueServlet.class.getName());
  private static final int numberOfTasksToAdd = 100;
  private static final int numberOfTasksToLease = 100;
  private static boolean useTaggedTasks = true;
  private static String output;
  private static String result;

  
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws IOException, ServletException {
    if (req.getParameter("addTask") != null) {
    	
      String payload = req.getParameter("payload");
      String output = "Adding "+numberOfTasksToAdd+" Tasks to the Task Queue with a payload of "+payload.toString();
      
      log.info(output.toString());

      Queue q = QueueFactory.getQueue("pull-queue");
      
      if (!useTaggedTasks) {
        for (int i = 0; i < numberOfTasksToAdd; i++) {
          
          q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(payload.toString()));
        }
      } else {
        for (int i = 0; i < numberOfTasksToAdd; i++) {
   
          q.add(
              TaskOptions.Builder.withMethod(TaskOptions.Method.PULL)
                  .payload(payload.toString())
                  .tag("process".getBytes()));
          
        }
      }
      
        result = "Added " + numberOfTasksToAdd + " tasks to the task queue.";
        req.setAttribute("result", result);
        req.getRequestDispatcher("pull.html").forward(req, resp);
      
    } else {
      if (req.getParameter("leaseTask") != null) {
        output = "Pulling "+numberOfTasksToLease+" Tasks from the Task Queue";
        
        log.info(output.toString());

        Queue q = QueueFactory.getQueue("pull-queue");
        
        if (!useTaggedTasks) {
          
          List<TaskHandle> tasks = q.leaseTasks(3600, TimeUnit.SECONDS, numberOfTasksToLease);
          
          result = processTasks(tasks, q);
        } else {
          
          List<TaskHandle> tasks = q.leaseTasksByTag(3600, TimeUnit.SECONDS, numberOfTasksToLease, "process");
          
          result = processTasks(tasks, q);
        }
        req.setAttribute("result", result);
        req.getRequestDispatcher("pull.html").forward(req, resp);
      } else {
        resp.sendRedirect("/");
      }
    }
  }


  private static String processTasks(List<TaskHandle> tasks, Queue q) {
    String payload;
    
    int numberOfDeletedTasks = 0;
    
    for (TaskHandle task : tasks) {
    	
      payload = new String(task.getPayload());
      output = "Processing: taskName="+task.getName().toString()+"  payload="+payload.toString();
      
      log.info(output.toString());
      
      output = "Deleting taskName="+ task.getName().toString();
      
      log.info(output.toString());
      
      q.deleteTask(task);
      
      numberOfDeletedTasks++;
    }
    if (numberOfDeletedTasks > 0) {
      result = "Processed and deleted " + numberOfTasksToLease + " tasks from the " + " task queue.";
    } 
    else {
    	result = "Task Queue has no tasks available for lease.";
    }
    return result;
  }
}
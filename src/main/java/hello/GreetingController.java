package hello;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Controller
public class GreetingController {
	@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
    	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	Date now = new Date();    
    	String connectionUrl = "jdbc:sqlserver://";
    	connectionUrl += System.getenv("jactSqlAdress")+":";
    	connectionUrl += System.getenv("jactSqlPort")+";";
    	connectionUrl += "databaseName="+System.getenv("jactSqlName")+";";
    	connectionUrl += "user="+System.getenv("jactSqlUser")+";";
    	connectionUrl += "password="+System.getenv("jactSqlPw")+";";
    	System.out.println(connectionUrl);
        //Thread.sleep(50); // simulated delay
    	try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "INSERT INTO [dbo].[LOGS2]([MESSAGE]) VALUES ('"+now.toString() +" : "+HtmlUtils.htmlEscape(message.getName())+"')";
            stmt.executeUpdate(SQL);

            // Iterate through the data in the result set and display it.

        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new Greeting(dateFormat.format(now)+" - <b>Anonymous :</b> " + HtmlUtils.htmlEscape(message.getName()));
    }
	
	
	@RequestMapping("/history")
    public String history(Model model) {
		String connectionUrl = "jdbc:sqlserver://";
    	connectionUrl += System.getenv("jactSqlAdress")+":";
    	connectionUrl += System.getenv("jactSqlPort")+";";
    	connectionUrl += "databaseName="+System.getenv("jactSqlName")+";";
    	connectionUrl += "user="+System.getenv("jactSqlUser")+";";
    	connectionUrl += "password="+System.getenv("jactSqlPw")+";";
    	System.out.println(connectionUrl);
        //Thread.sleep(50); // simulated delay
    	try (Connection con = DriverManager.getConnection(connectionUrl); Statement stmt = con.createStatement();) {
            String SQL = "SELECT TOP (100) [LOGID],[MESSAGE] FROM [dbo].[LOGS2] order by LOGID DESC;";
            ResultSet rs = stmt.executeQuery(SQL);
            List<String>history = new ArrayList<>();
            while(rs.next()) {
            	history.add(rs.getString("MESSAGE"));
            }
            model.addAttribute("history", history);
        }
        // Handle any errors that may have occurred.
        catch (SQLException e) {
            e.printStackTrace();
        }
        return "history";
    }
	


}
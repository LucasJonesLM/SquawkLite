package squawk;
import java.util.HashMap;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static spark.Spark.*;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;


public class RunSquawk {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		port(3000);
		ArrayList<SquawkUser> users = new ArrayList<SquawkUser>();
		
		get("/", (req, res) -> {
            System.out.println("request made");

            JtwigTemplate template = JtwigTemplate.classpathTemplate("/users.txt");
            JtwigModel model = JtwigModel.newModel().with("users", users);

            return template.render(model);
        });
		

		post("/createUser", (req, res) -> {
			System.out.println("request made");
			users.add(new SquawkUser(req.queryParams("userName"),
					req.queryParams("password"), req.queryParams("email")));
			SquawkDB newUser = new SquawkDB();
			if(newUser.checkUserExists(req.queryParams("userName"))){		
			System.out.println("user does not exist");
			newUser.insertUser(req.queryParams("userName"), req.queryParams("password"), req.queryParams("email"));
			return true;
			}
			System.out.println("User already exists!");
			return false;
		});
			 
		
		get("/create", (req, res) -> {
			JtwigTemplate template = JtwigTemplate.classpathTemplate("/newUserTemplate.txt");
			JtwigModel model = JtwigModel.newModel();
			System.out.println("request made");
			
			return template.render(model);

		});
		
		

	}




}

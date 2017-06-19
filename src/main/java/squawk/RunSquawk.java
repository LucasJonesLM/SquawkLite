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

			// req.session().attribute("userid");
			// req.session().attribute("userid", 67);
			//
			// int id = req.session().attribute("userid");

			JtwigTemplate template = JtwigTemplate.classpathTemplate("/ExistingUserForm.html");
			JtwigModel model = JtwigModel.newModel().with("users", users);

			return template.render(model);
		});

		get("/SquawkTimeline", (req, res) -> {
			System.out.println("request made");
			SquawkUser user = req.session().attribute("user");
			if(user == null){
				res.redirect("/");
				return "login";
			}
			JtwigTemplate template = JtwigTemplate.classpathTemplate("/SquawkTimeline.html");
			JtwigModel model = JtwigModel.newModel();
			return template.render(model);
		});

		// sets User for session
		post("/sessionID", (req, res) -> {
			System.out.println("request session");
			SquawkDB sessionReq = new SquawkDB();
			SquawkUser u = sessionReq.authenticateUsers(req.queryParams("userName"), req.queryParams("password"));
			if (u == null) {
				sessionReq.close();
				return "Invalid User Name and Password";
			}
			req.session().attribute("user", u);
			System.out.println("session success!");
			System.out.println(u.toString());
			sessionReq.close();
			return "Yes";
		});

		post("/createUser", (req, res) -> {
			System.out.println("request made");
			SquawkDB newUser = new SquawkDB();
			String u;
			if (newUser.checkUserExists(req.queryParams("userName"))) {
				System.out.println("user does not exist");
				newUser.insertUser(req.queryParams("userName"), req.queryParams("password"), req.queryParams("email"));
				u = "Yes";
			} else {
				System.out.println("User already exists!");
				u = "No";
			}
			newUser.close();
			return u;
		});

		get("/create", (req, res) -> {
			JtwigTemplate template = JtwigTemplate.classpathTemplate("/NewUserForm.html");
			JtwigModel model = JtwigModel.newModel();
			System.out.println("request made");
			return template.render(model);
		});

		post("/createSquawk", (req, res) -> {
			System.out.println("request made");
			SquawkUser user = req.session().attribute("user");
			if(user == null){
				res.status(403);
				return "login";
			}
			SquawkDB newSquawk = new SquawkDB();
			newSquawk.insertSquawk(0, null);
			newSquawk.close();
			return null;
		});

	}
}

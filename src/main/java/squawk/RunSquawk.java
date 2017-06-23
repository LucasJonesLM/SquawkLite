package squawk;

import java.util.HashMap;
import java.util.Map;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import spark.Route;

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
			req.session().removeAttribute("user");
			JtwigTemplate template = JtwigTemplate.classpathTemplate("/ExistingUserForm.html");
			JtwigModel model = JtwigModel.newModel().with("users", users);
			return template.render(model);
		});

		get("/SquawkTimeline", (req, res) -> {
			System.out.println("request made");
			SquawkUser user = req.session().attribute("user");
			if (user == null) {
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
			SquawkUser u = sessionReq.authenticateUsers(
					req.queryParams("userName"), req.queryParams("password"));
			if (u == null) {
				sessionReq.close();
				return "Invalid User Name and Password";
			}
			req.session().attribute("user", u);
			sessionReq.close();
			return "Yes";
		});

		post("/createUser", (req, res) -> {
			System.out.println("request made");
			SquawkDB newUser = new SquawkDB();
			String u;
			if (newUser.checkUserExists(req.queryParams("userName"))) {
				System.out.println("user does not exist");
				newUser.insertUser(req.queryParams("userName"),
						req.queryParams("password"), req.queryParams("email"));
				u = "Yes";
			} else {
				System.out.println("User already exists!");
				u = "No";
			}
			newUser.close();
			return u;
		});

		get("/create", (req, res) -> {
			JtwigTemplate template = JtwigTemplate
					.classpathTemplate("/NewUserForm.html");
			JtwigModel model = JtwigModel.newModel();
			System.out.println("request made");
			return template.render(model);
		});
		// posts squawk for first time
		post("/createSquawk", (req, res) -> {
			System.out.println("request made");
			SquawkUser user = req.session().attribute("user");
			if (user == null) {
				res.status(403);
				return "login";
			}
			SquawkDB newSquawk = new SquawkDB();
			int uID = user.userID;
			System.out.println(uID);
			newSquawk.insertSquawk(uID, req.queryParams("SquawkPost"));
			newSquawk.close();
			return "yes";
		});
		
	post("/like", (req, res) -> {
			System.out.println("Like posting");
			SquawkDB like = new SquawkDB();
			int msgID = Integer.parseInt(req.queryParams("MsgID"));
			like.setLike(msgID);
			like.close();
			return "";
		});
	
	post("/timeLineSquawk", (req, res) -> {
		System.out.println("run timeline");
		SquawkUser user = req.session().attribute("user");
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		Gson gson = new Gson();
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		String timelineJson = gson.toJson(timeLineSquawk.timeLineSquawks(user.userID));
		timeLineSquawk.close();
		System.out.println("send Gson");
		return timelineJson;
		});
	
	post("/MySquawks", (req, res) -> {
		System.out.println("run my Squawks");
		SquawkUser user = req.session().attribute("user");
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		Gson gson = new Gson();
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		String timelineJson = gson.toJson(timeLineSquawk.renderMySquawks(user.userID));
		timeLineSquawk.close();
		System.out.println("send Gson");
		return timelineJson;
		});
	
	get("/UserSquawks", (req, res) -> {
		System.out.println("request made");
		SquawkUser user = req.session().attribute("user");
		if(user == null){
			res.redirect("/");
			return "login";
		}
		JtwigTemplate template = JtwigTemplate.classpathTemplate("/UserSquawks.html");
		JtwigModel model = JtwigModel.newModel();
		return template.render(model);
	});
	
	get("/IfollowSquawks", (req, res) -> {
		System.out.println("request made");
		SquawkUser user = req.session().attribute("user");
		if(user == null){
			res.redirect("/");
			return "login";
		}
		JtwigTemplate template = JtwigTemplate.classpathTemplate("/IfollowSquawk.html");
		JtwigModel model = JtwigModel.newModel();
		return template.render(model);
	});
	
	get("/user/:username", (req, res)-> {
		String username = req.params(":username");
		System.out.println(username);
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		ArrayList<SquawkMsg> squawk = timeLineSquawk.otherSquawks(username);
		System.out.println(squawk.toString());
		JtwigTemplate template = JtwigTemplate.classpathTemplate("/OtherSquawks.html");
		JtwigModel model = JtwigModel.newModel().with("squawk", squawk);
		return template.render(model);
	});
	
	post("/Ifollow", (req, res) -> {
		System.out.println("run my Squawks");
		SquawkUser user = req.session().attribute("user");
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		Gson gson = new Gson();
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		String timelineJson = gson.toJson(timeLineSquawk.Ifollow(user.userID));
		timeLineSquawk.close();
		System.out.println("send Gson");
		return timelineJson;
		});
	
	get("/FollowMe", (req, res) -> {
		System.out.println("request made");
		SquawkUser user = req.session().attribute("user");
		if(user == null){
			res.redirect("/");
			return "login";
		}
		JtwigTemplate template = JtwigTemplate.classpathTemplate("/FollowMeSquawk.html");
		JtwigModel model = JtwigModel.newModel();
		return template.render(model);
	});
	
	post("/FollowMeSquawks", (req, res) -> {
		System.out.println("run my Squawks");
		SquawkUser user = req.session().attribute("user");
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		Gson gson = new Gson();
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		String timelineJson = gson.toJson(timeLineSquawk.FollowMe(user.userID));
		timeLineSquawk.close();
		System.out.println("send Gson");
		return timelineJson;
		});
	
	get("/PopularSquawkers", (req, res) -> {
		System.out.println("request made");
		SquawkUser user = req.session().attribute("user");
		if(user == null){
			res.redirect("/");
			return "login";
		}
		JtwigTemplate template = JtwigTemplate.classpathTemplate("/PopularSquawkers.html");
		JtwigModel model = JtwigModel.newModel();
		return template.render(model);
	});
	
	post("/SquawkerList", (req, res) -> {
		System.out.println("Squawker List");
		SquawkDB timeLineSquawk = new SquawkDB(); //db connection
		Gson gson = new Gson();
		// call db connection timeLineSquawk call method timeLineSquawks pass (uID)
		String SquawkersJson = gson.toJson(timeLineSquawk.SquawkerList());
		timeLineSquawk.close();
		System.out.println("send Gson");
		return SquawkersJson;
		});
	}

}

package course_evaluation.controllers;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import course_evaluation.beans.Course;
import course_evaluation.beans.Evaluation;
import course_evaluation.database.DatabaseAccess;





@Controller
public class MainController {
	@Autowired
	private DatabaseAccess da;
	
	@GetMapping("/login")
	public String login(){
		return "login.html";
	}
	
	@GetMapping("/logout")
	public String getLogOut() {
		return "login.html";
	}
	
	//mapping to home page
	@GetMapping("/")
	public String index() {
		return "index.html";
	}
	
	//mapping to home page
		@GetMapping("/gpacalculate")
		public String gpacalculate(HttpSession session) {
			
			if(session.getAttribute("courseComplete") == null)
				session.setAttribute("courseComplete", da.getEvalByCourseComplete());
			
			return "gpacalculate.html";
		}
	
	@GetMapping("/evalcalc")
	public String fillEvaluation(Model model, HttpSession session) {
		//get courses from database and populate in the form (evaluation.html)
		if(session.getAttribute("courses") == null) {
			//retrieve courses from the database by sql statement
			session.setAttribute("courses", da.getCourses());
		}
		//binding object model to view
		model.addAttribute("evaluation",new Evaluation());
		
		return "evaluation.html";
		
	}
	
	
	//The parameter variable name should be the same as the model attribute.
	@PostMapping("/eval")
	public String insertEvaluation(Model model, HttpSession session, @ModelAttribute Evaluation evaluation) {
		
		//if edit does not exist, assume false
		boolean edit = (session.getAttribute("edit") == null)
		? false : (boolean)session.getAttribute("edit");
		//if edit == true, then do update
		if(edit) {
			da.updateEvaluation(evaluation);
		}
		//if edit == false, then do insert
		else
			da.insertEvaluation(evaluation);
		
		session.removeAttribute("edit");
		
		session.setAttribute("eval", da.getEvaluations());		
		System.out.println(model.getAttribute("evaluation"));
		return "evalResults.html";
	}
	
	@GetMapping("/evaluationRecords")
	public String inventory(Model model, HttpSession session) {
			if(session.getAttribute("eval") == null)
			session.setAttribute("eval", da.getEvaluations());
		
			return "evalResults.html";
	}
	
	@GetMapping("/addcourse")
	public String addCourse(Model model) {
		//binding adding course form to model "course"
		model.addAttribute("course", new Course());
		return "course.html";
	}
	
	@PostMapping("/courseResults")
	public String insertCourse(HttpSession session, @ModelAttribute Course course) {
		da.insertCourse(course);
		//set updated courses to session
		session.setAttribute("courses", da.getCourses());
		return "courseResults.html";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(HttpSession session, Model model, @PathVariable int id) {
		
		//could also grab from evaluation list but it;s more work
		Evaluation eva = da.getEvaluationById(id);
		//display data needs to be edited in fields
		model.addAttribute("evaluation", eva);
		session.setAttribute("courses", da.getCourses());
		session.setAttribute("edit", true);
		
		return "evaluation.html";
	}	
	
	
	
	@GetMapping(value="/get/{code}", produces="application/json")
	@ResponseBody
	public Map<String, Object> getCourse(@PathVariable String code, RestTemplate rest){
		//Create a new HashMap, which will contain the key-value pairs for 
		//the container's member names and their values:
		Map<String, Object> data = new HashMap<String, Object>();
		//Invoke the getForEntity() method to make the GET request with 
		//the specified path variable id. The method will return a Container. 
		ResponseEntity<Course> responseEntity =
				rest.getForEntity("https://localhost:8443/evaluation/get/{code}", 
						Course.class, code);
		//Add the response body to the hash map. 
		data.put("course", responseEntity.getBody());
		//return the hash map
		return data;
	}
	
	@GetMapping("/geteval")
	public String getEvaluation(HttpSession session, RestTemplate rest){
		Evaluation[] list
			= rest.getForObject("https://localhost:8443/evaluation/getEvaluation", Evaluation[].class);
		  //= rest.getForObject("http://localhost:8080/evaluation/getEvaluation", Evaluation[].class);
		session.setAttribute("eval", list);
		return  "evalResults.html";
	}
}

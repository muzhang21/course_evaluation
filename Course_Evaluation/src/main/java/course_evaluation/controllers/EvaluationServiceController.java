package course_evaluation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import course_evaluation.beans.Course;
import course_evaluation.beans.Evaluation;
import course_evaluation.database.DatabaseAccess;


@RequestMapping("/evaluation")
@RestController
public class EvaluationServiceController {
	
	@Autowired
	private DatabaseAccess da;
	
	@GetMapping(value="/get/{code}")
	public Course getCourseByCode(@PathVariable String code) {
		return da.getCourseByCode(code);
	}
	
	@GetMapping(value="/getEvaluation")
	public List<Evaluation> getEvaluationCollection(){
		//invoke database access method and return objects
		return da.getEvaluations();
	}
}

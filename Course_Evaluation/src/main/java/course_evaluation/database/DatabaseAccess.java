package course_evaluation.database;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import course_evaluation.beans.Course;
import course_evaluation.beans.Evaluation;





@Repository
public class DatabaseAccess {
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	
	//retrieve courses in the database and populate in the index.html
	public List<Course> getCourses(){
		//sql statement that retrieves the list of courses objects
		String sql = "SELECT * FROM courses ORDER BY title, code;";
		//class data member names match column names, map each row's field to Course bean
		ArrayList<Course> courses = (ArrayList<Course>) jdbc.query(sql,
				new BeanPropertyRowMapper<Course>(Course.class));
		return courses;
		
	}
	
	public Course getCourseByCode(String code) {
		//select record by code
				String sql = "SELECT * FROM courses WHERE code=:code;";
				MapSqlParameterSource params = new MapSqlParameterSource();
				//map id parameter
				params.addValue("code", code);
				//column names match Course data member names, and map each row's field to Course bean
				ArrayList<Course> list = (ArrayList<Course>) jdbc.query(sql, params,
						new BeanPropertyRowMapper<Course>(Course.class));
				
				if(list.size() > 0) {
					//get the first record in the list
					return list.get(0);
				}
				else {
					return null;
				}
	}
	
	//add course to the database
	public void insertCourse(Course c) {
		//Insert data to table courses by using named parameter
		String sql ="INSERT INTO courses (code, title, credits) "
				+"VALUES(:code, :title, :credits);";
		//create object of MapSqlParameterSource class
		MapSqlParameterSource params = new MapSqlParameterSource();
		//using MapsqlParameterSource object to map each parameter to an actual value
		params.addValue("code", c.getCode()).addValue("title", c.getTitle())
				.addValue("credits", c.getCredits());
		//execute query and pass it the map of parameters
		jdbc.update(sql, params);
	}
	
	public void insertEvaluation(Evaluation e) {
		//Insert data to table evaluations by using named parameter
		String sql = "INSERT INTO evaluations (title, course, grade, max, weight, duedate) "
				+"VALUES(:title, :course, :grade, :max, :weight, :duedate);";
		MapSqlParameterSource params = new MapSqlParameterSource();
		//using MapSqlParameterSource object params to map each parameter to an actual value
		params.addValue("title", e.getTitle()).addValue("course", e.getCourse())
		.addValue("grade", e.getGrade()).addValue("max", e.getMax()).addValue("weight", e.getWeight())
		.addValue("duedate", e.getDueDate());
		//execute sql statement and pass it the map of parameters
		jdbc.update(sql, params);
	}
	
	public List<Evaluation> getEvaluations(){
		//retrieve evaluations records 
		String sql = "SELECT * FROM evaluations ORDER BY dueDate, course;";
		//column names match Evaluation data member names, and map each row's field to Evaluation bean
		ArrayList<Evaluation> eva = (ArrayList<Evaluation>) jdbc.query(sql,
				new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		return eva;
		
	}
	
	public Evaluation getEvaluationById(int id) {
		//select record by id
		String sql = "SELECT * FROM evaluations WHERE id=:id;";
		MapSqlParameterSource params = new MapSqlParameterSource();
		//map id parameter
		params.addValue("id", id);
		//column names match Evaluation data member names, and map each row's field to Evaluation bean
		ArrayList<Evaluation> list = (ArrayList<Evaluation>) jdbc.query(sql, params,
				new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		if(list.size() > 0) {
			//get the first record in the list
			return list.get(0);
		}
		else {
			return null;
		}
	}
	
	public int updateEvaluation(Evaluation e) {
		String sql = "UPDATE evaluations SET title=:title, course=:course, "
				+ "grade=:grade, max=:max, weight=:weight, duedate=:duedate WHERE id=:id;";
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id", e.getId()).addValue("title", e.getTitle())
			.addValue("course", e.getCourse()).addValue("grade", e.getGrade())
			.addValue("max", e.getMax()).addValue("weight", e.getWeight())
			.addValue("duedate", e.getDueDate());
			
			return jdbc.update(sql, params);
				
	}
	
	
	
	public Map<Evaluation, Course> getEvalByCourseComplete(){
		String sql = "SELECT * FROM evaluations INNER JOIN courses ON "
				+"evaluations.course = courses.code WHERE courses.complete='TRUE' "
				+"ORDER BY courses.code;";
				
		//construct evaluation object first
		//use it as key in a hash map where value is the course obj
		Map<Evaluation, Course> map = new HashMap<Evaluation, Course>();
		List<Map<String, Object>> rows = jdbc.queryForList(sql, new HashMap());
		for(Map<String,Object> row: rows) {
			Evaluation eval = new Evaluation((String)row.get("title"),
					(String)row.get("course"),
					((BigDecimal)row.get("grade")).doubleValue(),
					((BigDecimal)row.get("max")).doubleValue(),
					((BigDecimal)row.get("weight")).doubleValue(),
					((LocalDate)row.get("dueDate"))
					);
					
			Course course = new Course((String)row.get("code"),
					(String)row.get("title"),
					((BigDecimal)row.get("credits")).doubleValue(),
					(Boolean)row.get("complete")
					);
			map.put(eval, course);
		}
		return map;
		
	}
	
}
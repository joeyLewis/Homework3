package core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IInstructor;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Instructor;
import core.api.impl.Student;

public class TestInstructor
{
	private IAdmin _admin;
	private IStudent _student;
	private IInstructor _instructor;
	
	private String _course1;
	private String _course2;
	private String _invalidCourse;
	private int _classYear;
	
	private String _instructor1;
	private String _instructor2;
	
	private String _student1;
	private String _student2;
	
	private String _homework1;
	private String _homework2;
	private String _invalidHomework;
	
	@Before
	public void setup()
	{
		_course1 = "Test-Class1";
		_course2 = "Test-Class2";
		_invalidCourse = "TestClass-Invalid";
		_classYear = 2017;
		
		_instructor1 = "Test-Instructor1";
		_instructor2 = "Test-Instructor2";
		
		_student1 = "Test-Student1";
		_student2 = "Test-Student2";
		
		_homework1 = "Test-Homework1";
		_homework2 = "Test-Homework2";
		_invalidHomework = "Test-InvalidHomework";
		
		_admin = new Admin();
		_student = new Student();
		_instructor = new Instructor();
		
		_admin.createClass(_course1, _classYear, _instructor1, 10);
		_admin.createClass(_course2, _classYear, _instructor1, 10);
		_admin.createClass(_course1, _classYear + 1, _instructor1, 10);

		_admin.createClass(_course2, _classYear, _instructor2, 10);
		_admin.createClass(_course2, _classYear + 1, _instructor2, 10);
		
		_student.registerForClass(_student1, _course1, _classYear);
		_student.registerForClass(_student1, _course1, _classYear + 1);

		_student.registerForClass(_student2, _course2, _classYear);
		_student.registerForClass(_student2, _course2, _classYear + 1);
	}
	
	// ---------- Tests Add Homework ---------- 
	
	@Test
	public void testAddHomework()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		assertTrue(_instructor.homeworkExists(_course1, _classYear, _homework1));
	}
	
	@Test
	public void testAddHomeworkNotTeachingExistingCourse()
	{
		_instructor.addHomework(_instructor2, _course1, _classYear, _homework1);
		assertFalse(_instructor.homeworkExists(_course1, _classYear, _homework1));
	}
	
	@Test
	public void testAddHomeworkCourseNotExist()
	{
		_instructor.addHomework(_instructor1, _invalidCourse, _classYear, _homework1);
		assertFalse(_instructor.homeworkExists(_invalidCourse, _classYear, _homework1));;
	}
	
	@Test
	public void testAddHomeworkCourse1Not2()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		assertFalse(_instructor.homeworkExists(_course2, _classYear, _homework1));
	}
	
	@Test
	public void testAddHomework1Not2()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		assertFalse(_instructor.homeworkExists(_course1, _classYear, _homework2));
	}
	
	@Test
	public void testAddHomeworkCourseInEarlierYearNotLater()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		assertFalse(_instructor.homeworkExists(_course1, _classYear + 1, _homework1));
	}
	
	// ---------- Tests Assigned Grade ----------
	
	@Test
	public void assignValidGrade()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 50);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(50));
	}
	
	@Test
	public void assignZeroGrade()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 0);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(0));
	}
	
	@Test
	public void assignTopGrade()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 100);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(100));
	}
	
	@Test
	public void assignNonPercentLowGrade()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, -10);
		assertFalse(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(-10));
	}
	
	@Test
	public void assignNonPercentHighGrade()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 110);
		assertFalse(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(110));
	}
	
	@Test
	public void assignGradeNotTeachingCourse()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor2, _course1, _classYear, _homework1, _student1, 90);
		assertFalse(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(90));
	}
	
	@Test
	public void assignGradeNonValidHomework()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _invalidHomework, _student1, 90);
		assertTrue(_instructor.getGrade(_course1, _classYear, _invalidHomework, _student1) == null);
	}
	
	@Test
	public void assignGradeStudentNotSubmitted()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 90);
		assertFalse(_instructor.getGrade(_course1, _classYear, _homework1, _student1).equals(90));
	}
	
	@Test
	public void assignGradeWrongCourseYear()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear + 2, _homework1, _student1, 90);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework1, _student1) == null);
	}
	
	@Test
	public void assignGradeWrongCourse()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _invalidCourse, _classYear, _homework1, _student1, 90);
		assertTrue(_instructor.getGrade(_invalidCourse, _classYear, _homework1, _student1) == null);
	}
	
	@Test
	public void assignGradeCourse1Not2()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 90);
		assertTrue(_instructor.getGrade(_course2, _classYear, _homework1, _student1) == null);
	}
	
	@Test
	public void assignGradeHomework1Not2()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 90);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework2, _student1) == null);
	}
	
	@Test
	public void assignGradeStudent1Not2()
	{
		_instructor.addHomework(_instructor1, _course1, _classYear, _homework1);
		_student.submitHomework(_student1, _homework1, "answers", _course1, _classYear);
		_instructor.assignGrade(_instructor1, _course1, _classYear, _homework1, _student1, 90);
		assertTrue(_instructor.getGrade(_course1, _classYear, _homework1, _student2) == null);
	}
}
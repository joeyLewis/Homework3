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

public class TestStudent
{
	private IAdmin _admin;
	private IStudent _student;
	
	private String _validCourse;
	private String _invalidCourse;
	private int _classYear;
	
	private String _instructor;
	private IInstructor _instructorImpl;
	
	private int _highCapacity;
	private int _lowCapacity;
	
	private String _validStudent;
	private String _invalidStudent;
	
	private String _homework1;
	private String _homework2;
	private String _answers;
	
	@Before
	public void setup()
	{
		_validCourse = "TestClass-Valid";
		_invalidCourse = "TestClass-Invalid";
		_classYear = 2017;
		
		_instructor = "Instructor";
		_highCapacity = 100;
		_lowCapacity = 1;
		
		_validStudent = "TestStudent-Valid";
		_invalidStudent = "TestStudent-Invalid";
		
		_homework1 = "Homework1";
		_homework2 = "Homework2";
		_answers = "Answers";
		
		_admin = new Admin();
		_student = new Student();
		_instructorImpl = new Instructor();
		
		_admin.createClass(_validCourse, _classYear, _instructor, _highCapacity);
		_admin.createClass(_validCourse, _classYear + 1, _instructor, _highCapacity);
		_instructorImpl.addHomework(_instructor, _validCourse, _classYear, _homework1);
		_instructorImpl.addHomework(_instructor, _validCourse, _classYear, _homework2);
		_instructorImpl.addHomework(_instructor, _validCourse, _classYear, _homework1);
	}
	
	// ---------- Register for class tests ----------
	
	@Test
	public void registerForValidClass()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
	}
	
	@Test
	public void registerForNonExistantClass()
	{
		_student.registerForClass(_validStudent, _invalidCourse, _classYear);
		assertFalse(_student.isRegisteredFor(_validStudent, _invalidCourse, _classYear));
	}
	
	@Test
	public void invalidStudentRegistration()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		assertFalse(_student.isRegisteredFor(_invalidStudent, _validCourse, _classYear));
	}
	
	@Test
	public void registerForFullClass()
	{
		_admin.changeCapacity(_validCourse, _classYear, _lowCapacity);
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.registerForClass(_invalidStudent, _validCourse, _classYear);
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
		assertFalse(_student.isRegisteredFor(_invalidStudent, _validCourse, _classYear));
	}
	
	@Test
	public void registerForCourseWrongYear()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear + 2);
		assertFalse(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
		assertFalse(_student.isRegisteredFor(_validStudent, _validCourse, _classYear + 2));
	}
	
	// ---------- Drop Class Tests ----------
	
	@Test
	public void dropRegisteredCourse()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.dropClass(_validStudent, _validCourse, _classYear);
		assertFalse(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
	}
	
	@Test
	public void dropNonRegisteredCourse()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.dropClass(_validStudent, _invalidCourse, _classYear);
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
	}
	
	@Test
	public void dropCourseWrongStudent()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.dropClass(_invalidStudent, _validCourse, _classYear);
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
	}
	
	@Test
	public void dropCourseNextYear()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear + 1);
		_student.dropClass(_validStudent, _validCourse, _classYear + 1);
		assertFalse(_student.isRegisteredFor(_validStudent, _validCourse, _classYear + 1));
	}
	
	@Test
	public void dropCourseWrongYear()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.dropClass(_validStudent, _validCourse, _classYear + 1);
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
	}
	
	@Test
	public void dropCourseOneYear()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.registerForClass(_validStudent, _validCourse, _classYear + 1);
		_student.dropClass(_validStudent, _validCourse, _classYear);
		assertFalse(_student.isRegisteredFor(_validStudent, _validCourse, _classYear));
		assertTrue(_student.isRegisteredFor(_validStudent, _validCourse, _classYear + 1));
	}
	
	// ---------- Submit Homework Tests ----------
	
	@Test
	public void submitHomework()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.submitHomework(_validStudent, _homework1, _answers, _validCourse, _classYear);
		assertTrue(_student.hasSubmitted(_validStudent, _homework1, _validCourse, _classYear));
	}
	
	@Test
	public void submitHomeworkNotRegistered()
	{
		_student.submitHomework(_validStudent, _homework1, _answers, _validCourse, _classYear);
		assertFalse(_student.hasSubmitted(_validStudent, _homework1, _validCourse, _classYear));
	}
	
	@Test
	public void submitHomeworkFutureCourse()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear + 1);
		_student.submitHomework(_validStudent, _homework1, _answers, _validCourse, _classYear + 1);
		assertFalse(_student.hasSubmitted(_validStudent, _homework1, _validCourse, _classYear));
	}
	
	@Test
	public void submitHomeworkOneNotTwo()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.submitHomework(_validStudent, _homework1, _answers, _validCourse, _classYear);
		assertFalse(_student.hasSubmitted(_validStudent, _homework2, _validCourse, _classYear));
	}
	
	@Test
	public void submitInvalidCourse()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.submitHomework(_validStudent, _homework1, _answers, _invalidCourse, _classYear);
		assertFalse(_student.hasSubmitted(_validStudent, _homework1, _invalidCourse, _classYear));
		assertFalse(_student.hasSubmitted(_validStudent, _homework1, _validCourse, _classYear));
	}
	
	@Test
	public void submitInvalidStudent()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.submitHomework(_validStudent, _homework1, _answers, _validCourse, _classYear);
		assertFalse(_student.hasSubmitted(_invalidStudent, _homework1, _validCourse, _classYear));
	}
	
	@Test
	public void submitInvalidHomework()
	{
		_student.registerForClass(_validStudent, _validCourse, _classYear);
		_student.submitHomework(_validStudent, "Homework-Invalid", _answers, _validCourse, _classYear);
		assertFalse(_student.hasSubmitted(_validStudent, "Homework-Invalid", _validCourse, _classYear));
	}
	
}

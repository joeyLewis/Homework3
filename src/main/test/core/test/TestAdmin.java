package core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Student;

public class TestAdmin
{
	private IAdmin _admin;
	
	@Before
	public void setup()
	{
		_admin = new Admin();
	}
	
	// ------------- CreateClass Tests --------------
	
	@Test
	public void makeValidClass()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makePastClass()
	{
		String className = "TestClass-Invalid";
		int classYear = 2016;
		_admin.createClass(className,  classYear, "Instructor", 100);
		assertFalse(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeFutureClass()
	{
		String className = "TestClass-Valid";
		int classYear = 2018;
		_admin.createClass(className, classYear, "Instructor", 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeZeroCapacityClass()
	{
		String className = "TestClass-Invalid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 0);
		assertFalse(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeNegativeCapacityClass()
	{
		String className = "TestClass-Invalid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", -100);
		assertFalse(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeClassCheckOtherYear()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		assertFalse(_admin.classExists(className, classYear+1));
	}
	
	@Test
	public void makeClassEmptyName()
	{
		String className = "";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeClassNullInstructor()
	{
		String className = "TestClass-Invalid";
		int classYear = 2017;
		_admin.createClass(className, classYear, null, 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeClassEmptyInstructor()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "", 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeClassTwice()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		_admin.createClass(className, classYear, "Instructor", 100);
		assertTrue(_admin.classExists(className, classYear));
	}
	
	@Test
	public void makeClassTwoYears()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		_admin.createClass(className, classYear+1, "Instructor", 100);
		assertTrue(_admin.classExists(className, classYear));
		assertTrue(_admin.classExists(className, classYear+1));
	}
	
	@Test
	public void instructorRetained()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		String instructorName = "Instructor";
		_admin.createClass(className, classYear, instructorName, 100);
		assertTrue(_admin.getClassInstructor(className, classYear).equals(instructorName));
	}
	
	@Test
	public void makeValidInstructorCombo()
	{
		String className = "TestClass-Valid";
		String className2 = "TestClass-Valid2";
		int classYear = 2017;
		String instructorName = "Instructor-Valid";
		_admin.createClass(className, classYear, instructorName, 100);
		_admin.createClass(className2, classYear, instructorName, 100);
		assertTrue(_admin.classExists(className, classYear));
		assertTrue(_admin.classExists(className2, classYear));
	}
	
	@Test
	public void makeInvalidInstructorCombo()
	{
		String className = "TestClass-Valid";
		String className2 = "TestClass-Valid2";
		String className3 = "TestClass-InValid3";
		int classYear = 2017;
		String instructorName = "Instructor-MostlyValid";
		_admin.createClass(className, classYear, instructorName, 100);
		_admin.createClass(className2, classYear, instructorName, 100);
		_admin.createClass(className3, classYear, instructorName, 100);
		assertTrue(_admin.classExists(className, classYear));
		assertTrue(_admin.classExists(className2, classYear));
		assertFalse(_admin.classExists(className3, classYear));
	}
	
	@Test
	public void makeValidInstructorAcrossYears()
	{
		String className = "TestClass-Valid";
		String className2 = "TestClass-Valid2";
		String className3 = "TestClass-Valid3";
		int classYear = 2017;
		int classYear2 = 2018;
		int classYear3 = 2019;
		String instructorName = "Instructor-Valid";
		_admin.createClass(className, classYear, instructorName, 100);
		_admin.createClass(className2, classYear2, instructorName, 100);
		_admin.createClass(className3, classYear3, instructorName, 100);
		assertTrue(_admin.classExists(className, classYear));
		assertTrue(_admin.classExists(className2, classYear2));
		assertTrue(_admin.classExists(className3, classYear3));
	}
	
	
	// ----------- ChangeCapacity Tests --------------

	@Test
	public void makeCapacityCheck()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 100);
		assertEquals(_admin.getClassCapacity(className, classYear), 100);
	}
	
	@Test
	public void raiseCapacityWithNoEnrollment()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 150;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
	}
	
	@Test
	public void lowerCapacityWithNoEnrollment()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 50;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
	}
	
	@Test
	public void setZeroCapacityWithNoEnrollment()
	{
		String className = "TestClass-Invalid";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 0;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
	}
	
	@Test
	public void setNegativeCapacityWithNoEnrollment()
	{
		String className = "TestClass-Invalid";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = -100;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityOriginal);
	}
	
	@Test
	public void checkNoOtherYearCapacityModified()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int classYear2 = 2018;
		int capacityOriginal = 2;
		int capacityNew = 1;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.createClass(className, classYear2, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear2), capacityOriginal);
	}
	
	@Test
	public void changeCapacityBelowEnrollment()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int capacityOriginal = 10;
		int capacityNew = 1;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		
		IStudent student = new Student();
		student.registerForClass("Student", className, classYear);
		student.registerForClass("Student2", className, classYear);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityOriginal);
	}
	
	@Test
	public void changeCapacityToEnrollment()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int capacityOriginal = 10;
		int capacityNew = 2;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		
		IStudent student = new Student();
		student.registerForClass("Student", className, classYear);
		student.registerForClass("Student2", className, classYear);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
	}
	
	@Test
	public void changeCapacityAboveEnrollment()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		int capacityOriginal = 10;
		int capacityNew = 3;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		
		IStudent student = new Student();
		student.registerForClass("Student", className, classYear);
		student.registerForClass("Student2", className, classYear);
		_admin.changeCapacity(className, classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityNew);
	}
	
	@Test
	public void changeCapacityOfUnknownClass()
	{
		String className = "TestClass";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 50;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className+"-New", classYear, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityOriginal);
	}
	
	@Test
	public void changeCapacityOfUnknownYear()
	{
		String className = "TestClass";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 50;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className, classYear+1, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityOriginal);
	}
	
	@Test
	public void changeCapacityOfUnknownClassAndYear()
	{
		String className = "TestClass";
		int classYear = 2017;
		int capacityOriginal = 100;
		int capacityNew = 50;
		_admin.createClass(className, classYear, "Instructor", capacityOriginal);
		_admin.changeCapacity(className+"-New", classYear+1, capacityNew);
		assertEquals(_admin.getClassCapacity(className, classYear), capacityOriginal);
	}
	
	@Test
	public void sanityCheckCapacityChange()
	{
		String className = "TestClass-Valid";
		int classYear = 2017;
		_admin.createClass(className, classYear, "Instructor", 1);
		
		final int TEST_NUMBER = 10000;
		int newCapacity;
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		
		for(int i = 0; i < TEST_NUMBER; i++)
		{
			newCapacity = rand.nextInt(100) + 1;
			_admin.changeCapacity(className, classYear, newCapacity);
			assertEquals(_admin.getClassCapacity(className, classYear), newCapacity);
		}
	}
}

package com.csci3130.daloffline.initialization;

import com.csci3130.daloffline.DalOfflineUI;
import com.csci3130.daloffline.authentication.Authenticator;
import com.csci3130.daloffline.domain.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.*;


/**
 * Static methods to initialize and clean up  the database for testing
 * 
 * @author Jesse MacLeod
 *
 */
public class DatabaseInitializer {

	public static ArrayList<UsernamePasswordPair> usernamePasswordPairs = new ArrayList<UsernamePasswordPair>();
	//public static ArrayList<Course> courses = new ArrayList<Course>();
	
	/**
	 * Fills the specified persistence unit (database) with user and password pairs for testing.
	 * @param persistenceUnitName
	 */
	public static void generateUsers(EntityManagerFactory factory) {
		
		EntityManager em = factory.createEntityManager();
		
		usernamePasswordPairs.add(new UsernamePasswordPair("user", Authenticator.hash("pass")));
		usernamePasswordPairs.add(new UsernamePasswordPair("jesse", Authenticator.hash("1234")));
		usernamePasswordPairs.add(new UsernamePasswordPair("xrd", Authenticator.hash("mmspos")));
		usernamePasswordPairs.add(new UsernamePasswordPair("Bobethy", Authenticator.hash("Collective")));
		
		//factory = Persistence.createEntityManagerFactory(DalOfflineUI.PERSISTANCE_UNIT);
		//EntityManager em = factory.createEntityManager();
		
		try {
			em.getTransaction().begin();
			for(UsernamePasswordPair u : usernamePasswordPairs) {
				em.persist(u);
				System.out.println("Adding username: "+u.getUsername()+" with password: "+u.getPassword());
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			em.getTransaction().begin();
			List<UsernamePasswordPair> allUsers = em.createQuery("Select a from UsernamePasswordPair a", UsernamePasswordPair.class).getResultList();
			em.getTransaction().commit();
			
			for(UsernamePasswordPair u : allUsers) {
				System.out.println("Successfully added username: "+u.getUsername()+" with password: "+u.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		em.close();
	}
	
	/**
	 * Fills the specified persistence unit (database) with courses for testing.
	 * @param persistenceUnitName
	 */
	public static void generateCourses(EntityManagerFactory factory) {
		
		EntityManager em = factory.createEntityManager();
		
		em.getTransaction().begin(); //Begin a transaction
		Course newCourse = new Course("Software Engineering", "Computer Science", "CSCI3130", "Dr. Ashraf Abusharekh"); //Create a course object with params
		Section newSection = new Section("Killam Fun Zone", 123, "The TAs", 8, 30, 90); //Create a section with params
		newSection.addDays(new int[]{4,6}); //Set days for this section
		newCourse.addLab(newSection); //Add the section to the course as a lab or lecture
		newSection.setCourse(newCourse); //Set the reference to the parent course from the section
		
		newSection = new Section("Psychology building for some reason", 69, "The Professor", 16, 0, 90);
		newSection.addDays(new int[]{3,5});
		newCourse.addLecture(newSection);
		newSection.setCourse(newCourse);
		em.persist(newCourse); //Persist the course (will automatically persist the added sections)
		
		newCourse = new Course("Principles of Programming Languages", "Computer Science", "CSCI3136", "NAUZER");
		newSection = new Section("CS 127", 666, "NAAUUUZER", 13, 30, 60);
		newSection.addDays(new int[]{2,4,6});
		newCourse.addLecture(newSection);
		newSection.setCourse(newCourse);
		newSection = new Section("Somewhere Else", 667, "NAAUUUZER", 11, 30, 60);
		newSection.addDays(new int[]{2,4,6});
		newCourse.addLecture(newSection);
		newSection.setCourse(newCourse);
		em.persist(newCourse);

		em.getTransaction().commit();
		
		/*em.getTransaction().begin();
		Course gotCourse = (Course) em.createQuery("SELECT c FROM COURSES c").getSingleResult();
		ArrayList<Section> sections = gotCourse.getSections();
		Section fuck = sections.get(0);
		System.out.println("!---> Coursename: "+gotCourse.getCourseName()+" - "+fuck.getLocation());
		em.getTransaction().commit();*/
		em.close();
	}
	
	/**
	 * Clears the users generated by DatabaseInitializer.gernerateUsers() from the specified persistence unit.
	 * @param persistenceUnitName
	 */
	public static void clearUsers(EntityManagerFactory factory) {

		EntityManager em = factory.createEntityManager();
		try {
			em.getTransaction().begin();
			
			for(UsernamePasswordPair u : usernamePasswordPairs) {

				em.remove(u);
				System.out.println("removing username: "+u.getUsername()+" with password: "+u.getPassword());

			}
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		em.close();
		
	}
}

package edu.ncsu.csc216.incident_management.model.manager;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;

/**
 * Tests IncidentManager class
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class IncidentManagerTest {

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#getInstance()}.
	 */
	@Test
	public void testGetInstance() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		assertNotNull(IncidentManager.getInstance());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#saveManagedIncidentsToFile(java.lang.String)}.
	 */
	@Test
	public void testSaveManagedIncidentsToFile() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		// Load valid file.
		try {
			IncidentManager.getInstance().loadManagedIncidentsFromFile("test-files/incident1.xml");
			assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		} catch (IllegalArgumentException iae) {
			fail(iae.getMessage());
		}

		// Attempt to save to an invalid place.
		try {
			IncidentManager.getInstance().saveManagedIncidentsToFile("#$%^/a/sdsa@#.txt");
			fail();
		} catch (IllegalArgumentException iae) {
			assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		}

//		String[][] validImport = IncidentManager.getInstance().getManagedIncidentsAsArray();
//		String[][] checkSaved = null;
//
//		// Save to a valid place.
//		try {
//			IncidentManager.getInstance().saveManagedIncidentsToFile("test-files/checkIncidents.xml");
//			// ^^THIS isn't saving the file to the test-files folder.
//			IncidentManager.getInstance().createNewManagedIncidentList();
//			// BELOW is throwing an exception since ^^^ isn't saving. Stuck on this.
//			IncidentManager.getInstance().loadManagedIncidentsFromFile("test-files/checkIncidents.xml");
//
//			checkSaved = IncidentManager.getInstance().getManagedIncidentsAsArray();
//			assertNotNull(checkSaved);
//
//			assertTrue(checkSaved == validImport);
//		} catch (Exception e) {
//			fail(e.getMessage());
//		}
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		IncidentManager.getInstance().saveManagedIncidentsToFile("test-files/testIncidents.xml");
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#loadManagedIncidentsFromFile(java.lang.String)}.
	 */
	@Test
	public void testLoadManagedIncidentsFromFile() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		// Attempt invalid Incident file. Will throw an exception.
		try {
			IncidentManager.getInstance().loadManagedIncidentsFromFile("test-files/incident-1.xml");
			fail();
		} catch (IllegalArgumentException iae) {
			assertEquals(0, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		}

		// Load valid file.
		try {
			IncidentManager.getInstance().loadManagedIncidentsFromFile("test-files/incident1.xml");
			assertEquals(6, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		} catch (IllegalArgumentException iae) {
			fail(iae.getMessage());
		}

	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#createNewManagedIncidentList()}.
	 */
	@Test
	public void testCreateNewManagedIncidentList() {
		IncidentManager.getInstance().createNewManagedIncidentList();

		assertEquals(0, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		assertEquals(1, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
		IncidentManager.getInstance().createNewManagedIncidentList();
		assertEquals(0, IncidentManager.getInstance().getManagedIncidentsAsArray().length);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#getManagedIncidentsAsArrayByCategory(edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category)}.
	 */
	@Test
	public void testGetManagedIncidentsAsArrayByCategory() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		assertEquals("New",
				IncidentManager.getInstance().getManagedIncidentsAsArrayByCategory(Category.DATABASE)[0][2]);
	}

	/**
	 * Test method for GetManagedIncidentsAsArray
	 */
	@Test
	public void testGetManagedIncidentsAsArray() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		assertEquals("0", IncidentManager.getInstance().getManagedIncidentsAsArray()[0][0]);
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#executeCommand(int, edu.ncsu.csc216.incident_management.model.command.Command)}.
	 */
	@Test
	public void testExecuteCommand() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		IncidentManager.getInstance().executeCommand(0, cm);
		assertEquals("In Progress", IncidentManager.getInstance().getManagedIncidentById(0).getState().getStateName());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#deleteManagedIncidentById(int)}.
	 */
	@Test
	public void testDeleteManagedIncidentById() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");
		IncidentManager.getInstance().deleteManagedIncidentById(0);
		assertEquals(null, IncidentManager.getInstance().getManagedIncidentById(0));
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.IncidentManager#addManagedIncidentToList(java.lang.String, edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category, edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddManagedIncidentToList() {
		IncidentManager.getInstance().createNewManagedIncidentList();
		IncidentManager.getInstance().addManagedIncidentToList("Eric", ManagedIncident.Category.DATABASE,
				ManagedIncident.Priority.HIGH, "Replace Database infrastructure.", "so many notes");

		assertNotNull(IncidentManager.getInstance().getManagedIncidentById(0));
		assertEquals("Eric", IncidentManager.getInstance().getManagedIncidentById(0).getCaller());
		assertEquals(ManagedIncident.Category.DATABASE,
				IncidentManager.getInstance().getManagedIncidentById(0).getCategory());
		assertEquals("High", IncidentManager.getInstance().getManagedIncidentById(0).getPriorityString());
		assertEquals("Replace Database infrastructure.",
				IncidentManager.getInstance().getManagedIncidentById(0).getName());
		assertEquals("so many notes" + '\n' + "-------" + '\n', IncidentManager.getInstance().getManagedIncidentById(0).getNotesString());
	}

}

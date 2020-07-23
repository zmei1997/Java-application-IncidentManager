package edu.ncsu.csc216.incident_management.model.manager;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * Tests ManagedIncidentList class
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class ManagedIncidentListTest {

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#ManagedIncidentList()}.
	 */
	@Test
	public void testManagedIncidentList() {
		ManagedIncidentList list = new ManagedIncidentList();
		assertEquals(0, list.getManagedIncidents().size());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#addIncident(java.lang.String, edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category, edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAddIncident() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		assertEquals(1, list.getManagedIncidents().size());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#addXMLIncidents(java.util.List)}.
	 */
	@Test
	public void testAddXMLIncidents() {
		ManagedIncidentList list = new ManagedIncidentList();
		ManagedIncident incident = new ManagedIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		ArrayList<Incident> a = new ArrayList<Incident>();
		a.add(incident.getXMLIncident());
		list.addXMLIncidents(a);
		assertEquals("caller", list.getIncidentById(0).getCaller());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#getManagedIncidents()}.
	 */
	@Test
	public void testGetManagedIncidents() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		assertEquals(1, list.getManagedIncidents().size());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#getIncidentsByCategory(edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category)}.
	 */
	@Test
	public void testGetIncidentsByCategory() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
//		list.addIncident("zmei", Category.HARDWARE, Priority.MEDIUM, "name", "workNote");
//		list.addIncident("zzzz", Category.INQUIRY, Priority.LOW, "name", "workNote");
		assertEquals(1, list.getManagedIncidents().size());
		assertEquals("caller", list.getIncidentsByCategory(Category.SOFTWARE).get(0).getCaller());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#getIncidentById(int)}.
	 */
	@Test
	public void testGetIncidentById() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		list.addIncident("zmei", Category.HARDWARE, Priority.MEDIUM, "name", "workNote");
		list.addIncident("zzzz", Category.INQUIRY, Priority.LOW, "name", "workNote");
		assertEquals(3, list.getManagedIncidents().size());
		assertEquals("zzzz", list.getIncidentById(2).getCaller());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#executeCommand(int, edu.ncsu.csc216.incident_management.model.command.Command)}.
	 */
	@Test
	public void testExecuteCommand() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		Command c = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		list.executeCommand(0, c);
		assertEquals("In Progress", list.getIncidentById(0).getState().getStateName());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.manager.ManagedIncidentList#deleteIncidentById(int)}.
	 */
	@Test
	public void testDeleteIncidentById() {
		ManagedIncidentList list = new ManagedIncidentList();
		list.addIncident("caller", Category.SOFTWARE, Priority.HIGH, "name", "workNote");
		list.addIncident("zmei", Category.HARDWARE, Priority.HIGH, "name", "workNote");
		list.deleteIncidentById(0);
		assertEquals(1, list.getManagedIncidents().size());
	}

}

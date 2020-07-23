package edu.ncsu.csc216.incident_management.model.manager;

import edu.ncsu.csc216.incident.io.IncidentIOException;
import edu.ncsu.csc216.incident.io.IncidentReader;
import edu.ncsu.csc216.incident.io.IncidentWriter;
import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * The class is called IncidentManager. It implements the Singleton design
 * pattern, works with the XML files that contain the ManagedIncidents in a file
 * when the application is not in use., and also provides information to the GUI
 * through methods. Controls the creation and modification of (potentially many)
 * ManagedIncidentLists.
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class IncidentManager {

	/**
	 * The list containing the ManagedIncidents.
	 */
	private ManagedIncidentList incidentList;

	/**
	 * The only Instance of IncidentManager.
	 */
	private static IncidentManager singleton = new IncidentManager();

	/**
	 * Prevents other class from creating another instance of IncidentManager();
	 */
	private IncidentManager() {
		incidentList = new ManagedIncidentList();
	}

	/**
	 * getInstance of IncidentManager
	 * 
	 * @return the instance of IncidentManager
	 */
	public static IncidentManager getInstance() {
		return singleton;
	}

	/**
	 * Save the ManagedIncidents to a file.
	 * 
	 * @param fileName The name of the saved file.
	 */
	public void saveManagedIncidentsToFile(String fileName) {
		IncidentWriter xmlWriter = new IncidentWriter(fileName);
		try {
			for (ManagedIncident i : incidentList.getManagedIncidents()) {
				Incident converted = i.getXMLIncident();
				xmlWriter.addItem(converted);
			}
			xmlWriter.marshal();
		} catch (IncidentIOException iioe) {
			throw new IllegalArgumentException(iioe.getMessage());
		}
	}

	/**
	 * Load ManagedIncidents from a file.
	 * 
	 * @param fileName The name of the file that will be used to load the Incidents.
	 */
	public void loadManagedIncidentsFromFile(String fileName) {
		try {
			IncidentReader xmlReader = new IncidentReader(fileName);
			incidentList.addXMLIncidents(xmlReader.getIncidents());
		} catch (IncidentIOException iioe) {
			throw new IllegalArgumentException(iioe.getMessage());
		}
	}

	/**
	 * Create an entirely new ManagedIncidentList. Will remove all current
	 * MangedIncidents.
	 */
	public void createNewManagedIncidentList() {
		incidentList = new ManagedIncidentList();
	}

	/**
	 * Return the ManagedIncidents as an Array.
	 * 
	 * @return a 2D String Array with information concerning the ManagedIncidents.
	 */
	public String[][] getManagedIncidentsAsArray() {
		if (incidentList.getManagedIncidents().size() == 0)
			return new String[0][0];

		String[][] incidentInfo = new String[incidentList.getManagedIncidents().size()][5];
		int incidentCounter = 0;
		for (ManagedIncident infoMe : incidentList.getManagedIncidents()) {
			incidentInfo[incidentCounter][0] = Integer.toString(infoMe.getIncidentId());
			incidentInfo[incidentCounter][1] = infoMe.getCategoryString();
			incidentInfo[incidentCounter][2] = infoMe.getState().getStateName();
			incidentInfo[incidentCounter][3] = infoMe.getPriorityString();
			incidentInfo[incidentCounter][4] = infoMe.getName();

			incidentCounter++;
		}

		return incidentInfo;
	}

	/**
	 * Return the ManagedIncidents as an Array by a certain Category..
	 * 
	 * @param c The Category that will sort the Incidents by.
	 * @return a 2D String Array with information concerning the ManagedIncidents.
	 */
	public String[][] getManagedIncidentsAsArrayByCategory(Category c) {
		if (c == null)
			throw new IllegalArgumentException();
		else if (incidentList.getIncidentsByCategory(c).size() == 0)
			return new String[0][0];

		String[][] incidentInfo = new String[incidentList.getIncidentsByCategory(c).size()][5];
		int incidentCounter = 0;
		for (ManagedIncident infoMe : incidentList.getIncidentsByCategory(c)) {
			incidentInfo[incidentCounter][0] = Integer.toString(infoMe.getIncidentId());
			incidentInfo[incidentCounter][1] = infoMe.getCategoryString();
			incidentInfo[incidentCounter][2] = infoMe.getState().getStateName();
			incidentInfo[incidentCounter][3] = infoMe.getPriorityString();
			incidentInfo[incidentCounter][4] = infoMe.getName();

			incidentCounter++;
		}
		return incidentInfo;
	}

	/**
	 * Return a ManagedIncident by it's ID.
	 * 
	 * @param incidentId the id of a particular Incident
	 * @return The ManagedIncident.
	 */
	public ManagedIncident getManagedIncidentById(int incidentId) {
		return incidentList.getIncidentById(incidentId);
	}

	/**
	 * Execute the following command to the incident with the corresponding ID.
	 * 
	 * @param id the id of Incident you want to run a command on.
	 * @param c  the command
	 */
	public void executeCommand(int id, Command c) {
		incidentList.executeCommand(id, c);
	}

	/**
	 * Delete a certain Incident by an ID.
	 * 
	 * @param incidentId the id of Incident to be deleted.
	 */
	public void deleteManagedIncidentById(int incidentId) {
		incidentList.deleteIncidentById(incidentId);
	}

	/**
	 * Add a ManagedIncident To the list.
	 * 
	 * @param caller   the caller
	 * @param category the category of the incident
	 * @param priority the priority of the incident
	 * @param name     the name
	 * @param workNote the work notes
	 */
	public void addManagedIncidentToList(String caller, Category category, Priority priority, String name,
			String workNote) {
		incidentList.addIncident(caller, category, priority, name, workNote);
	}

}

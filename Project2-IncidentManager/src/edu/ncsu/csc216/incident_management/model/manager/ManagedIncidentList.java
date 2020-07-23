package edu.ncsu.csc216.incident_management.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * Maintains a List of ManagedIncidents. Can add a ManagedIncident to the list,
 * add a List of Incidents from the XML library to the list, remove a
 * ManagedIncident from the list, search for and get a ManagedIncident in the
 * list by id, update a ManagedIncident in the list through execution of a
 * Command, and return the entire list or sublists of itself (for example, the
 * ManagedIncidentList can return a List of ManagedIncidents filtered by
 * Category).
 * 
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class ManagedIncidentList {

	/**
	 * the list of Incidents
	 */
	private ArrayList<ManagedIncident> incidents;

	/**
	 * Create a new list of ManagedIncidents and sets the current incident ID to 0.
	 */
	public ManagedIncidentList() {
		incidents = new ArrayList<ManagedIncident>();
		ManagedIncident.setCounter(0);
	}

	/**
	 * Add an ManagedIncident to the list.
	 * 
	 * @param caller   the caller
	 * @param category the category of the incident
	 * @param priority the priority of the incident
	 * @param name     the name
	 * @param workNote the work notes
	 * @return the number of added incidents
	 */
	public int addIncident(String caller, Category category, Priority priority, String name, String workNote) {
		ManagedIncident incident = new ManagedIncident(caller, category, priority, name, workNote);
		incidents.add(incident);
		return incident.getIncidentId();
	}

	/**
	 * Add ManagedIncidents that are contained in a XML file.
	 * 
	 * @param xmlIncidents The XML file.
	 */
	public void addXMLIncidents(List<Incident> xmlIncidents) {
		int count = 0;
		for (int i = 0; i < xmlIncidents.size(); i++) {
			if (count < xmlIncidents.get(i).getId()) {
				count = xmlIncidents.get(i).getId();
			}
			incidents.add(new ManagedIncident(xmlIncidents.get(i)));
		}
		ManagedIncident.setCounter(count + 1);
	}

	/**
	 * Get the list of Managed Incidents.
	 * 
	 * @return a list of Managed Incidents
	 */
	public ArrayList<ManagedIncident> getManagedIncidents() {
		return incidents;
	}

	/**
	 * Get the list of Managed Incidents by a certain category.
	 * 
	 * @param category the category of incidents
	 * @return a list of Managed Incidents
	 */
	public ArrayList<ManagedIncident> getIncidentsByCategory(Category category) {
		if (category == null) {
			throw new IllegalArgumentException();
		}
		ArrayList<ManagedIncident> list = new ArrayList<ManagedIncident>();
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getCategory().equals(category)) {
				list.add(incidents.get(i));
			}
		}
		return list;
	}

	/**
	 * Get an Incident by it's ID.
	 * 
	 * @param byId the id of incident
	 * @return the Incident with the corresponding ID
	 */
	public ManagedIncident getIncidentById(int byId) {
		ManagedIncident incident = null;
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getIncidentId() == byId) {
				incident = incidents.get(i);
			}
		}
		return incident;
	}

	/**
	 * Execute the following command to the incident with the corresponding ID.
	 * 
	 * @param id      the id of Incident you want to run a command on.
	 * @param thisCmd the command
	 */
	public void executeCommand(int id, Command thisCmd) {
		for (ManagedIncident mi : incidents) {
			if (mi.getIncidentId() == id) {
				mi.update(thisCmd);
				return;
			}

		}
	}

	/**
	 * Delete a certain Incident by an ID.
	 * 
	 * @param id the id of Incident to be deleted.
	 */
	public void deleteIncidentById(int id) {
		for (int i = 0; i < incidents.size(); i++) {
			if (incidents.get(i).getIncidentId() == id) {
				incidents.remove(i);
			}
		}
	}
}

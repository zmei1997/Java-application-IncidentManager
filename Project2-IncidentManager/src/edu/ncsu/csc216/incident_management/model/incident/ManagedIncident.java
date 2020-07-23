package edu.ncsu.csc216.incident_management.model.incident;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident.xml.WorkNotes;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;

/**
 * Represents an incident tracked by our IncidentManager. Each Instance knows
 * its id, caller, category, state, priority, owner, name, HoldReason,
 * ChangeRequest, ResolutionCode, CancellationCode, notes, and state.
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class ManagedIncident {

	/**
	 * Each Incident has a Category concerning what the Incident is about.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum Category {
	/**
	 * A user would like inquiry about some information.
	 */
	INQUIRY,
	/**
	 * A user encountered a Software incident.
	 */
	SOFTWARE,
	/**
	 * A user encountered a Hardware incident.
	 */
	HARDWARE,
	/**
	 * A user encountered a Network incident.
	 */
	NETWORK,
	/**
	 * A user encountered a Database incident.
	 */
	DATABASE
	}

	/**
	 * Each Incident has a type of Priority so the IT Department can prioritize
	 * which Incident to focus on first.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum Priority {
		/**
		 * This Incident is the extremely important.
		 */
		URGENT,
		/**
		 * This Incident is important.
		 */
		HIGH,
		/**
		 * This Incident is somewhat important.
		 */
		MEDIUM,
		/**
		 * This Incident is the least important.
		 */
		LOW
	}

	/**
	 * An incident enters the incident management state model in the New state.
	 * Incidents in the New state should be triaged.
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 *
	 */
	public class NewState implements IncidentState {

		@Override
		public void updateState(Command command) {
			if (command.getCommand() == Command.CommandValue.INVESTIGATE) {
				owner = command.getOwnerId();

				notes.add(command.getWorkNote());

				state = inProgressState;
			} else if (command.getCommand() == Command.CommandValue.CANCEL) {
				notes.add(command.getWorkNote());

				cancellationCode = command.getCancellationCode();
				state = canceledState;
			} else {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public String getStateName() {
			return NEW_NAME;
		}

	}

	/**
	 * An incident In Progress is under investigation by the assigned incident
	 * management personnel. The incident moves out of the In Progress state when
	 * the incident management personnel records the results of their investigation
	 * in this state.
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 *
	 */
	public class InProgressState implements IncidentState {

		@Override
		public void updateState(Command command) {
			if (command.getCommand() == Command.CommandValue.HOLD) {
				notes.add(command.getWorkNote());

				state = onHoldState;
				onHoldReason = command.getOnHoldReason();
			} else if (command.getCommand() == Command.CommandValue.RESOLVE) {
				notes.add(command.getWorkNote());

				state = resolvedState;
				resolutionCode = command.getResolutionCode();
			} else if (command.getCommand() == Command.CommandValue.CANCEL) {
				// ADDED
				notes.add(command.getWorkNote());

				state = canceledState;
				cancellationCode = command.getCancellationCode();
			} else {
				throw new UnsupportedOperationException();
			}

		}

		@Override
		public String getStateName() {
			return IN_PROGRESS_NAME;
		}

	}

	/**
	 * An incident In Hold is awaiting information from the caller, system
	 * developers, or a vendor. The incident moves out of the On Hold state when the
	 * stakeholder responds.
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 *
	 */
	public class OnHoldState implements IncidentState {

		@Override
		public void updateState(Command command) {
			if (command.getCommand() == Command.CommandValue.REOPEN) {
				notes.add(command.getWorkNote());
				if (onHoldReason.equals(Command.OnHoldReason.AWAITING_CHANGE)) {
					changeRequest = command.getWorkNote();
				}

				// Remove the holdReason when it LEAVES this state.
				onHoldReason = null;
				state = inProgressState;
			} else if (command.getCommand() == Command.CommandValue.RESOLVE) {
				state = resolvedState;
				resolutionCode = command.getResolutionCode();

				notes.add(command.getWorkNote());
				if (onHoldReason.equals(Command.OnHoldReason.AWAITING_CHANGE)) {
					changeRequest = command.getWorkNote();
				}

				// Remove the holdReason when it LEAVES this state.
				onHoldReason = null;
			} else if (command.getCommand() == Command.CommandValue.CANCEL) {
				notes.add(command.getWorkNote());

				state = canceledState;
				cancellationCode = command.getCancellationCode();

				// Remove the holdReason when it LEAVES this state.
				onHoldReason = null;
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return ON_HOLD_NAME;
		}

	}

	/**
	 * An incident in the Resolved state has been fixed and is waiting for
	 * confirmation of the resolution.
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 *
	 */
	public class ResolvedState implements IncidentState {

		@Override
		public void updateState(Command command) {
			if (command.getCommand() == Command.CommandValue.HOLD) {
				notes.add(command.getWorkNote());

				state = onHoldState;
				resolutionCode = null;
				onHoldReason = command.getOnHoldReason();
			} else if (command.getCommand() == Command.CommandValue.REOPEN) {
				notes.add(command.getWorkNote());

				state = inProgressState;
				resolutionCode = null;
			} else if (command.getCommand() == Command.CommandValue.CONFIRM) {
				notes.add(command.getWorkNote());

				state = closedState;
			} else if (command.getCommand() == Command.CommandValue.CANCEL) {
				notes.add(command.getWorkNote());

				state = canceledState;
				resolutionCode = null;
				cancellationCode = command.getCancellationCode();
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return RESOLVED_NAME;
		}

	}

	/**
	 * An incident in the Closed state has been resolved and the resolution has been
	 * confirmed.
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 */
	public class ClosedState implements IncidentState {
		@Override
		public void updateState(Command command) {
			if (command.getCommand() == Command.CommandValue.REOPEN) {
				notes.add(command.getWorkNote());

				// Once NOT cancelled, remove the code.
				resolutionCode = null;
				state = inProgressState;
			} else {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return CLOSED_NAME;
		}

	}

	/**
	 * An incident in the Canceled state is either a duplicate incident, unnecessary
	 * to resolve, or not an incident. Once an incident is in the Canceled state,
	 * the incident cannot leave (the user would have to report a new incident if
	 * needed).
	 * 
	 * @author Zhongxiao Mei
	 * @author Dennis Sarsozo
	 *
	 */
	public class CanceledState implements IncidentState {

		@Override
		public void updateState(Command command) {
			// Once an incident is in the Canceled state, the incident cannot leave (the
			// user would have to report a new incident if needed).
			if (command.getCommand() == Command.CommandValue.CANCEL
					|| command.getCommand() == Command.CommandValue.CONFIRM
					|| command.getCommand() == Command.CommandValue.HOLD
					|| command.getCommand() == Command.CommandValue.INVESTIGATE
					|| command.getCommand() == Command.CommandValue.REOPEN
					|| command.getCommand() == Command.CommandValue.RESOLVE) {
				owner = null;
			}

			if (command.getCommand() == Command.CommandValue.CANCEL
					|| command.getCommand() == Command.CommandValue.CONFIRM
					|| command.getCommand() == Command.CommandValue.HOLD
					|| command.getCommand() == Command.CommandValue.INVESTIGATE
					|| command.getCommand() == Command.CommandValue.REOPEN
					|| command.getCommand() == Command.CommandValue.RESOLVE) {
				throw new UnsupportedOperationException();
			}
		}

		@Override
		public String getStateName() {
			return CANCELED_NAME;
		}

	}

	/**
	 * Name used for the Category.INQUIRY
	 */
	public static final String C_INQUIRY = "Inquiry";
	/**
	 * Name used for the Category.SOFTWARE
	 */
	public static final String C_SOFTWARE = "Software";
	/**
	 * Name used for the Category.HARDWARE
	 */
	public static final String C_HARDWARE = "Hardware";
	/**
	 * Name used for the Category.NETWORK
	 */
	public static final String C_NETWORK = "Network";
	/**
	 * Name used for the Category.DATABASE
	 */
	public static final String C_DATABASE = "Database";
	/**
	 * Name used for the Priority.URGENT
	 */
	public static final String P_URGENT = "Urgent";
	/**
	 * Name used for the Priority.HIGH
	 */
	public static final String P_HIGH = "High";
	/**
	 * Name used for the Priority.MEDIUM
	 */
	public static final String P_MEDIUM = "Medium";
	/**
	 * Name used for the Priority.LOW
	 */
	public static final String P_LOW = "Low";
	/**
	 * Name used for the NewState class.
	 */
	public static final String NEW_NAME = "New";
	/**
	 * Name used for the InProgressState class.
	 */
	public static final String IN_PROGRESS_NAME = "In Progress";
	/**
	 * Name used for the OnHoldState class.
	 */
	public static final String ON_HOLD_NAME = "On Hold";
	/**
	 * Name used for the ResolvedState class.
	 */
	public static final String RESOLVED_NAME = "Resolved";
	/**
	 * Name used for the ClosedState class.
	 */
	public static final String CLOSED_NAME = "Closed";
	/**
	 * Name used for the CanceledState class.
	 */
	public static final String CANCELED_NAME = "Canceled";

	/**
	 * Unique id for an incident.
	 */
	private int incidentId;
	/**
	 * User id of person who reported the incident.
	 */
	private String caller;
	/**
	 * Category of the incident. One of the Category values.
	 */
	private Category category;
	/**
	 * Current state for the incident of type IncidentState.
	 */
	private IncidentState state;
	/**
	 * Priority of the incident. One of the Priority values.
	 */
	private Priority priority;
	/**
	 * User id of the incident owner or null if there is not an assigned owner.
	 */
	private String owner;
	/**
	 * Incidnet's name information from when the incident is created.
	 */
	private String name;
	/**
	 * On hold reason for the incident. One of the OnHoldReason values or null if
	 * the incident isn't in the HoldState.
	 */
	private OnHoldReason onHoldReason;
	/**
	 * Change request information for the incident. Can be null if no change request
	 * has been made.
	 */
	private String changeRequest;
	/**
	 * Resolution code for the incident. One of the ResolutionCode values or null if
	 * the incident isn't in the ResolvedState or ClosedState.
	 */
	private ResolutionCode resolutionCode;
	/**
	 * Cancellation code for the incident. One of the CancellationCode values or
	 * null if the incident isn't in the CanceledState.
	 */
	private CancellationCode cancellationCode;
	/**
	 * An ArrayList of notes.
	 */
	private ArrayList<String> notes;
	/**
	 * A static field that keeps track of the id value that should be given to the
	 * next ManagedIncident created.
	 */
	private static int counter = 0;

	/**
	 * initialize the newState
	 */
	private final IncidentState newState = new NewState();
	/**
	 * initialize the inProgressState
	 */
	private final IncidentState inProgressState = new InProgressState();
	/**
	 * initialize the onHoldState
	 */
	private final IncidentState onHoldState = new OnHoldState();
	/**
	 * initialize the resolvedState
	 */
	private final IncidentState resolvedState = new ResolvedState();
	/**
	 * initialize the closedState
	 */
	private final IncidentState closedState = new ClosedState();
	/**
	 * initialize the canceledState
	 */
	private final IncidentState canceledState = new CanceledState();

	/**
	 * Constructs a ManagedIncident from the provided parameters.
	 * 
	 * @param caller   the caller
	 * @param category the category of the incident
	 * @param priority the priority of the incident
	 * @param name     the name
	 * @param workNote the work notes
	 */
	public ManagedIncident(String caller, Category category, Priority priority, String name, String workNote) {
		checkParameters(caller, category, priority, name, workNote);
		// ASSUME an entirely new Incident. Thus, a newState.

		this.incidentId = ManagedIncident.counter;
		ManagedIncident.incrementCounter();

		this.caller = caller;

		this.owner = null;

		this.category = category;
		this.priority = priority;

		this.name = name;
		this.notes = new ArrayList<String>();
		this.notes.add(workNote);

		this.onHoldReason = null;
		this.changeRequest = null;
		this.resolutionCode = null;
		this.cancellationCode = null;

		this.state = newState;
	}

	/**
	 * The fields of the ManagedIncident are set to the values from the Incident.
	 * 
	 * @param i the incident
	 */
	public ManagedIncident(Incident i) {
		checkIncident(i);
		// Assume Incident could be in ANY state.

		this.incidentId = i.getId();
		ManagedIncident.incrementCounter();

		this.caller = i.getCaller();
		this.owner = i.getOwner();
		this.name = i.getName();
		// Check if below is valid.
		this.notes = new ArrayList<String>();
		this.notes.addAll(i.getWorkNotes().getNotes());
		this.changeRequest = i.getChangeRequest();
	}

	/**
	 * Helps check the parameters used to make an ManagedIncidet.
	 * 
	 * @param caller   the caller
	 * @param category the category of the incident
	 * @param priority the priority of the incident
	 * @param name     the name
	 * @param workNote the work notes
	 */
	private void checkParameters(String caller, Category category, Priority priority, String name, String workNote) {
		if (caller == null || category == null || priority == null || name == null || workNote == null)
			throw new IllegalArgumentException("One of the parameters is null.");

		if (caller.isEmpty() || name.isEmpty() || workNote.isEmpty())
			throw new IllegalArgumentException("One of the strings is empty.");
	}

	/**
	 * Check the Incident if it can be converted to an ManagedIncident.
	 * 
	 * @param checkMe the incident is checked
	 */
	private void checkIncident(Incident checkMe) {
		/**
		 * The fields of the ManagedIncident are set to the values from the Incident.
		 * You may find it useful to create private helper methods to transform the
		 * string category, state, priority, onHoldReason, resolutionCode, and
		 * cancellationCode from Incident into the object or enumeration types needed
		 * for a ManagedIncident. If the string cannot be converted to an appropriate
		 * object or enumeration type, then an IllegalArgumentException should be thrown
		 * for required values. However, remember that onHoldReason, resolutionCode, and
		 * cancellationCode can be null. If so, don't throw an IllegalArgumentException.
		 */

		if (checkMe == null)
			throw new IllegalArgumentException("The Incident is null.");

		String callerCheck = checkMe.getCaller();
		List<String> noteCheck = checkMe.getWorkNotes().getNotes();
		String nameCheck = checkMe.getName();

		if (nameCheck == null || noteCheck == null || callerCheck == null || callerCheck.isEmpty()
				|| nameCheck.isEmpty() || noteCheck.isEmpty())
			throw new IllegalArgumentException("One of the Strings is null or empty.");

		try {
			setCategory(checkMe.getCategory());

			setState(checkMe.getState());

			setPriority(checkMe.getPriority());

			setOnHoldReason(checkMe.getOnHoldReason());

			setResolutionCode(checkMe.getResolutionCode());

			setCancellationCode(checkMe.getCancellationCode());
		} catch (Exception e) {
			// throw new IllegalArgumentException(e.getMessage());
		}

	}

	/**
	 * Increase the current id value that will be given to the next ManagedIncident
	 * created by 1.
	 */
	public static void incrementCounter() {
		counter++;
	}

	/**
	 * Gets the incident's ID
	 * 
	 * @return the incident's ID
	 */
	public int getIncidentId() {
		return incidentId;
	}

	/**
	 * Gets the "change request" notes from the Incident.
	 * 
	 * @return the changeRequest
	 */
	public String getChangeRequest() {
		return changeRequest;
	}

	/**
	 * Gets the Incident's Category
	 * 
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Gets the Incident's Category in String.
	 * 
	 * @return the categoryString
	 */
	public String getCategoryString() {
		if (category == null) {
			return null;
		} else if (category == Category.DATABASE) {
			return C_DATABASE;
		} else if (category == Category.INQUIRY) {
			return C_INQUIRY;
		} else if (category == Category.SOFTWARE) {
			return C_SOFTWARE;
		} else if (category == Category.HARDWARE) {
			return C_HARDWARE;
		} else {
			return C_NETWORK;
		}
	}

	/**
	 * setter method of category
	 * 
	 * @param category the category of incident
	 */
	private void setCategory(String category) {
		if (category.equals(C_DATABASE))
			this.category = Category.DATABASE;
		else if (category.equals(C_INQUIRY))
			this.category = Category.INQUIRY;
		else if (category.equals(C_SOFTWARE))
			this.category = Category.SOFTWARE;
		else if (category.equals(C_HARDWARE))
			this.category = Category.HARDWARE;
		else if (category.equals(C_NETWORK))
			this.category = Category.NETWORK;
		else
			throw new IllegalArgumentException("Invalid category String.");
	}

	/**
	 * Get the Incident's Priority in String.
	 * 
	 * @return the PriorityString
	 */
	public String getPriorityString() {
		if (priority == null) {
			return null;
		} else if (priority == Priority.URGENT) {
			return P_URGENT;
		} else if (priority == Priority.HIGH) {
			return P_HIGH;
		} else if (priority == Priority.MEDIUM) {
			return P_MEDIUM;
		} else {
			return P_LOW;
		}
	}

	/**
	 * setter method of priority
	 * 
	 * @param priority the priority of the incident
	 */
	private void setPriority(String priority) {
		if (priority.equals(P_URGENT))
			this.priority = Priority.URGENT;
		else if (priority.equals(P_HIGH))
			this.priority = Priority.HIGH;
		else if (priority.equals(P_MEDIUM))
			this.priority = Priority.MEDIUM;
		else if (priority.equals(P_LOW))
			this.priority = Priority.LOW;
		else
			throw new IllegalArgumentException("Invalid Priority String.");
	}

	/**
	 * Gets the Incident's OnHoldReason in String
	 * 
	 * @return the OnHoldReasonString
	 */
	public String getOnHoldReasonString() {
		if (onHoldReason == null) {
			return null;
		} else if (onHoldReason == OnHoldReason.AWAITING_CALLER) {
			return "Awaiting Caller";
		} else if (onHoldReason == OnHoldReason.AWAITING_CHANGE) {
			return "Awaiting Change";
		} else {
			return "Awaiting Vendor";
		}
	}

	/**
	 * setter method of OnHoldReason
	 * 
	 * @param the OnholdReason
	 */
	private void setOnHoldReason(String holdReason) {
		if (holdReason == null) {
			onHoldReason = null;
			return;
		}

		if (holdReason.equals("Awaiting Caller"))
			onHoldReason = Command.OnHoldReason.AWAITING_CALLER;
		else if (holdReason.equals("Awaiting Change"))
			onHoldReason = Command.OnHoldReason.AWAITING_CHANGE;
		else if (holdReason.equals("Awaiting Vendor"))
			onHoldReason = Command.OnHoldReason.AWAITING_VENDOR;
		else
			throw new IllegalArgumentException("Invalid Hold Reason String");

	}

	/**
	 * Gets the Incident's CancellationCode in String
	 * 
	 * @return the CancellationCodeString
	 */
	public String getCancellationCodeString() {
		if (cancellationCode == null) {
			return null;
		} else if (cancellationCode == CancellationCode.DUPLICATE) {
			return "Duplicate";
		} else if (cancellationCode == CancellationCode.NOT_AN_INCIDENT) {
			return "Not an Incident";
		} else {
			return "Unnecessary";
		}
	}

	/**
	 * setter method of CancellationCode
	 * 
	 * @param cancellationCode the cancellation code of incident
	 */
	private void setCancellationCode(String cancellationCode) {
		if (cancellationCode == null) {
			this.cancellationCode = null;
			return;
		}

		if (cancellationCode.equals("Duplicate"))
			this.cancellationCode = Command.CancellationCode.DUPLICATE;
		else if (cancellationCode.equals("Not an Incident"))
			this.cancellationCode = Command.CancellationCode.NOT_AN_INCIDENT;
		else if (cancellationCode.equals("Unnecessary"))
			this.cancellationCode = Command.CancellationCode.UNNECESSARY;
		else
			throw new IllegalArgumentException("Invalid Cancellation Code String");
	}

	/**
	 * Get the Incident's current state.
	 * 
	 * @return the state of the incident
	 */
	public IncidentState getState() {
		return state;
	}

	/**
	 * setter method of State
	 * 
	 * @param state the state of incident
	 */
	private void setState(String state) {
		if (state == null || state.isEmpty())
			throw new IllegalArgumentException("Null/Empty State String.");
		else if (state.equals(NEW_NAME))
			this.state = newState;
		else if (state.equals(IN_PROGRESS_NAME))
			this.state = inProgressState;
		else if (state.equals(ON_HOLD_NAME))
			this.state = onHoldState;
		else if (state.equals(RESOLVED_NAME))
			this.state = resolvedState;
		else if (state.equals(CLOSED_NAME))
			this.state = closedState;
		else if (state.equals(CANCELED_NAME))
			this.state = canceledState;
		else
			throw new IllegalArgumentException("Invalid State String");
	}

	/**
	 * Get the Incident's Resolution Code.
	 * 
	 * @return the resolutionCode
	 */
	public ResolutionCode getResolutionCode() {
		return resolutionCode;
	}

	/**
	 * Get the Incident's Resolution Code in String.
	 * 
	 * @return the resolutionCode in String.
	 */
	public String getResolutionCodeString() {
		if (resolutionCode == null) {
			return null;
		} else if (resolutionCode == ResolutionCode.CALLER_CLOSED) {
			return "Caller Closed";
		} else if (resolutionCode == ResolutionCode.NOT_SOLVED) {
			return "Not Solved";
		} else if (resolutionCode == ResolutionCode.PERMANENTLY_SOLVED) {
			return "Permanently Solved";
		} else {
			return "Workaround";
		}
	}

	/**
	 * setter method of Resolution Code
	 * 
	 * @param resolutionCode the code to resolute
	 */
	private void setResolutionCode(String resolutionCode) {
		if (resolutionCode == null) {
			this.resolutionCode = null;
			return;
		}

		if (resolutionCode.equals("Caller Closed"))
			this.resolutionCode = Command.ResolutionCode.CALLER_CLOSED;
		else if (resolutionCode.equals("Not Solved"))
			this.resolutionCode = Command.ResolutionCode.NOT_SOLVED;
		else if (resolutionCode.equals("Permanently Solved"))
			this.resolutionCode = Command.ResolutionCode.PERMANENTLY_SOLVED;
		else if (resolutionCode.equals("Workaround"))
			this.resolutionCode = Command.ResolutionCode.WORKAROUND;
		else
			throw new IllegalArgumentException("Invalid Resolution Code String");
	}

	/**
	 * Get the Incident's owner.
	 * 
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Get the Incident's name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the Incident's caller.
	 * 
	 * @return the caller
	 */
	public String getCaller() {
		return caller;
	}

	/**
	 * Get the Incident's notes in a ArrayList.
	 * 
	 * @return the notes in ArrayList format.
	 */
	public ArrayList<String> getNotes() {
		return notes;
	}

	/**
	 * Get the Incident's notes in a String.
	 * 
	 * @return the notes in a String.
	 */
	public String getNotesString() {
		if (notes == null) {
			return "";
		}

		String note = "";
		for (int i = 0; i < notes.size(); i++) {
			note += notes.get(i) + '\n' + "-------" + '\n';
			// note += notes.get(i) + '\n' + "-------" + '\n' + "note" + '\n' + "-------" +
			// '\n';
		}
		return note;
	}

	/**
	 * the method drives the finite state machine by delegating the Command to the
	 * current state and if successful adding non-null notes to the notes list.
	 * 
	 * @param cmd the command
	 */
	public void update(Command cmd) {
		state.updateState(cmd);
	}

	/**
	 * Get's the ManagedIncident in it's XML format.
	 * 
	 * @return the XMLIncident
	 */
	public Incident getXMLIncident() {
		Incident incident = new Incident();
		incident.setId(incidentId);
		incident.setCaller(caller);
		incident.setCategory(getCategoryString());
		incident.setState(state.getStateName());
		incident.setPriority(getPriorityString());
		if (owner != null)
			incident.setOwner(owner);
		incident.setName(getName());

		if (this.state == onHoldState)
			incident.setOnHoldReason(getOnHoldReasonString());
		else if (this.state == resolvedState)
			incident.setResolutionCode(getResolutionCodeString());
		else if (this.state == canceledState)
			incident.setCancellationCode(getCancellationCodeString());
		if (notes != null) {
			incident.setWorkNotes(new WorkNotes());
			incident.getWorkNotes().getNotes().addAll(notes);
		} else
			incident.setWorkNotes(new WorkNotes());

		return incident;
	}

	/**
	 * Set the current id value that will be given to the next ManagedIncident
	 * created.
	 * 
	 * @param newCounter The new id value
	 */
	public static void setCounter(int newCounter) {
		ManagedIncident.counter = newCounter;
	}

}

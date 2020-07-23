package edu.ncsu.csc216.incident_management.model.command;

/**
 * Encapsulates the information about a user command that would lead to a
 * transition. Contains four inner enumerations
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class Command {

	/**
	 * An enumeration contained in the Command class. Represents one of the six
	 * possible commands that a user can make for the Incident Management FSM.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum CommandValue {
	/**
	 * Investigate Incident.
	 */
	INVESTIGATE,
	/**
	 * Hold the Incident.
	 */
	HOLD,
	/**
	 * Resolve the Incident.
	 */
	RESOLVE,
	/**
	 * Confirm the Incident.
	 */
	CONFIRM,
	/**
	 * Reopen the Incident.
	 */
	REOPEN,
	/**
	 * Cancel the Incident.
	 */
	CANCEL
	}

	/**
	 * An enumeration contained in the Command class. Represents the three possible
	 * on hold codes.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum OnHoldReason {
		/**
		 * The incident is waiting for more information from the caller, which is the
		 * person who created the incident.
		 */
		AWAITING_CALLER,
		/**
		 * The incident requires a change in some system so the incident is on hold
		 * until the change is made.
		 */
		AWAITING_CHANGE,
		/**
		 * The incident requires that an external vendor provides additional information
		 * for resolution so the incident is on hold until the vendor provides the
		 * information.
		 */
		AWAITING_VENDOR
	}

	/**
	 * An enumeration contained in the Command class. Repsrents the three possible
	 * cancellation codes.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum ResolutionCode {
		/**
		 * The reported incident has been resolved such that another incident like is
		 * shouldn't be reported.
		 */
		PERMANENTLY_SOLVED,
		/**
		 * The reported incident was resolved with a workaround. Future incidents may be
		 * reported like this one.
		 */
		WORKAROUND,
		/**
		 * The reported incident was not solved due to issues with replicating the
		 * problem.
		 */
		NOT_SOLVED,
		/**
		 * The reported incident was closed by the caller either because they fixed the
		 * incident on their own or withdrew the incident.
		 */
		CALLER_CLOSED
	}

	/**
	 * An enumeration contained in the Command class. Repsrents the three possible
	 * cancellation codes.
	 * 
	 * @author Dennis Sarsozo
	 * @author Zhongxiao Mei
	 *
	 */
	public enum CancellationCode {
		/**
		 * The incident is a duplicate of another incident in the system.
		 */
		DUPLICATE,
		/**
		 * The incident is not something that should be fixed or addressed.
		 */
		UNNECESSARY,
		/**
		 * The report is not an incident.
		 */
		NOT_AN_INCIDENT
	};

	/** Message shown when onHold reason is about the Caller. */
	public static final String OH_CALLER = "Awaiting Caller";
	/** Message shown when onHold reason is about changing the system. */
	public static final String OH_CHANGE = "Awaiting Change";
	/** Message shown when onHold reason is about the Vendor. */
	public static final String OH_VENDOR = "Awaiting Vendor";
	/** Message shown when the Incident is permanently resolved. */
	public static final String RC_PERMANENTLY_SOLVED = "Permanently Solved";
	/** Message shown when Incident is resolved via workaround. */
	public static final String RC_WORKAROUND = "WORKAROUND";
	/** Message shown when the Incident is not resolved. */
	public static final String RC_NOT_SOLVED = "Not Solved";
	/** Message shown when the Incident is closed by the caller. */
	public static final String RC_CALLER_CLOSED = "Caller Closed";
	/** Message shown when Incident is canceled since it is a duplicate. */
	public static final String CC_DUPLICATE = "Duplicate";
	/** Message shown when the Incident is cancelled due to being unnecessary */
	public static final String CC_UNNECESSARY = "Unnecessary";
	/** Message shown when the Incident turns out is not an Incident at all */
	public static final String CC_NOT_AN_INCIDENT = "Not an Incident";

	/** The current CommandValue of the Command */
	private CommandValue c;
	/** The onHoldReason is initialized based on the Command. */
	private OnHoldReason onHoldReason;
	/** The resolutionCode is initialized based on the Command. */
	private ResolutionCode resolutionCode;
	/** The cancellationCode is initialized based on the Command. */
	private CancellationCode cancellationCode;

	/** the owner's ID of the Incident */
	private String ownerId;
	/** The description of the Command */
	private String note;

	/**
	 * Initiate a state change of the Incident.
	 * 
	 * @param c                the command value
	 * @param ownerId          owner's id
	 * @param onHoldReason     the reason of on hold
	 * @param resolutionCode   the code to resolve
	 * @param cancellationCode the code to cancel
	 * @param note             notes can be added
	 */
	public Command(CommandValue c, String ownerId, OnHoldReason onHoldReason, ResolutionCode resolutionCode,
			CancellationCode cancellationCode, String note) {
		checkParameters(c, ownerId, onHoldReason, resolutionCode, cancellationCode, note);

		this.c = c;
		this.onHoldReason = onHoldReason;
		this.resolutionCode = resolutionCode;
		this.cancellationCode = cancellationCode;
		this.ownerId = ownerId;
		this.note = note;
	}

	private void checkParameters(CommandValue c, String ownerId, OnHoldReason onHoldReason,
			ResolutionCode resolutionCode, CancellationCode cancellationCode, String note) {
		if (c == null)
			throw new IllegalArgumentException();
		else if (c == CommandValue.INVESTIGATE && (ownerId == null || ownerId.isEmpty()))
			throw new IllegalArgumentException();
		else if (c == CommandValue.HOLD && onHoldReason == null)
			throw new IllegalArgumentException();
		else if (c == CommandValue.RESOLVE && resolutionCode == null)
			throw new IllegalArgumentException();
		else if (c == CommandValue.CANCEL && cancellationCode == null)
			throw new IllegalArgumentException();
		else if (note == null || note.isEmpty())
			throw new IllegalArgumentException();
	}

	/**
	 * Gets the type of CommandValue.
	 * 
	 * @return the CommandValue contained in the Command.
	 */
	public CommandValue getCommand() {
		return c;
	}

	/**
	 * Get the Command's on hold reasons.
	 * 
	 * @return the OnHoldReason contained in the Command.
	 */
	public OnHoldReason getOnHoldReason() {
		return onHoldReason;
	}

	/**
	 * Get Command's resolution codes.
	 * 
	 * @return the ResolutionCode contained in the Command.
	 */
	public ResolutionCode getResolutionCode() {
		return resolutionCode;
	}

	/**
	 * Get the Command's cancellation codes.
	 * 
	 * @return the CancellationCode contained in the Command.
	 */
	public CancellationCode getCancellationCode() {
		return cancellationCode;
	}

	/**
	 * Get the owner's ID.
	 * 
	 * @return The ownerID
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * Get the Command's Notes.
	 * 
	 * @return The notes.
	 */
	public String getWorkNote() {
		return note;
	}

}

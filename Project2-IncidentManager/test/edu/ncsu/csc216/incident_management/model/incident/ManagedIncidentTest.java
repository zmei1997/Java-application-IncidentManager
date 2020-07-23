package edu.ncsu.csc216.incident_management.model.incident;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.ncsu.csc216.incident.xml.Incident;
import edu.ncsu.csc216.incident.xml.WorkNotes;
import edu.ncsu.csc216.incident_management.model.command.Command;
import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Category;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.InProgressState;
import edu.ncsu.csc216.incident_management.model.incident.ManagedIncident.Priority;

/**
 * Tests ManagedIncident
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class ManagedIncidentTest {

	/**
	 * Test incrementCounter()
	 */
	@Test
	public void testManagedIncident() {
		try {
			ManagedIncident i = new ManagedIncident(null, Category.INQUIRY, Priority.HIGH, "name", "workNote");
			Command c = new Command(CommandValue.RESOLVE, "zmei", null, null, null, "note");
			i.update(c);
		} catch (IllegalArgumentException e) {
			//
		}

		try {
			ManagedIncident i = new ManagedIncident("", Category.INQUIRY, Priority.HIGH, "name", "workNote");
			Command c = new Command(CommandValue.RESOLVE, "zmei", null, null, null, "note");
			i.update(c);
		} catch (IllegalArgumentException e) {
			//
		}

		try {
			Incident icd = null;
			assertNull(icd);
		} catch (IllegalArgumentException e) {
			//
		}
	}

	/**
	 * tests updateState() for each state
	 */
	@Test
	public void testUpdateState() {
		ManagedIncident i = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		try {
			Command cm0 = new Command(CommandValue.REOPEN, "zmei", null, null, null, "note");
			i.update(cm0);
		} catch (UnsupportedOperationException e) {
			//
		}
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		i.update(cm);
		assertEquals("In Progress", i.getState().getStateName());
		Command cm2 = new Command(CommandValue.HOLD, "zmei", OnHoldReason.AWAITING_CALLER, null, null, "note");
		i.update(cm2);
		assertEquals("On Hold", i.getState().getStateName());
		try {
			Command cm00 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
			i.update(cm00);
		} catch (UnsupportedOperationException e) {
			//
		}
		Command cm3 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.NOT_SOLVED, null, "note");
		i.update(cm3);
		assertEquals("Resolved", i.getState().getStateName());
		try {
			Command cm0000 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
			i.update(cm0000);
			assertEquals("Resolved", i.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		Command cm4 = new Command(CommandValue.CONFIRM, "zmei", null, null, null, "note");
		i.update(cm4);
		assertEquals("Closed", i.getState().getStateName());
		try {
			Command cm000 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
			i.update(cm000);
			assertEquals("In Closed", i.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		Command cm5 = new Command(CommandValue.REOPEN, "zmei", null, null, null, "note");
		i.update(cm5);
		assertEquals("In Progress", i.getState().getStateName());
		try {
			Command cm55 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
			i.update(cm55);
			assertEquals("In Progress", i.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}

		ManagedIncident i2 = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command c2 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.DUPLICATE, "note");
		i2.update(c2);
		assertEquals("Canceled", i2.getState().getStateName());

		ManagedIncident i3 = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command c3 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		i3.update(c3);
		assertEquals("In Progress", i3.getState().getStateName());
		Command c33 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		i3.update(c33);
		assertEquals("Resolved", i3.getState().getStateName());
		Command c333 = new Command(CommandValue.HOLD, "zmei", OnHoldReason.AWAITING_CHANGE, null, null, "note");
		i3.update(c333);
		assertEquals("On Hold", i3.getState().getStateName());
		assertEquals(null, i3.getChangeRequest());

		Command c3333 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.UNNECESSARY, "note");
		i3.update(c3333);
		assertEquals("Canceled", i3.getState().getStateName());

		ManagedIncident i4 = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command c4 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		i4.update(c4);
		assertEquals("In Progress", i4.getState().getStateName());
		Command c44 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		i4.update(c44);
		assertEquals("Resolved", i4.getState().getStateName());
		Command c444 = new Command(CommandValue.REOPEN, "zmei", null, null, null, "note");
		i4.update(c444);
		assertEquals("In Progress", i4.getState().getStateName());
		Command c4444 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.WORKAROUND, null, "note");
		i4.update(c4444);
		assertEquals("Resolved", i4.getState().getStateName());
		Command c44444 = new Command(CommandValue.HOLD, "zmei", OnHoldReason.AWAITING_VENDOR, null, null, "note");
		i4.update(c44444);
		assertEquals("On Hold", i4.getState().getStateName());
		Command c444444 = new Command(CommandValue.REOPEN, "zmei", null, null, null, "note");
		i4.update(c444444);
		assertEquals("In Progress", i4.getState().getStateName());
		Command c4444444 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.NOT_AN_INCIDENT,
				"note");
		i4.update(c4444444);
		assertEquals("Canceled", i4.getState().getStateName());

		ManagedIncident i5 = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command c5 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		i5.update(c5);
		assertEquals("In Progress", i5.getState().getStateName());
		Command c55 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		i5.update(c55);
		assertEquals("Resolved", i5.getState().getStateName());
		Command c555 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.UNNECESSARY, "note");
		i5.update(c555);
		assertEquals("Canceled", i5.getState().getStateName());

		ManagedIncident i22 = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command c22 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.DUPLICATE, "note");
		i22.update(c22);
		assertEquals("Canceled", i22.getState().getStateName());
		try {
			Command c222 = new Command(CommandValue.CANCEL, "zmei", null, null, CancellationCode.DUPLICATE, "note");
			i22.update(c222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		try {
			Command c2222 = new Command(CommandValue.CONFIRM, "zmei", null, null, null, "note");
			i22.update(c2222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		try {
			Command c22222 = new Command(CommandValue.HOLD, "zmei", OnHoldReason.AWAITING_VENDOR, null, null, "note");
			i22.update(c22222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		try {
			Command c222222 = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
			i22.update(c222222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		try {
			Command c2222222 = new Command(CommandValue.REOPEN, "zmei", null, null, null, "note");
			i22.update(c2222222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
		try {
			Command c22222222 = new Command(CommandValue.RESOLVE, "zmei", null, ResolutionCode.NOT_SOLVED, null,
					"note");
			i22.update(c22222222);
			assertEquals("Canceled", i22.getState().getStateName());
		} catch (UnsupportedOperationException e) {
			//
		}
	}

//	/**
//	 * Test getIncidentId()
//	 */
//	@Test
//	public void testGetIncidentId() {
//		
//		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
//		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null,
//				null, null, "note");
//		incident.update(cm);
//		assertEquals(0, incident.getIncidentId());
//	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getChangeRequest()}.
	 */
	@Test
	public void testGetChangeRequest() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER,
				ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		incident.update(cm);
		assertEquals(null, incident.getChangeRequest());
		assertEquals(0, incident.getIncidentId());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getCategory()}.
	 */
	@Test
	public void testGetCategory() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER,
				ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		incident.update(cm);
		assertEquals(Category.INQUIRY, incident.getCategory());

		try {
			ManagedIncident incident1 = new ManagedIncident("caller", null, Priority.HIGH, "name", "workNote");
			incident1.getCategory();
		} catch (IllegalArgumentException e) {
			//
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getCategoryString()}.
	 */
	@Test
	public void testGetCategoryString() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER,
				ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		incident.update(cm);
		assertEquals("Inquiry", incident.getCategoryString());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getPriorityString()}.
	 */
	@Test
	public void testGetPriorityString() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER,
				ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		incident.update(cm);
		assertEquals("High", incident.getPriorityString());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getOnHoldReasonString()}.
	 */
	@Test
	public void testGetOnHoldReasonString() {
		try {
			ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name",
					"workNote");
			Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_VENDOR, null, null,
					"note");
			incident.update(cm);
			assertEquals(null, incident.getOnHoldReasonString());
		} catch (NullPointerException e) {
			//
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getCancellationCodeString()}.
	 */
	@Test
	public void testGetCancellationCodeString() {
		try {
			ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name",
					"workNote");
			Command cm = new Command(CommandValue.CANCEL, "zmei", OnHoldReason.AWAITING_CALLER,
					ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "note");
			incident.update(cm);
			assertEquals("Duplicate", incident.getCancellationCodeString());
		} catch (NullPointerException e) {
			//
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getState()}.
	 */
	@Test
	public void testGetState() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER,
				ResolutionCode.PERMANENTLY_SOLVED, CancellationCode.DUPLICATE, "note");
		incident.update(cm);
		assertEquals("In Progress", incident.getState().getStateName());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getResolutionCode()}.
	 */
	@Test
	public void testGetResolutionCode() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incident.update(cm);
		assertEquals(null, incident.getResolutionCode());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getResolutionCodeString()}.
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testGetResolutionCodeString() {
		try {
			ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name",
					"workNote");
			Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, ResolutionCode.WORKAROUND, null, "note");
			incident.update(cm);
			incident.setCounter(0);
			assertEquals(null, incident.getResolutionCodeString());
		} catch (NullPointerException e) {
			//
		}
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getOwner()}.
	 */
	@Test
	public void testGetOwner() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incident.update(cm);
		assertEquals("zmei", incident.getOwner());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getName()}.
	 */
	@Test
	public void testGetName() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incident.update(cm);
		assertEquals("name", incident.getName());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getCaller()}.
	 */
	@Test
	public void testGetCaller() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incident.update(cm);
		assertEquals("caller", incident.getCaller());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getNotes()}.
	 */
	@Test
	public void testGetNotes() {
		ManagedIncident incident = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incident.update(cm);
		Command cmd = new Command(CommandValue.HOLD, "zmei", OnHoldReason.AWAITING_CHANGE, null, null, "note");
		incident.update(cmd);
		ArrayList<String> list = new ArrayList<String>();
		list.add("workNote");
		list.add("note");
		list.add("note");
		assertEquals(list, incident.getNotes());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getNotesString()}.
	 */
	@Test
	public void testGetNotesString() {
		ManagedIncident incidentDIFF = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name",
				"workNote");
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		incidentDIFF.update(cm);
		assertEquals("workNote" + '\n' + "-------" + '\n' + "note" + '\n' + "-------" + '\n',
				incidentDIFF.getNotesString());
	}

	/**
	 * Test method for
	 * {@link edu.ncsu.csc216.incident_management.model.incident.ManagedIncident#getXMLIncident()}.
	 */
	@Test
	public void testGetXMLIncident() {
		Incident incident = new Incident();
		incident.setId(1);
		incident.setCaller("sesmith5");
		incident.setCategory("Software");
		incident.setState("New");
		incident.setPriority("Urgent");
		incident.setName("Jenkins installation");
		WorkNotes note = new WorkNotes();
		note.getNotes().add("I am not empty.");
		incident.setWorkNotes(note);
		incident.setOnHoldReason("Awaiting Vendor");
		incident.setCancellationCode("DUPLICATE");
		incident.setResolutionCode("PERMANENTLY SOLVED");
		ManagedIncident incidents = new ManagedIncident(incident);
		assertEquals(1, incidents.getXMLIncident().getId());
		assertEquals("sesmith5", incidents.getCaller());
		assertEquals("Awaiting Vendor", incidents.getOnHoldReasonString());
	}

	/**
	 * for jenkins
	 */
	@Test
	public void testUpdateClosedToInProgress() {
		ManagedIncident checkInc = new ManagedIncident("caller", Category.INQUIRY, Priority.HIGH, "name", "workNote");
		Command cmINV = new Command(CommandValue.INVESTIGATE, "owner", null, null, null, "note");
		checkInc.update(cmINV);
		
		assertEquals(checkInc.new InProgressState().getClass(), checkInc.getState().getClass());
		assertEquals("owner", checkInc.getOwner());
		
		Command cmRES = new Command(CommandValue.RESOLVE, "owner", null, Command.ResolutionCode.PERMANENTLY_SOLVED, null, "note");
		checkInc.update(cmRES);
		
		assertEquals(checkInc.new ResolvedState().getClass(), checkInc.getState().getClass());
		assertEquals(Command.ResolutionCode.PERMANENTLY_SOLVED, checkInc.getResolutionCode());
		
		Command cmCONFIRM = new Command(CommandValue.CONFIRM, "owner", null, null, null, "note");
		checkInc.update(cmCONFIRM);
		
		assertEquals(checkInc.new ClosedState().getClass(), checkInc.getState().getClass());
		assertEquals(Command.ResolutionCode.PERMANENTLY_SOLVED, checkInc.getResolutionCode());
		
		Command cmREOPEN = new Command(CommandValue.REOPEN, "owner", null, null, null, "note");
		checkInc.update(cmREOPEN);
		
		assertEquals(checkInc.new InProgressState().getClass(), checkInc.getState().getClass());
		assertEquals(null, checkInc.getResolutionCode());
	}
}

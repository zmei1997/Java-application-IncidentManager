package edu.ncsu.csc216.incident_management.model.command;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.incident_management.model.command.Command.CancellationCode;
import edu.ncsu.csc216.incident_management.model.command.Command.CommandValue;
import edu.ncsu.csc216.incident_management.model.command.Command.OnHoldReason;
import edu.ncsu.csc216.incident_management.model.command.Command.ResolutionCode;

/**
 * Tests Command class
 * 
 * @author Dennis Sarsozo
 * @author Zhongxiao Mei
 *
 */
public class CommandTest {

	/**
	 * Test the constructor of Command
	 */
	@Test
	public void testCommand() {
		Command cm = null;
		
		try {
			cm = new Command(null, "zmei", null, null, null, "note");
			assertEquals("zmei", cm.getOwnerId());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
		
		try {
			cm = new Command(CommandValue.INVESTIGATE, null, null, null, null, "note");
			assertEquals(null, cm.getOwnerId());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
		
		try {
			cm = new Command(CommandValue.HOLD, "zmei", null, null, null, "note");
			assertEquals("zmei", cm.getOwnerId());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
		
		try {
			cm = new Command(CommandValue.RESOLVE, "zmei", null, null, null, "note");
			assertEquals("zmei", cm.getOwnerId());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
		
		try {
			cm = new Command(CommandValue.CANCEL, "zmei", null, null, null, "note");
			assertEquals("zmei", cm.getOwnerId());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
		
		try {
			cm = new Command(null, "zmei", null, null, null, null);
			assertEquals(null, cm.getWorkNote());
		} catch (IllegalArgumentException e) {
			// skip this line
		}
	}

	/**
	 * Test getCommand().
	 */
	@Test
	public void testGetCommand() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", null, null, null, "note");
		assertEquals(CommandValue.INVESTIGATE, cm.getCommand());
	}

	/**
	 * Test getOnHoldReason().
	 */
	@Test
	public void testGetOnHoldReason() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER, null, null, "note");
		assertEquals(OnHoldReason.AWAITING_CALLER, cm.getOnHoldReason());
	}

	/**
	 * Test getResolutionCode().
	 */
	@Test
	public void testGetResolutionCode() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER, ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		assertEquals(ResolutionCode.NOT_SOLVED, cm.getResolutionCode());
	}

	/**
	 * Test getCancellationCode().
	 */
	@Test
	public void testGetCancellationCode() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER, ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		assertEquals(CancellationCode.DUPLICATE, cm.getCancellationCode());
	}

	/**
	 * Test getOwnerId().
	 */
	@Test
	public void testGetOwnerId() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER, ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		assertEquals("zmei", cm.getOwnerId());
	}

	/**
	 * Test getWorkNote().
	 */
	@Test
	public void testGetWorkNote() {
		Command cm = new Command(CommandValue.INVESTIGATE, "zmei", OnHoldReason.AWAITING_CALLER, ResolutionCode.NOT_SOLVED, CancellationCode.DUPLICATE, "note");
		assertEquals("note", cm.getWorkNote());
	}

}

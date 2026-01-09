
package edu.ncsu.csc216.wolf_review.model.command;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * Tests the Command class in the WolfReview system.
 * Each test ensures that the Command enforces the rules for valid commands
 * and throws appropriate exceptions when necessary.
 * @author Steven Saleeb
 */
class CommandTest {

	/**
	 * Tests the commands that don't need extra informations, these command should work even if the information is null
	 */
	@Test
    void testValidCommandsWithoutInformation() {
		
        assertDoesNotThrow(() -> new Command(Command.CommandValue.MODIFY, null));
        assertDoesNotThrow(() -> new Command(Command.CommandValue.SUBMIT, null));
        assertDoesNotThrow(() -> new Command(Command.CommandValue.PROPOSE, null));
    }

	/**
	 * Tests if command is null, should throw an exception
	 */
    @Test
    void testConstructorWithNullCommand() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> new Command(null, null));
        assertEquals("Invalid information.", e.getMessage());
    }

    /**
     * Tests commands that requires extra information, should throw an exception if it is null
     */
    @Test
    void testConstructorMissingInformation() {

    	assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.ASSIGN, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.PROCESS, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.RECOMMEND, null));
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.CLOSE, null));
    }

    /**
     * Tests commands that require specific information
     */
    @Test
    void testConstructorWithInformation() {

    	assertDoesNotThrow(() -> new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
    	assertDoesNotThrow(() -> new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_WEAK_ACCEPT));
    	assertDoesNotThrow(() -> new Command(Command.CommandValue.PROCESS, Paper.ACCEPT_CLOSED));
    }
    
    /**
     * Tests commands that shouldn't have extra information
     */
    @Test
    void testConstructorExtraInformation() {
    	
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.MODIFY, "Extra informations"));
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.SUBMIT, "Extra informations"));
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.PROPOSE, "Extra informations"));
    }
    
    /**
     * Tests recommend command with invalid information
     */
    @Test
    void testRecommendInvalidInformation() {

    	assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.RECOMMEND, Paper.ACCEPT_CLOSED));
    }

    /**
     * Tests close command with invalid information
     */
    @Test
    void testCloseInvalidInformation() {
        
        assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.CLOSE, Paper.RECOMMEND_STRONG_REJECT));
    }

    /**
     * Tests process command with invalid information
     */
    @Test
    void testProcessInvalidInformation() {

    	assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.PROCESS, Paper.RECOMMEND_WEAK_ACCEPT));
    }

}

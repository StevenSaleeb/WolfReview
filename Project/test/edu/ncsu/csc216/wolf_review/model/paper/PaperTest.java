package edu.ncsu.csc216.wolf_review.model.paper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.command.Command;
import edu.ncsu.csc216.wolf_review.model.command.Command.CommandValue;
import edu.ncsu.csc216.wolf_review.model.paper.Paper.PaperType;

/**
 * Tests the Paper class in the WolfReview system.
 * Each test makes sure that the Paper behaves correctly, updates state properly, 
 * and throws exceptions when invalid actions are attempted.
 * @author Steven Saleeb
 */
class PaperTest {

	/**
	 * A Paper instance used for testing
	 */
    private Paper paper;

    /**
     * Sets up a new Paper instance before each test and resets the Id counter
     */
    @BeforeEach 
    void setUp() {
        Paper.setCounter(0); 
        paper = new Paper("Steven Saleeb");
    }

    /**
     * Tests the constructor and getters methods
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals(0, paper.getId());
        assertEquals("Steven Saleeb", paper.getAuthorNames());
        assertEquals(Paper.P_ABSTRACT, paper.getPaperType());
        assertEquals(Paper.SUBMITTED_NAME, paper.getState());
        assertFalse(paper.isProcessed());
        assertEquals("", paper.getReviewer());
        assertEquals("", paper.getNote());
    }

    /**
     * Tests the constructor with full details
     */
    @Test 
    void testConstructorWithFullDetails() {
        Paper p2 = new Paper(5, Paper.CLOSED_NAME, "Steven", Paper.P_FULLPAPER, true, null, Paper.ACCEPT_CLOSED);
        assertEquals(5, p2.getId());
        assertEquals("Steven", p2.getAuthorNames());
        assertEquals(Paper.P_FULLPAPER, p2.getPaperType());
        assertEquals(Paper.CLOSED_NAME, p2.getState());
        assertTrue(p2.isProcessed());
        assertEquals("", p2.getReviewer());
        assertEquals(Paper.ACCEPT_CLOSED, p2.getNote()); 
    }  
 
    @Test
    void testInvalidConstructors() {
        assertThrows(IllegalArgumentException.class, () -> new Paper(""));
        assertThrows(IllegalArgumentException.class, () -> new Paper((String) null));
    }
    /**
     * Tests constructor for invalid author names
     */
    @Test
    void testSettersValidation() {
    
        assertThrows(IllegalArgumentException.class, () -> new Paper(""));
        assertThrows(IllegalArgumentException.class, () -> new Paper((String) null));
    }

    /**
     * Tests assigning a reviewer and making a recommendation
     */
    @Test
    void testAssignThenRecommend() {
 
        Paper.setCounter(0);
 
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        assertEquals(Paper.REVIEWING_NAME, paper.getState());
       
        assertEquals("anonymous", paper.getAuthorNames());
        assertEquals("Reviewer", paper.getReviewer());
        assertFalse(paper.isProcessed());

        paper.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        assertEquals(Paper.SUBMITTED_NAME, paper.getState());
        assertEquals(Paper.RECOMMEND_STRONG_ACCEPT, paper.getNote());
        assertTrue(paper.isProcessed());
        assertEquals("Reviewer", paper.getReviewer());
    }

    /**
     * Tests revising state transitions
     */
    @Test
    void testRevisingState() {
       
    	Paper p = new Paper("Steven S.; Mario A.");
    	p.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        p.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        p.update(new Command(Command.CommandValue.SUBMIT, null));
        p.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        p.update(new Command(Command.CommandValue.PROPOSE, null));
        p.update(new Command(Command.CommandValue.MODIFY, null));
        assertEquals(Paper.SUBMITTED_NAME, paper.getState());
        assertFalse(paper.isProcessed());
    }

  
    /**
     * Tests closing a paper from the submitted state
     */
    @Test
    void testCloseFromSubmitted() {
        
        Paper p2 = new Paper("Steve");
        p2.update(new Command(Command.CommandValue.CLOSE, Paper.WITHDRAW_CLOSED));
        assertEquals(Paper.CLOSED_NAME, p2.getState());
        assertEquals(Paper.WITHDRAW_CLOSED, p2.getNote());
        assertFalse(p2.isProcessed());
    }

    /**
     * Tests closing a paper from reviewing state
     */
    @Test
    void testCloseFromReviewingValid() {
    	 
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        paper.update(new Command(Command.CommandValue.CLOSE, Paper.REJECT_CLOSED)); 
        assertEquals(Paper.CLOSED_NAME, paper.getState());
        assertEquals(Paper.REJECT_CLOSED, paper.getNote());
        assertTrue(paper.isProcessed()); 
    }
    
    @Test
    void testCloseFromReviewingThrows() {
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        assertThrows(UnsupportedOperationException.class,
                     () -> paper.update(new Command(Command.CommandValue.CLOSE, Paper.REJECT_CLOSED)));
    }

    /**
     * Tests closing a paper from revising state
     */
    @Test
    void testCloseFromRevising() {
    	
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        paper.update(new Command(Command.CommandValue.SUBMIT, null));
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper.update(new Command(Command.CommandValue.PROPOSE, null));
        paper.update(new Command(Command.CommandValue.CLOSE, Paper.WITHDRAW_CLOSED));
        assertEquals(Paper.CLOSED_NAME, paper.getState());
        assertEquals(Paper.WITHDRAW_CLOSED, paper.getNote());
        assertFalse(paper.isProcessed());
    }

    

    /**
     * Tests toString and Id counter increment
     */
    @Test
    void testToStringAndCounter() {
        Paper.setCounter(0);
        Paper p1 = new Paper("Steve");
        assertEquals("0,Submitted,Steve,Abstract,false,,", p1.toString());
      
        Paper.incrementCounter();
      
        Paper p2 = new Paper("Steven Saleeb");
        assertEquals(2, p2.getId());
    }

    /**
     * Tests handling a null command
     */
    @Test
    void testNullCommand() {
        assertThrows(IllegalArgumentException.class, () -> paper.update(null));
    } 
    
    /**
     * Tests submitted state behavior
     */
    @Test
    void testSubmittedState() {
        Paper p = new Paper("Steven S.; Batrck O.");
        p.update(new Command(CommandValue.ASSIGN, "Maya")); 

        assertEquals(Paper.REVIEWING_NAME, p.getState());
        assertEquals("Maya", p.getReviewer());
        assertEquals("anonymous", p.getAuthorNames()); 
        assertFalse(p.isProcessed());
    }
   

   /**
    * Tests that invalid commands in registering state throw exception
    */
    @Test
    void testRegisteringStateInvalidCommandThrows() {

        assertThrows(UnsupportedOperationException.class, 
            () -> paper.update(new Command(CommandValue.SUBMIT, null)));
    }

    /**
     * Tests the PaperType and that the type updates after submission
     */
    @Test
    void testPaperType() {
      
        PaperType typeAbstract = Paper.PaperType.ABSTRACT;
        PaperType typeFull = Paper.PaperType.FULLPAPER;
        
        assertEquals(Paper.PaperType.ABSTRACT, typeAbstract);
        assertEquals(Paper.PaperType.FULLPAPER, typeFull);

        assertEquals(Paper.P_ABSTRACT, paper.getPaperType());
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        paper.update(new Command(CommandValue.SUBMIT, null));
        assertEquals(Paper.P_FULLPAPER, paper.getPaperType());
    }
   
    /**
     * Tests registering state and processing
     */
    @Test
    public void testRegisteringState() {
    	Paper p = new Paper("Steven S.; Mario A.");
 
        p.update(new Command(CommandValue.ASSIGN, "Steven"));
        p.update(new Command(CommandValue.RECOMMEND, Paper.RECOMMEND_WEAK_ACCEPT));
        assertEquals(Paper.SUBMITTED_NAME, p.getState());
        assertTrue(p.isProcessed()); 
        
        Paper p2 = new Paper(2, Paper.SUBMITTED_NAME, Paper.ANONYMOUS, Paper.P_FULLPAPER, true, "Mario", Paper.RECOMMEND_STRONG_ACCEPT);
        p2.update(new Command(CommandValue.ACCEPT, null)); 
        assertEquals(Paper.REGISTERING_NAME, p2.getState()); 
        assertTrue(p2.isProcessed());
        assertEquals("", p2.getNote());
        p2.update(new Command(CommandValue.PROCESS, Paper.ACCEPT_CLOSED));
        assertEquals(Paper.CLOSED_NAME, p2.getState());
        assertTrue(p2.isProcessed()); 
        assertEquals(Paper.ACCEPT_CLOSED, p2.getNote());
 
    }  
   
    /**
     * Tests when paper have invalid parameters
     */
    @Test
    void testInvalidParameters() {
    
        assertThrows(IllegalArgumentException.class, () -> 
            new Paper(0, Paper.SUBMITTED_NAME, "Steven", Paper.P_ABSTRACT, false, null, null));
    }
     
    /**
     * Tests invalid closed state 
     */ 
    @Test
    void testInvalidClosed() {
    		Paper p = new Paper(5, Paper.CLOSED_NAME, "Steven", Paper.P_FULLPAPER, true, "", Paper.WITHDRAW_CLOSED);
    		Command c  = new Command(CommandValue.MODIFY, null);
    		assertEquals(Paper.CLOSED_NAME, p.getState());
    		assertThrows(UnsupportedOperationException.class, 
    				() -> p.update(c));
    }  
    
    /**
     * Tests that closing a processed paper with the duplicate note will throw an UnsupportedOperationException
     */
    @Test
    void testCloseDuplicateClosedInPaper() {
      
        Paper p = new Paper("Steven Saleeb");
        p.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        p.update(new Command(Command.CommandValue.RECOMMEND, Paper.RECOMMEND_STRONG_ACCEPT));
        assertTrue(p.isProcessed());

        Command c = new Command(Command.CommandValue.CLOSE, Paper.DUPLICATE_CLOSED);
        assertThrows(UnsupportedOperationException.class, () -> p.update(c));
    }
    
    /**
     * Tests closing a paper in ReviewingState without temporary author name and 
     * makes sure the state is closed with the withdraw note  
     * 
     */
    @Test
    void testCloseWithNoTempAuthorNames() {
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper = new Paper("Steven Saleeb");
        paper.update(new Command(Command.CommandValue.ASSIGN, "Reviewer"));
        paper.update(new Command(Command.CommandValue.CLOSE, Paper.WITHDRAW_CLOSED));
        assertEquals(Paper.CLOSED_NAME, paper.getState());
        assertEquals(Paper.WITHDRAW_CLOSED, paper.getNote());
    }
    

}

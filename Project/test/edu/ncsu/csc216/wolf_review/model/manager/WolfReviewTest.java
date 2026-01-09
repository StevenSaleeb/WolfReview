package edu.ncsu.csc216.wolf_review.model.manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.command.Command;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * Tests the WolfReview class in the WolfReview system.
 * Each test makes sure that WolfReview maintains consistent state,
 * validates inputs, and throws appropriate exceptions when errors occur
 * @author Steven Saleeb
 */
class WolfReviewTest { 

	/**
	 * A WolfReview singleton instance used for testing the review system
	 */
    private WolfReview review;

    /**
     * Sets up a WolfReview instance before each test
     */
    @BeforeEach
    void setUp() {
        review = WolfReview.getInstance();
        review.resetManager();
        review = WolfReview.getInstance();
    }
    /**
     * Tests that WolfReview is a singleton and always returns the same instance
     */
    @Test
    void testSingleton() {
        WolfReview second = WolfReview.getInstance();
        assertEquals(review, second);
    }

    /**
     * Tests adding a new paper track and loading its information
     */
    @Test
    void testAddAndLoadPaperTrack() {
        review.addNewPaperTrack("Track", 10, 5);
        assertEquals("Track", review.getActivePaperTrackName());
        assertNotNull(review.getActivePaperTrack());
        assertEquals(1, review.getPaperTrackList().length);
    }

    /**
     * Tests adding a new paper to the active paper track
     */
    @Test
    void testAddPaperToPaperTrack() {
        review.addNewPaperTrack("Paper", 10, 5);
        review.addPaperToPaperTrack("Steve");
        assertEquals(1, review.getActivePaperTrack().getPapers().size());
    }

    /**
     * Tests executing the ASSIGN command on a paper and makes sure that 
     * the paper state and author names are updated correctly
     */
    @Test
    void testExecuteCommandAssign() {
        review.addNewPaperTrack("Research", 10, 5);
        review.addPaperToPaperTrack("Saleeb");
        Paper paper = review.getActivePaperTrack().getPapers().get(0);        
        Command command = new Command(Command.CommandValue.ASSIGN, "Reviewer");
        review.executeCommand(paper.getId(), command);
        assertEquals("Reviewing", paper.getState());
        assertEquals("anonymous", paper.getAuthorNames());
    }

    /**
     * Tests deleting a paper by its Id from the active paper track
     */
    @Test
    void testDeletePaperById() {
        review.addNewPaperTrack("Researcher", 10, 5);
        review.addPaperToPaperTrack("Steve");
        Paper paper = review.getActivePaperTrack().getPapers().get(0);
        review.deletePaperById(paper.getId());
        assertNull(review.getActivePaperTrack().getPaperById(paper.getId()));
    }


    /**
     * Tests retrieving papers as a 2D array
     */
    @Test
    void testGetPapersAsArray() {
        review.addNewPaperTrack("Papers", 10, 5);
        review.addPaperToPaperTrack("Steven");
        String[][] arr = review.getPapersAsArray(null);
        assertEquals(1, arr.length);
        assertEquals("Steven", arr[0][2]);
    }

    /**
     * Tests getting the paper track list when no tracks exist
     */
    @Test
    void testGetPaperTrackListEmpty() {
        WolfReview empty = WolfReview.getInstance();
        empty.resetManager();
        empty = WolfReview.getInstance();
        assertEquals(0, empty.getPaperTrackList().length);
    }
    
    /**
     * Tests getPaperById method when there are no active track
     */
    @Test
    void testGetPaperByIdNoActiveTrack() {
        assertNull(review.getPaperById(1));
    }
    
    /**
     * Tests getPaperById method when there are no active track
     */
    @Test
    void testSavePaperTracksToFileNoActiveTrack() {
        assertThrows(IllegalArgumentException.class, () -> {
            review.savePaperTracksToFile("test.txt");
        });
    }
    
    /**
     * Tests addNewPaperTrack method when it has a null or empty name
     */
    @Test
    void testAddNewPaperTrackNullOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            review.addNewPaperTrack(null, 10, 5);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            review.addNewPaperTrack("", 10, 5);
        });
    }
    
    /**
     * Tests addNewPaperTrack method when it has a duplicate name
     */
    @Test
    void testAddNewPaperTrackDuplicateName() {
        review.addNewPaperTrack("Research", 10, 5);

        assertThrows(IllegalArgumentException.class, () -> {
            review.addNewPaperTrack("Research", 15, 10);
        });
    }
    
    /**
     * Tests loading a file that does not exist 
     */
    @Test
    void testLoadTracksFromFileWithInvalidFile() {
        assertThrows(IllegalArgumentException.class, () -> {
            review.loadTracksFromFile("file.txt");
        }); 
    }
     
    /**
     * Tests loading an existing file
     */
    @Test
    void testLoadTracksFromFileWithTracks() {
      
        String validFile = "test-files/paperTrack1.txt";
        review.loadTracksFromFile(validFile);
        assertTrue(review.getPaperTrackList().length > 0);
        assertNotNull(review.getActivePaperTrack());
    }
  
}

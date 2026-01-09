package edu.ncsu.csc216.wolf_review.model.manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * Tests the PaperTrack class in the WolfReview system.
 * Each test makes sure that the PaperTrack maintains a consistent
 * state, behave correctly and throws exceptions when something goes wrong.
 * @author Steven Saleeb
 */
class PaperTrackTest { 

	/**
	 * A Paper Track object used in tests to manage papers
	 */
    private PaperTrack track;
    
    /**
     * Sets up a new Paper Track before each test
     */
    @BeforeEach
    void setUp() {
        track = new PaperTrack("Research Track", 10, 5);
    }

    /**
     * Tests the constructor and getter methods for PaperTrack
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("Research Track", track.getTrackName());
        assertEquals(10, track.getPageLimit());
        assertEquals(5, track.getPayRateExtraPages());
        assertTrue(track.getPapers().isEmpty());
    }

    /**
     * Tests adding a paper using author name string
     */
    @Test
    void testAddPaperByString() {
        int id = track.addPaper("Steven");
        assertEquals(1, track.getPapers().size());
        assertEquals(id, track.getPapers().get(0).getId());
        assertEquals("Steven", track.getPapers().get(0).getAuthorNames());
    }

    /**
     * Tests adding a paper using a Paper object
     */
    @Test
    void testAddPaperByPaper() {
        Paper p = new Paper("Mario");
        int id = track.addPaper(p);
        assertEquals(1, track.getPapers().size());
        assertEquals(p, track.getPapers().get(0));
        assertEquals(id, p.getId());
    }

    /**
     * Tests retrieving a paper by its Id
     */
    @Test
    void testGetPaperById() {
        Paper p = new Paper("Malak");
        track.addPaper(p);
        assertEquals(p, track.getPaperById(p.getId()));
        assertNull(track.getPaperById(999));
    }

    /**
     * Tests deleting a paper by its Id
     */
    @Test
    void testDeletePaperById() {
        Paper p = new Paper("Saleeb");
        track.addPaper(p);
        assertEquals(1, track.getPapers().size());
        track.deletePaperById(p.getId());
        assertTrue(track.getPapers().isEmpty());
    }

    /**
     * Tests the toString method for correct output format
     */
    @Test
    void testToString() {
    	PaperTrack p = new PaperTrack("Steven", 10, 11);
        assertEquals("Steven,10,11", p.toString());
    }

    /**
     * Tests invalid constructor arguments for proper exceptions
     */
    @Test
    void testInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack(null, 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack("", 10, 5));
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack("Paper", 2, 5));
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack("Track", 21, 5));
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack("P", 10, 0));
        assertThrows(IllegalArgumentException.class, () -> new PaperTrack("T", 10, 51));
    }

    /**
     * Tests adding papers with invalid input and throws exceptions
     */
    @Test
    void testAddPaperInvalid() {
        assertThrows(IllegalArgumentException.class, () -> track.addPaper(""));
        assertThrows(IllegalArgumentException.class, () -> track.addPaper((String) null));
    }
    
   
}

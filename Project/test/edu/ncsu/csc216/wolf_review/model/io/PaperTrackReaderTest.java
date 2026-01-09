package edu.ncsu.csc216.wolf_review.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.manager.PaperTrack;


/**
 * Tests the PaperTrackReader class in the WolfReview system.
 * Each test ensures that the tracks are correctly loaded, the data is accurate, 
 * and errors are properly handled.
 * @author Steven Saleeb
 */ 
public class PaperTrackReaderTest {

    /**
     * Tests reading multiple tracks with multiple papers
     * @throws Exception if reading the test file fails
     */
    @Test
    public void testMultipleTracksAndPapers() throws Exception {
       ArrayList<PaperTrack> paperTrack = PaperTrackReader.readPaperTrackFile("test-files/paperTrack2.txt");
       assertEquals(4, paperTrack.size());
       PaperTrack p = paperTrack.get(0);

        
       	assertEquals("Research Track", p.getTrackName());
		assertEquals(10, p.getPageLimit());
		assertEquals(11, p.getPayRateExtraPages());
		assertEquals(7, p.getPapers().size());
		
		p = paperTrack.get(1);
		
		assertEquals("Position paper", p.getTrackName());
		assertEquals(12, p.getPageLimit());
		assertEquals(15, p.getPayRateExtraPages());
		assertEquals(6, p.getPapers().size());
		
		p = paperTrack.get(2);
		
		assertEquals("Experience report", p.getTrackName());
		assertEquals(8, p.getPageLimit());
		assertEquals(15, p.getPayRateExtraPages());
		assertEquals(3, p.getPapers().size());
		
		p = paperTrack.get(3);
		
		assertEquals("Curricula Initiative", p.getTrackName());
		assertEquals(15, p.getPageLimit());
		assertEquals(16, p.getPayRateExtraPages());
		assertEquals(2, p.getPapers().size());
    }

    /**
     * Tests reading a track that has no papers
     * @throws Exception if temporary file creation or reading fails
     */
    @Test
    public void testTrackWithoutPapers() throws Exception {
        String content = "# Empty,10,5\n";

        Path path = Files.createTempFile("empty_track_", ".txt");
        Files.write(path, content.getBytes(StandardCharsets.UTF_8));

        ArrayList<PaperTrack> tracks = PaperTrackReader.readPaperTrackFile(path.toString());
        assertEquals(0, tracks.size());
    }

    
    /**
     * Tests reading an empty file
     * @throws Exception if temporary file creation or reading fails
     */
    @Test
    public void testEmptyFile() throws Exception {
        Path path = Files.createTempFile("Empty_file", ".txt");
        Files.write(path, "".getBytes(StandardCharsets.UTF_8));

        ArrayList<PaperTrack> tracks = PaperTrackReader.readPaperTrackFile(path.toString());
        assertTrue(tracks.isEmpty());
    }

   
    /**
     * Tests reading a missing file and makes sure an 
     * IllegalArgumentException is thrown with the correct message
     */
    @Test
    public void testMissingFileThrowsException() {
        String p = "file.txt";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> PaperTrackReader.readPaperTrackFile(p));
        assertEquals("Unable to load file.", ex.getMessage());
    }
}

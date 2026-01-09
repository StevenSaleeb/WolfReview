package edu.ncsu.csc216.wolf_review.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_review.model.manager.PaperTrack;


/**
 * Tests the PaperTrackWriter class in the WolfReview system.
 * Each test makes sure that files are correctly created, written, and validated, 
 * and that errors are handled properly
 * @author Steven Saleeb
 */
public class PaperTrackWriterTest {

    /**
     * Tests writing an empty list of PaperTracks to a file and makes sure
     * the file is created and remained empty
     * @throws Exception if temporary file fails
     */
    @Test
    public void testWriteEmptyList() throws Exception {
        Path path = Files.createTempFile("empty_tracks", ".txt");
        ArrayList<PaperTrack> tracks = new ArrayList<>();

        PaperTrackWriter.writePaperTracksToFile(path.toString(), tracks);

        String content = Files.readString(path);
        assertTrue(content.isEmpty(), "File should be empty");
    }  

    
    /**
     * Tests writing a PaperTrack that has one paper and makes sure that
     * the track is correctly written to the file
     */
    @Test
    public void testWriteTrackWithOnePaperTrack() {
        ArrayList<PaperTrack> tracks = PaperTrackReader.readPaperTrackFile("test-files/paperTrack1.txt");
        PaperTrackWriter.writePaperTracksToFile("test-files/paper1.txt", tracks);
        checkFiles("test-files/paperTrack1.txt", "test-files/paper1.txt");
        
    }  

    /**
     * Tests writing to an invalid path and makes sure an 
     * IllegalArgumentException is thrown with the correct message
     * @throws Exception if temporary file fails
     */
    @Test
    public void testWriteThatThrowsException() throws Exception {
        Path path = Files.createTempDirectory("invalid_writer");

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            PaperTrackWriter.writePaperTracksToFile(path.toString(), new ArrayList<>());
        });
        assertEquals("Unable to save file.", e.getMessage());
    }
    
    /**
	 * Helper method to compare two files for the same contents
	 * @param expFile expected output
	 * @param actFile actual output
	 */
    @Test
    private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new File(expFile));
			 Scanner actScanner = new Scanner(new File(actFile));) {
			
			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}
			
			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

   
}

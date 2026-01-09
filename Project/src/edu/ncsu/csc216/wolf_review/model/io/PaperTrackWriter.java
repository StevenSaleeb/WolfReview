/**
 * 
 */
package edu.ncsu.csc216.wolf_review.model.io;


import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import edu.ncsu.csc216.wolf_review.model.manager.PaperTrack;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;


/**
 * This class converts paperTrack objects into a text format for
 * saving them to a file and each paper track and its papers are written in a specific
 * format that can be read by the PaperTrackReader
 * @author Steven Saleeb
 */
public class PaperTrackWriter {
	
	/**
	 * Writes the given list of PaperTracks to the specified file.
	 * Each PaperTrack and its Papers are written in the proper format that the PaperTrackReader can read later
	 * @param fileName the name of the file
	 * @param paperTracks the list of paperTrack objects to write
	 * @throws IllegalArgumentException if the file cannot be created or written to
	 */
	public static void writePaperTracksToFile(String fileName, ArrayList<PaperTrack> paperTracks ) {
		
		PrintStream write;
		
		try {
			write = new PrintStream(new File(fileName));
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to save file.");
		}
		
		ArrayList<PaperTrack> papers = paperTracks;
		
			for (int i = 0; i < papers.size(); i++) {
				PaperTrack p = papers.get(i);
				List<Paper> ps = p.getPapers();
				if (ps.size() != 0) {
					write.println("# " + p.toString());
					for (int k = 0; k < ps.size(); k++) {
						Paper paper = ps.get(k);
						write.println("* " + paper.toString());
					}
				}
				
				
			} 
			write.close();
	} 
}

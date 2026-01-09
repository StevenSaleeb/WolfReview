
package edu.ncsu.csc216.wolf_review.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_review.model.manager.PaperTrack;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * This class reads the data from a text file and converts it into PaperTrack
 * objects and each containing a list of Paper objects which ensures that only valid
 * PaperTracks and Papers are created.
 * This class helps the WolfReview system load saved paper information
 * @author Steven Saleeb
 */
public class PaperTrackReader {

	/**
	 * Reads a file containing multiple paper tracks and returns a list of PaperTrack objects
	 * Each paper track starts with a line that begins with a "#" and has the track information
	 * and the following lines start with "*" are papers that belongs to that track
	 * @param fileName the name of the file to read
	 * @return a list of paper track objects
	 * @throws IllegalArgumentException if the file cannot be opened
	 */
	public static ArrayList<PaperTrack> readPaperTrackFile(String fileName) {
		ArrayList<PaperTrack> tracks = new ArrayList<PaperTrack>();
		
		String file = "";
		try {
			FileInputStream theReader = new FileInputStream(fileName);
            Scanner in = new Scanner(theReader);
            
            while(in.hasNextLine()) {
            	file += in.nextLine() + "\n";
            }
            in.close();
            file = file.trim();
            if (file.isEmpty() || file.charAt(0) != '#') {
            	return tracks;
            }
            
            Scanner scanner = new Scanner(file);
            scanner.useDelimiter("\\r?\\n?[#]");
            
            while (scanner.hasNext()) {
            	String string = scanner.next().trim();
            	if (!string.isEmpty()) {
            		PaperTrack track = processPaperTrack(string);
            		if(track != null) {
            			tracks.add(track);
            		}
            	}
            }
            scanner.close();
            
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		return tracks;
	}
	
	/**
	 * Processes the text of a single paper track and converting it into a PaperTrack object and 
	 * it also reads all the papers that belong to that track and adds them
	 * @param paperTrackText the text representing the paper track from the file
	 * @return a paper track object 
	 */
	private static PaperTrack processPaperTrack(String paperTrackText) {
		
		if (paperTrackText == null || paperTrackText.isEmpty()) {
		    return null;
		}
		
		Scanner in = new Scanner(paperTrackText);
		in.useDelimiter("\\r?\\n");
		
		if (!in.hasNext()) {
	        in.close();
	        return null;
	    }
		
		PaperTrack paperTrack = processPaperTrackLine(in.next().trim());
		
		if (paperTrack == null) {
		    in.close();
		    return null;
		}
		
		boolean papers = false;
	    
		while (in.hasNext()) {
			String line = in.next().trim();
	        if (line.isEmpty()) {
	            continue;
	        }
	        if (line.isEmpty() || !line.startsWith("*")) {
	            continue;
	        }
	        line = line.substring(1).trim();
	        
	        try {
	            Paper paper = processPaper(line);
	            if (paper != null) {
	            	 if (paperTrack.getPaperById(paper.getId()) != null) {
	                     continue; 
	                 }
	            	 
	            	paperTrack.addPaper(paper);
	            	papers = true;
	            }
	        } catch (Exception e) {  
	          continue;
	        }
	   }
	    in.close();
	    
	    if (!papers) {
	        return null;
	    }
	    return paperTrack;
	}
	
	
	/**
	 * Processes the first line of a paper track to create a PaperTrack object
	 * @param paperTrackLine object with the specified name, page limit, and pay rate
	 * @return a string paperTrackLine 
	 */
	private static PaperTrack processPaperTrackLine(String paperTrackLine) {
		
		if (paperTrackLine == null || paperTrackLine.trim().isEmpty()) {
			return null;
		}
		
		String line = paperTrackLine;
		
		if (line.startsWith("#")) {
			line = paperTrackLine.substring(1).trim();
		}
		try {
			
		 String[] lineParts = paperTrackLine.split(",");
		 if (lineParts.length != 3) {
		      return null; 
		  }
		 String authorNames = lineParts[0].trim();
		 int pageLimit = Integer.parseInt(lineParts[1].trim());
		 int extraPagesPayRate = Integer.parseInt(lineParts[2].trim());
		 
		 return new PaperTrack(authorNames, pageLimit, extraPagesPayRate);
		 
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	/**
	 * Processes a single line of text representing a Paper and creates it 
	 * @param paperLine the line of text representing one paper 
	 * @return a paper object created from the line
	 * @throws IllegalArgumentException if the format is incorrect
	 */
	private static Paper processPaper(String paperLine) {
		if (paperLine == null || paperLine.trim().isEmpty()) {
			return null;
		}
		
		Scanner in = new Scanner(paperLine);
		in.useDelimiter(",");
		
		int id = in.nextInt();
		
		String state = in.next().trim();
		
		String authorNames = in.next().trim();
		
		String paperType = in.next().trim();
		
		boolean processed = in.nextBoolean();
		
		String reviewer = in.next().trim();
		
		String note = "";
		
		if(in.hasNext()) {
			note = in.next();
		}
		if (in.hasNext()) {
			in.close();
			throw new IllegalArgumentException("");
		}
		
		Paper paper = new Paper(id, state, authorNames, paperType, processed, reviewer, note);
		in.close();
		return paper;
		
	}
		
	
}

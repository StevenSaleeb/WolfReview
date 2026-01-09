
package edu.ncsu.csc216.wolf_review.model.manager;

import java.util.ArrayList;
import java.util.List;


import edu.ncsu.csc216.wolf_review.model.command.Command;
import edu.ncsu.csc216.wolf_review.model.io.PaperTrackReader;
import edu.ncsu.csc216.wolf_review.model.io.PaperTrackWriter;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * This class is the main manager for the entire WolfReview system, 
 * it keeps track of multiple PaperTracks and provides methods to
 * load tracks from a file, save tracks, add new tracks, 
 * and it ensures only one instance exists throughout the program
 * @author Steven Saleeb
 */
public class WolfReview {
	
	/** The singleton instance of WolfReview  */
	private static WolfReview singleton;
	
	/** List of all PaperTracks in the system */
	private ArrayList<PaperTrack> paperTracks;
	
	/** The active PaperTrack */
	private PaperTrack activePaperTrack;

	/**
	 * Private constructor to prevent multiple instances from being created.
	 */
	private WolfReview() {
		paperTracks = new ArrayList<>();
	    activePaperTrack = null;
	 
	}

	/**
	 * Returns the instance of WolfReview
	 * @return the single instance of WolfReview
	 */
	public static synchronized WolfReview getInstance() {
		if (singleton == null) {
			singleton = new WolfReview();
		}
		return singleton;
	
	}

	/**
	 * Loads PaperTracks from a file and makes the first one active
	 * @param fileName the file to read PaperTracks from
	 */
	public void loadTracksFromFile(String fileName) {
		ArrayList<PaperTrack> tracks = PaperTrackReader.readPaperTrackFile(fileName);
		for (int i = 0; i < tracks.size(); i++) {
			paperTracks.add(tracks.get(i));
		}
		if (tracks.isEmpty()) {
			return;	
		}
		activePaperTrack = tracks.get(0);
		
	}

	/**
	 * Saves all PaperTracks to a file
	 * @param fileName the file to save PaperTracks to
	 * @throws IllegalArgumentException if no track is active
	 */
	public void savePaperTracksToFile(String fileName) {
	if (activePaperTrack == null) {
		throw new IllegalArgumentException("Unable to save file.");
	}
	PaperTrackWriter.writePaperTracksToFile(fileName, paperTracks);
	}

	/**
	 * Adds a new PaperTrack to the system and makes it active
	 * @param paperTrackName the name of the new PaperTrack
	 * @param pageLimit the maximum page limit for papers in this track
	 * @param payRateExtraPages the payment rate for extra pages
	 * @throws IllegalArgumentException if the name is invalid or already used
	 */
	public void addNewPaperTrack(String paperTrackName, int pageLimit, int payRateExtraPages) {
		if (paperTrackName == null || paperTrackName.isEmpty()) {
            throw new IllegalArgumentException("Paper Track cannot be created.");
        }
		for (PaperTrack track : paperTracks) {
            if (track.getTrackName().equalsIgnoreCase(paperTrackName)) {
                throw new IllegalArgumentException("Paper Track cannot be created.");
            }
		}
		PaperTrack newPaperTracks = new PaperTrack(paperTrackName, pageLimit, payRateExtraPages);
		paperTracks.add(newPaperTracks);
		loadTrack(paperTrackName);

	}

	/**
	 * Loads an existing PaperTrack and sets it as the active one
	 * @param paperTrackName the name of the track to load
	 * @throws IllegalArgumentException if the track name does not exist
	 */
	public void loadTrack(String paperTrackName) {
		for (PaperTrack track : paperTracks) {
			if(track.getTrackName().equals(paperTrackName)) {
				activePaperTrack = track;
				activePaperTrack.setPaperId();
				return;
			}
		}
		throw new IllegalArgumentException("Paper Track not available."); 
	}

	/**
	 * Returns the name of the currently active PaperTrack
	 * @return the active name of PaperTrack
	 */
	public String getActivePaperTrackName() {
		if (activePaperTrack == null) {
			return null;
		}
		return activePaperTrack.getTrackName();
	}	

	/**
	 * Returns the currently active PaperTrack object
	 * @return the active PaperTrack or null if nothing is active
	 */
	public PaperTrack getActivePaperTrack() {
		if (activePaperTrack == null) {
			return null;
		}
		return activePaperTrack;
	}	

	/**
	 * Returns an array of names of all PaperTracks
	 * @return a String array of PaperTrack names
	 */
	public String[] getPaperTrackList() {
		String[] namesList = new String [paperTracks.size()];
		for (int i = 0; i < paperTracks.size(); i++) {
			namesList[i] = paperTracks.get(i).getTrackName();
		}
		return namesList;
	}

	/**
	 * Adds a new Paper to the active PaperTrack using author names
	 * @param authorNames the names of the authors for the new Paper
	 */
	public void addPaperToPaperTrack(String authorNames) {
		if (activePaperTrack != null) {
			activePaperTrack.addPaper(authorNames);
		}
		
		
	}

	/**
	 * Perform a command on a Paper in the active PaperTrack
	 * @param id id the id of the Paper to update
	 * @param c the command to execute on the paper
	 */
	public void executeCommand(int id, Command c) {
		if (activePaperTrack != null) {
			activePaperTrack.executeCommand(id, c);
		}
		
	}

	/**
	 * Deletes a Paper from the active PaperTrack by its id
	 * @param id the id of the Paper to delete
	 */
	public void deletePaperById(int id) {
		if (activePaperTrack != null) {
			activePaperTrack.deletePaperById(id);
		}
	}
	 
	/**
	 * Returns a 2D array of the papers data in the active track, filtered by a given state
	 * @param stateName the name of the state to filter by
	 * @return a 2D array of Paper data
	 */
	public String[][] getPapersAsArray(String stateName) {
		if (activePaperTrack == null) {
			return null;
		}
		
		List<Paper> papers = activePaperTrack.getPapers();
		
	    ArrayList<Paper> thePapers = new ArrayList<>();
	     
	    for (Paper p : papers) {
	    	if (p == null) {
	    		
	    		continue;
	    	}
	    	
	    	if (stateName == null) {
	    		
	    		thePapers.add(p);
	    		
	    	} else if ("All".equals(stateName)) {
	    		
	    		thePapers.add(p);
	    		
	    	} else if (stateName.equals(p.getState())) {
	    		
	    		thePapers.add(p);
	    	}
	    }
	    
	    String[][] results = new String[thePapers.size()][4];
	    
	    for (int i = 0; i < thePapers.size(); i++) {
	    	Paper paper = thePapers.get(i);
	    	results[i][0] = Integer.toString(paper.getId());
	    	results[i][1] = paper.getState();
	    	results[i][2] = paper.getAuthorNames();
	    	results[i][3] = paper.getPaperType();
	    }
	    
		return results;
	}

	/**
	 * Returns a paper by its id from the active PaperTrack
	 * @param id the id of the Paper
	 * @return the Paper with the given id or null if it is not found
	 */
	public Paper getPaperById(int id) {
		if (activePaperTrack == null) {
			return null;
		}
		return activePaperTrack.getPaperById(id);
	}

	/**
	 * Resets the manager by removing its singleton instance
	 */
	protected void resetManager() {
		singleton = null;
	}

}

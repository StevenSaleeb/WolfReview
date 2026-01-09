
package edu.ncsu.csc216.wolf_review.model.manager;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.wolf_review.model.command.Command;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * PaperTrack holds information like its name, page limit, extra pages pay rate, 
 * and a list of papers and it allows adding, removing, retrieving, and updating papers
 * within the track
 * @author Steven Saleeb
 */
public class PaperTrack {

	/** List of Papers in this track */
	private List<Paper> papers;
	
	/** Name of the paper track */
	private String paperTrackName;

	/** Maximum allowed number of pages for papers */
	private int pageLimit;

	/** Payment rate for extra pages after the limit */
	private int extraPagesPayRate;

	/**
	 * Constructs a new PaperTrack with the given name, page limit, and extra page pay rate
	 * @param paperTrackName the name of the paper track
	 * @param pageLimit the maximum page limit for papers
	 * @param extraPagesPayRate the payment rate for extra pages
	 * @throws IllegalArgumentException if any parameter is invalid
	 */
	public PaperTrack(String paperTrackName, int pageLimit, int extraPagesPayRate) {
		setPaperTrackName(paperTrackName);
		setPageLimit(pageLimit);
		setPayRateExtraPages(extraPagesPayRate);
		papers = new ArrayList<Paper>();
		
		}  

	/**
	 * Sets the counter for paper id and update it based on the largest current id, 
	 * and this ensures that new papers get a unique id
	 */
	public void setPaperId() {
		int max = 0;
		
		for (int i = 0; i < papers.size(); i++) {
			Paper paper = papers.get(i);
			if (paper.getId() > max) {
				max = paper.getId();
			}
		}
		Paper.setCounter(max + 1);
	}

	/**
	 * Sets the name of the PaperTrack
	 * @param paperTrackName the name of the paper track
	 * @throws IllegalArgumentException if the name is null or empty
	 */
	private void setPaperTrackName(String paperTrackName) {
		
		if (paperTrackName == null || paperTrackName.isEmpty()) {
			throw new IllegalArgumentException("Paper Track cannot be created.");
		}
		this.paperTrackName = paperTrackName;
	}

	/**
	 * Returns the name of the PaperTrack
	 * @return the paper track name
	 */
	public String getTrackName() {
		return paperTrackName;
	}
	
	/**
	 * Sets the page limit for the PaperTrack
	 * @param pageLimit the maximum pages allowed
	 * @throws IllegalArgumentException if the value is outside the valid range
	 */
	private void setPageLimit(int pageLimit) {
		
		if (pageLimit < 3 || pageLimit > 20) {
			throw new IllegalArgumentException("Paper Track cannot be created.");
		}
		
		this.pageLimit = pageLimit;
	}

	/**
	 * Returns the page limit for the PaperTrack
	 * @return the maximum page limit
	 */
	public int getPageLimit() {
		return pageLimit;
	}

	/**
	 * Sets the extra page pay rate
	 * @param payRate the payment rate for extra pages
	 * @throws IllegalArgumentException if the rate is outside the valid range
	 */
	private void setPayRateExtraPages(int payRate) {
	
		if (payRate < 1 || payRate > 50) {
			throw new IllegalArgumentException("Paper Track cannot be created.");
		}
		
		this.extraPagesPayRate = payRate;
	}

	/**
	 * Returns the extra page pay rate
	 * @return the extra page pay rate
	 */
	public int getPayRateExtraPages() {
		return extraPagesPayRate;
	}

	/**
	 * adds a new Paper to the PaperTrack using author names
	 * @param authorNames the names of the authors
	 * @return the ID of the added Paper
	 * @throws IllegalArgumentException if author names are empty or null
	 */
	public int addPaper(String authorNames) {
		if (authorNames == null || authorNames.isEmpty()) {
			throw new IllegalArgumentException("Paper Track cannot be created.");
		}
		Paper paper = new Paper(authorNames);
		addPaper(paper);
		return paper.getId();
	}

	/**
	 * Adds an existing Paper to the PaperTrack 
	 * @param paper the paper to add 
	 * @return the ID of the added Paper
	 * @throws IllegalArgumentException if a paper with the same ID already exists
	 */
	public int addPaper(Paper paper) {
		for (Paper p : papers) {
			if (p.getId() == paper.getId()) {
				throw new IllegalArgumentException("Paper Track cannot be created.");
			}
		}
		int i = 0;
		while (i < papers.size() && papers.get(i).getId() < paper.getId()) {
			i++;
		}
		papers.add(i, paper);
		
		return paper.getId();
		
	}
	/**
	 * Returns a list of all Papers in the PaperTrack
	 * @return the list of Papers
	 */
	public List<Paper> getPapers() {
		return papers;
	}

	/**
	 * Returns the Paper with the given ID
	 * @param id the id of the Paper to retrieve
	 * @return the Paper with the id
	 */
	public Paper getPaperById(int id) {

		for (Paper paper : papers) {
			if (paper.getId() == id) {
				return paper;
			}
		}
		return null;
	
	}

	/**
	 * Deletes the paper with the given id from the PaperTrack
	 * @param id the id of the Paper to delete
	 */
	public void deletePaperById(int id) {
		for (int i = 0; i < papers.size(); i++) {
			if (papers.get(i).getId() == id) {
				papers.remove(i);
				return;
			}
		}
	}

	/**
	 * Perform a command on the Paper with the given id
	 * @param id the id of the Paper to update
	 * @param c the command to perform
	 */
	public void executeCommand(int id, Command c) {
		 for (Paper paper : papers) {
			 if (paper.getId() == id) {
				 paper.update(c);
				 return;
			 }
		 }
	}

	/**
	 * Returns a string representation of the PaperTrack 
	 * @return a formatted string representing the PaperTrack
	 */
	@Override
	public String toString() {
		String paperTrack = getTrackName() + "," + getPageLimit() + "," + getPayRateExtraPages();
	    return paperTrack;
		} 
	
	}
	
	


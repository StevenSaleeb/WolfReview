package edu.ncsu.csc216.wolf_review.model.paper;

import edu.ncsu.csc216.wolf_review.model.command.Command;

/**
 * This class is responsible to keep track of all the information that is related to the paper 
 * like the paper id, author names, type of paper, processing status, reviewer, 
 * notes, and its current state, and the class uses the state design pattern to handle 
 * the different stages of a paper, such as 
 * Submitted, Reviewing, Revising, Registering, and Closed
 * @author Steven Saleeb
 */
  public class Paper {

  /** A unique id for the paper */
  private int paperId;
  
  /** Names of the authors */
  private String authorNames;
  
  /** Type of the paper */
  private PaperType paperType;
  
  /** Flag indicating whether the paper has been processed */
  private boolean processed;
  
  /** Reviewer assigned to the paper */
  private String reviewer;
  
  /** A note for the paper */
  private String note;
  
  /** Counter for papers */
  private static int counter = 0;
  
  /** Temporary author names */
  private String tempAuthorNames;
  
  /** Constant represents the Abstract paper type */
  public static final String P_ABSTRACT = "Abstract";
  
  /** Constant represents the Full paper type */
  public static final String P_FULLPAPER = "FullPaper";
  
  /** The submitted name */
  public static final String SUBMITTED_NAME = "Submitted";
  
  /** The reviewing name */
  public static final String REVIEWING_NAME = "Reviewing";
  
  /** The revising name */
  public static final String REVISING_NAME = "Revising";
  
  /** The registering name */
  public static final String REGISTERING_NAME = "Registering";
  
  /** The closed name */
  public static final String CLOSED_NAME = "Closed";
  
  /** The withdrawn name */
  public static final String WITHDRAW_CLOSED = "Withdrawn";
  
  /** The accepted name */
  public static final String ACCEPT_CLOSED = "Accepted";
  
  /** The rejected name */
  public static final String REJECT_CLOSED = "Rejected";
  
  /** The duplicate name */
  public static final String DUPLICATE_CLOSED = "Duplicate";
  
  /** The Strong Accept Recommendation */
  public static final String RECOMMEND_STRONG_ACCEPT = "StrongAcceptRecommendation";
  
  /** The Weak Accept Recommendation */
  public static final String RECOMMEND_WEAK_ACCEPT = "WeakAcceptRecommendation";
  
  /** The Weak Reject Recommendation */
  public static final String RECOMMEND_WEAK_REJECT = "WeakRejectRecommendation";
  
  /** The Strong Reject Recommendation */
  public static final String RECOMMEND_STRONG_REJECT = "StrongRejectRecommendation";
  
  /** It is the author name when the actual author name is hidden  */
  public static final String ANONYMOUS = "anonymous";
  
  /** Temporary name for the author*/
  private static final String TEMP_AUTHOR_NAMES = "C. Ross; M. Mendez";
 

  /** Paper types (Abstract and full paper) */ 
  public enum PaperType {
	  /** Type of the paper called abstract */
	  ABSTRACT,
	  /** Type of the paper called full paper */
	  FULLPAPER 
	  }  
  
  /** The current state of the paper */
  private PaperState currentState;
  
  /** The state of the paper */
  private String state;

  /** The submitted state */
  private final SubmittedState submittedState = new SubmittedState();
  
  /** The reviewing state */
  private final ReviewingState reviewingState = new ReviewingState();
  
  /** The revising state */
  private final RevisingState revisingState = new RevisingState();
  
  /** The registering state */
  private final RegisteringState registeringState = new RegisteringState();
  
  /** The closed state */
  private final ClosedState closedState = new ClosedState();

  /**
   * Constructs a paper with author names, the paper receives a unique id number 
   * and starts in the submitted state, it is not yet processed 
   * and sets the paper as an abstract type  
   * @param authorNames the author names for the new paper
   * @throws IllegalArgumentException if the author names are null or empty
   */
    public Paper(String authorNames) {
    this.authorNames = authorNames;
    if (authorNames == null || authorNames.isEmpty()) {
    	throw new IllegalArgumentException("Paper cannot be created.");
    }
    
     this.paperId = counter;
     incrementCounter();
     setAuthorNames(authorNames);
     setState(SUBMITTED_NAME);
     paperType = PaperType.ABSTRACT;
     this.processed = false;
     this.reviewer = null; 
     this.note = null;
     
 } 
 
  /**
   * Creates a paper with full details, This constructor is used when restoring papers that already exist and may be in
   * any of the states it checks that the data is consistent and throws an exception if something is not right.
   * @param id the paper id
   * @param state the state of the paper
   * @param authorNames the names of the authors
   * @param  paperType the paper type 
   * @param processed the processed status
   * @param reviewer the reviewer assign to the the paper 
   * @param note the note of the paper
   * @throws IllegalArgumentException if any information is invalid or inconsistent
   */
    public Paper(int id, String state, String authorNames, String paperType, boolean processed,
    String reviewer, String note) {
    	
    	if (id <= 0 || state == null || state.isEmpty() ||
    	        authorNames == null || authorNames.isEmpty() ||
    	        paperType == null || paperType.isEmpty()) {
    	        throw new IllegalArgumentException("Paper cannot be created.");
    	    }

    	    setState(state);
    	    setProcessed(processed);
    	    setAuthorNames(authorNames);
    	    setPaperType(paperType);
    	    setReviewer(reviewer);
    	    setNote(note);

    	    if (id >= counter) {
    	        setCounter(id + 1);
    	    }

    	    if (getState().equals(REVIEWING_NAME) || getState().equals(REVISING_NAME)
    	            || (getState().equals(SUBMITTED_NAME) || isProcessed())) {
    	        setSavedAuthorNames(TEMP_AUTHOR_NAMES);
    	    }

    	    if (isProcessed() && getState().equals(SUBMITTED_NAME) && reviewer == null) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (isProcessed() && getNote().equals(DUPLICATE_CLOSED)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (!isProcessed() && getNote().equals(REJECT_CLOSED)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REVISING_NAME) && getPaperType().equals(P_ABSTRACT)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REGISTERING_NAME) && getPaperType().equals(P_ABSTRACT)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REVIEWING_NAME) && isProcessed()) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REVIEWING_NAME) && !getAuthorNames().equals(ANONYMOUS)) {
    	        throw new IllegalArgumentException("Paper cannot be created.");
    	    }

    	    if ((getState().equals(REVISING_NAME) && isProcessed())
    	            || (getState().equals(REVISING_NAME) && !getAuthorNames().equals(ANONYMOUS))) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(CLOSED_NAME) && (note == null || note.isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(CLOSED_NAME) && getPaperType().equals(P_ABSTRACT)
    	            && note.equals(ACCEPT_CLOSED)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(CLOSED_NAME) && getAuthorNames().equals(ANONYMOUS)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(SUBMITTED_NAME) && isProcessed()) {
    	        if (reviewer == null || note == null || reviewer.isEmpty() || note.isEmpty()) {
    	            throw new IllegalArgumentException("Invalid information.");
    	        }
    	    }

    	    if (getState().equals(REVIEWING_NAME)
    	            && (getReviewer() == null || getReviewer().isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(SUBMITTED_NAME) && !isProcessed()
    	            && !(reviewer == null || reviewer.isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REVISING_NAME)
    	            && !(getReviewer() == null || getReviewer().isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(REGISTERING_NAME)
    	            && !(getReviewer() == null || getReviewer().isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(CLOSED_NAME) && !isProcessed()
    	            && getNote().equals(ACCEPT_CLOSED)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(SUBMITTED_NAME) && isProcessed()) {
    	    	if (!authorNames.equals(ANONYMOUS)) {
    	            throw new IllegalArgumentException("Invalid information.");
    	        }  
    	        if (!(note.equals(RECOMMEND_STRONG_ACCEPT) || note.equals(RECOMMEND_STRONG_REJECT)
    	                || note.equals(RECOMMEND_WEAK_ACCEPT) || note.equals(RECOMMEND_WEAK_REJECT))) {
    	            throw new IllegalArgumentException("Invalid information.");
    	        } 
    	        
    	    } 

    	    if (getState().equals(REGISTERING_NAME) && getAuthorNames().equals(ANONYMOUS)) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }

    	    if (getState().equals(CLOSED_NAME) && !(reviewer == null || reviewer.isEmpty())) {
    	        throw new IllegalArgumentException("Invalid information.");
    	    }
    	    
    	    setId(id);
    	
    }

    
    /**
     * Sets the unique id number of the paper
     * @param id the id of the paper
     * @throws IllegalArgumentException if the id is negative
     */
    private void setId(int id) {
    	
    if ( id < 0) {
    	throw new IllegalArgumentException("Paper cannot be created.");
    }
    
    if (id >= counter) {
    	 setCounter(id); 
    	}
  	  this.paperId = id;
    }
    
  /**
   * Returns the paper id
   * @return the id of the paper
   */
  public int getId() {
	  return paperId;
  }

  /**
   * Sets the current state by name
   * @param stateValue the state name
   * @throws IllegalArgumentException if the name does not match any state names
   */
  private void setState(String stateValue) { 
	  if (stateValue == null) {
		  throw new IllegalArgumentException("Paper cannot be created.");
	  }
	  switch (stateValue) {
	  
	  	case SUBMITTED_NAME:
	  		currentState = submittedState;
	  		state = stateValue;
	  		break;
	  		
	  	case REVIEWING_NAME:  
	  		currentState = reviewingState;
	  		state = stateValue;
	  		break;
	  	
	  	case REGISTERING_NAME:
	  		currentState = registeringState;
	  		state = stateValue;
	  		break;
	  	
	  	case REVISING_NAME:
	  		currentState = revisingState;
	  		state = stateValue;
	  		break;
	  	
	  	case CLOSED_NAME: 
	  		currentState = closedState;
	  		state = stateValue;
	  		break;
	  	
	  	default:
	  		throw new IllegalArgumentException("Paper cannot be created.");
	  }
	  
  }

  /**
   * Returns the current state name
   * @return the name of current state 
   */
  public String getState() {
	  return state;
  }
  
  /**
   * Sets the paper's author names
   * @param authorNames new author names
   * @throws IllegalArgumentException if null or empty
   */
  private void setAuthorNames(String authorNames) {
	  
	  if (authorNames == null || authorNames.isEmpty()) {
      	throw new IllegalArgumentException("Paper cannot be created.");
      }
	  
	  this.authorNames = authorNames;
  }

  /**
   * Returns the paper's author names
   * @return the author names of the paper
   */
  public String getAuthorNames() {
	  return authorNames;
  }

  /**
   * Sets the paper type
   * @param paperType the type of the paper
   * @throws IllegalArgumentException if null or empty
   */
  private void setPaperType(String paperType) {
	  if (paperType == null || paperType.isBlank()) {
		throw new IllegalArgumentException("Paper cannot be created.");
	  }
	 
	   if (paperType.equalsIgnoreCase(P_FULLPAPER)) {
		  this.paperType = PaperType.FULLPAPER;
	  }
	  else if (paperType.equalsIgnoreCase(P_ABSTRACT)) {
		  this.paperType = PaperType.ABSTRACT;
	  }
	  else {
		  throw new IllegalArgumentException("Paper cannot be created.");
	  }
	 
  }


  /**
   * Returns the type of the paper (Abstract or Full Paper)
   * @return the paper type
   * @throws IllegalArgumentException if the type is not recognized
   */
  public String getPaperType() {
	 if (paperType == PaperType.ABSTRACT) {
		 return P_ABSTRACT;
	 } else if (paperType == PaperType.FULLPAPER) {
		 return P_FULLPAPER; 
	 } else {
		 throw new IllegalArgumentException("Invalid PaperType.");
	 }
	 
  }

  /**
   * Sets whether the paper has been processed
   * @param processed the processed status
   */
  private void setProcessed(boolean processed) {
	  this.processed = processed;
  }
 
  	/**
  	 * Returns whether the paper has been processed
  	 * @return the processed status
  	 */
  	public boolean isProcessed() {
  		return processed;
  }

  	/**
  	 * Sets the reviewer for the paper
  	 * @param reviewer the reviewer for the paper
  	 */
    private void setReviewer(String reviewer) {
    	if (reviewer != null && reviewer.trim().isEmpty()) {
    		 this.reviewer = null;
    		} else {
    		 this.reviewer = reviewer;
    	 }
    } 

    /**
     * Returns the reviewer of the paper
     * @return the reviewer of the paper
     */
    public String getReviewer() {
	  if (reviewer == null) {
		  return "";
	  } else {
    	return reviewer;
	  }
  }

  /**
   * Sets a note for the paper
   * @param note the note for the paper
   * @throws IllegalArgumentException if the note is invalid for the current state
   */
  private void setNote(String note) {
	  if (note == null || note.isEmpty()) {
		  this.note = "";
		  } else if (!(note.equals(REJECT_CLOSED) || note.equals(WITHDRAW_CLOSED) || 
				  note.equals(DUPLICATE_CLOSED) || note.equals(ACCEPT_CLOSED)) && 
				  	getState().equals(CLOSED_NAME)) {
			  throw new IllegalArgumentException("Invalid command.");
		  } else {
		  this.note = note;
		  }
	  }
  

  /**
   * Returns the note of the paper
   * @return the note of the paper or an empty string if it does not exist
   */
  public String getNote() {
	  if (note == null) {
		  return "";
	  }
	  	else {
			  return note;
		  }
  }
  
  /**
   * Sets saved author names temporarily
   * @param savedAuthorNames temporarily saved author names
   */
  private void setSavedAuthorNames(String savedAuthorNames) {
	  tempAuthorNames = savedAuthorNames;
  }
  
  /**
   * Returns saved author names
   * @return saved author names
   */
  public String getSavedAuthorNames() {
	  return tempAuthorNames;
  }
 

  /**
   * Increments the paper counter
   */
  public static void incrementCounter() {
    counter++;
  }

  /**
   * Sets the paper counter to a new value
   * @param newCount the new paper counter
   */
  public static void setCounter(int newCount) {
	  counter = newCount;
  }
  
  	/**
  	 * Returns a string representation of the paper
  	 */
  	@Override
  	public String toString() {
	  return paperId + "," + currentState.getStateName() + "," + getAuthorNames() + "," +
              getPaperType() + "," + processed + "," + getReviewer() + "," + getNote();
  }

  /**
   * Updates the paper based on a command and each paper state decides how it reacts to the command
   * @param c the object of the command
   * @throws IllegalArgumentException if the command is null
   */
  public void update(Command c) {
	  if (c == null) {
	        throw new IllegalArgumentException("Command cannot be null.");
	    }
	    currentState.updateState(c);
  }

  
  /**
	 * Interface for states in the Paper State Pattern.  All 
	 * concrete Paper states must implement the PaperState interface.
	 * The PaperState interface should be a private interface of the 
	 * Paper class.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private interface PaperState {
		
		/**
		 * Update the Paper from the given Command.
		 * An UnsupportedOperationException is thrown if the Command
		 * is not a valid action for the given state.  
		 * @param command Command describing the action that will update the Paper's
		 * state.
		 * @throws UnsupportedOperationException if the Command is not a valid action
		 * for the given state.
		 */
		void updateState(Command command);
		
		/**
		 * Returns the name of the current state as a String.
		 * @return the name of the current state as a String.
		 */
		String getStateName();
		
	}

/**
 * The state of the paper when it has been submitted
 */ 
  private class SubmittedState implements PaperState {

	  /**
	   * Handles what happens when a command is applied to a paper in the Submitted state
	   * @param command the command to process
	   * @throws IllegalArgumentException if the command is invalid for this state
	   */
	  @Override
	  public void updateState(Command command) { 
		  
		  if (command == null || command.getCommand() == null) {
	            throw new UnsupportedOperationException("Invalid command.");
	        }
		  switch(command.getCommand()) {
		  
		  case ASSIGN: 
			   
			  if (isProcessed()) {
				 throw new UnsupportedOperationException("Invalid command.");
			  }
			  
			 
			  setReviewer(command.getCommandInformation());
				
			  
			  if (authorNames != null && !ANONYMOUS.equals(authorNames)) {
				  tempAuthorNames = authorNames;
			  }
			  
             setAuthorNames(ANONYMOUS);
             setProcessed(false);
             setState(REVIEWING_NAME);
			  
              break;
			  
		  case SUBMIT:
			  
			  if (P_ABSTRACT.equals(getPaperType()) && !isProcessed()) {
				  throw new UnsupportedOperationException("Invalid command."); 
			  }
			  
			  if (RECOMMEND_STRONG_REJECT.equals(note) || RECOMMEND_WEAK_REJECT.equals(note)) {
				  throw new UnsupportedOperationException("Invalid command."); 
			  }
			  
			  setState(SUBMITTED_NAME);
			  setProcessed(false);
			  setNote(null);
			  setReviewer(null); 
			  setPaperType(P_FULLPAPER);
			  setAuthorNames(getSavedAuthorNames());
              
              break;
              
		  case ACCEPT: 
			  
			  if (P_ABSTRACT.equals(getPaperType()) || !processed) {
                  throw new UnsupportedOperationException("Invalid command.");
              }
			  
              note = null;
              processed = true;
              
              if (tempAuthorNames != null && !tempAuthorNames.trim().isEmpty()) {
            	    authorNames = tempAuthorNames;
            	    
            	} else {
            		 setAuthorNames(getSavedAuthorNames());
            	    
            	}
              
              	reviewer = null;
              	setState(REGISTERING_NAME);
              	break;
              	
		  case CLOSE:
			  String info = command.getCommandInformation();
			  
			  	if (info == null || info.trim().isEmpty() || (!WITHDRAW_CLOSED.equals(info) && !REJECT_CLOSED.equals(info) 
			  			&& !DUPLICATE_CLOSED.equals(info))) {
			  		throw new UnsupportedOperationException("Invalid command."); 
			  }
			  
			  	if (REJECT_CLOSED.equals(info) && !processed) {
			  		throw new UnsupportedOperationException("Invalid command."); 
			  }
			  	
			  	if (!WITHDRAW_CLOSED.equals(info) && !REJECT_CLOSED.equals(info) && !DUPLICATE_CLOSED.equals(info)) {
		                throw new UnsupportedOperationException("Invalid command.");
		            }
			  
			  	if (DUPLICATE_CLOSED.equals(info) && processed) {
			  		throw new UnsupportedOperationException("Invalid command."); 
			  }
			  
			  	if (REJECT_CLOSED.equals(info)) {
				   
			  	setProcessed(true);
				  
			  } else if (DUPLICATE_CLOSED.equals(info)) {
				  
				setProcessed(false);
				
                setNote(info);
			  }
                  
			  	if (authorNames.equals(ANONYMOUS)) {
	            	  setAuthorNames(getSavedAuthorNames());
	            	    
	            	}  
			  
                   
                  
                String author;
                  
  	            if (tempAuthorNames != null && !tempAuthorNames.trim().isEmpty()) {
  	            	author = tempAuthorNames;         
  	                
  	            } else if (authorNames != null && !authorNames.trim().isEmpty()
  	                    && !ANONYMOUS.equals(authorNames)) {
  	            	author = authorNames;  
  	                 
  	            } else {
  	            	author = TEMP_AUTHOR_NAMES;  
  	            }
  	            setAuthorNames(author);
  	            setReviewer(null);
  	            setNote(command.getCommandInformation());
  	            setState(CLOSED_NAME);
  	            break;
			   
              
           default:
        	   throw new UnsupportedOperationException("Invalid command.");
 		  }
		 
	  }
  
	  /**
	   * Returns the name of the state
	   * @return Submitted
	   */
	  @Override
	  public String getStateName() { 
		  return SUBMITTED_NAME;
		  }
	  }
	
  /**
   * Represents the state of a paper when it is under review	
   */
  private class ReviewingState implements PaperState {
	  
	  /**
	   * Handles what happens when a command is applied to a paper in the Reviewing state
	   * @param command the command to process
	   * @throws IllegalArgumentException if the command is invalid for this state
	   */  
	  @Override
	  public void updateState(Command command) { 
		  
		  if (command == null || command.getCommand() == null) {
	            throw new UnsupportedOperationException("Invalid command.");
	        }
		  
		  switch (command.getCommand()) {
		  
		  case RECOMMEND: 
			  
			  String info = command.getCommandInformation();
			  
			  if (!RECOMMEND_STRONG_ACCEPT.equals(info) && !RECOMMEND_WEAK_ACCEPT.equals(info)
                     && !RECOMMEND_WEAK_REJECT.equals(info) && !RECOMMEND_STRONG_REJECT.equals(info)) {
				  		throw new UnsupportedOperationException("Invalid command.");
			  }
			  
			  note = info; 
			  setState(SUBMITTED_NAME);
			  setProcessed(true);
			  break;
			  
		  case PROPOSE: 
			  
				 if (P_ABSTRACT.equals(getPaperType())) {
					 throw new UnsupportedOperationException("Invalid command.");
				 }
				 setProcessed(false);
				 setAuthorNames(ANONYMOUS);
				 setReviewer(null);
				 setState(REVISING_NAME);
				 break;
			 
		  case CLOSE:
			  
			  String information = command.getCommandInformation();
			  
			  	if (!WITHDRAW_CLOSED.equals(information)) {
				  throw new UnsupportedOperationException("Invalid command.");
			  }
			  setNote(WITHDRAW_CLOSED);
			  setProcessed(false);
			  
			  	if (tempAuthorNames != null && !tempAuthorNames.trim().isEmpty()) {
          	    
				  authorNames = tempAuthorNames;
				  
			 } else {
				 
				 authorNames = TEMP_AUTHOR_NAMES;
          	}
			  	
			  setState(CLOSED_NAME);
			  setReviewer(null);
			  break;
			   
			  default:
				  throw new UnsupportedOperationException("Invalid command."); 
		  }
	  }
	  
	  /**
	   * Returns the name of the state
	   * @return Reviewing
	   */
	  @Override
	  public String getStateName() { 
		  return REVIEWING_NAME; 
	  	}
	  }
	
  /**
   * Represents the state of a paper when authors are revising it
   */
  private class RevisingState implements PaperState {
		
	  /**
	   * Handles what happens when a command is applied to a paper in the Revising state
	   * @param command the command to process
	   * @throws IllegalArgumentException if the command is invalid for this state
	   */  
	  @Override
	  public void updateState(Command command) { 
		  
		  switch (command.getCommand()) {

		  case MODIFY:
			  setState(SUBMITTED_NAME);
			  break;
			  
		  case CLOSE:
			  String information = command.getCommandInformation();
			  
			  if (!WITHDRAW_CLOSED.equals(information)) {
				  throw new UnsupportedOperationException("Invalid command.");
			  }
			  setNote(WITHDRAW_CLOSED);
			  if (tempAuthorNames != null && !tempAuthorNames.trim().isEmpty()) {
          	    
				  authorNames = tempAuthorNames;
          	} else {
          	    authorNames = TEMP_AUTHOR_NAMES;
          	}
			  setState(CLOSED_NAME);
			  reviewer = null;
			  processed = false; 
			  break;
			  
			  default: 
				 throw new UnsupportedOperationException("Invalid command.");  
		  }
		 
	  }
	  
	  /**
	   * Returns the name of the state
	   * @return Revising
	   */
	  @Override
	  public String getStateName() { 
		  return REVISING_NAME; 
	  	}
	  }
	
  /**
   *  Represents the state of a paper when it is being registered  
   */
  private class RegisteringState implements PaperState {
		
	  /**
	   * Handles what happens when a command is applied to a paper in the Registering state
	   * @param command the command to process
	   * @throws IllegalArgumentException if the command is invalid for this state
	   */  
	  @Override
	  public void updateState(Command command) { 
		  
		  if (command.getCommand() == Command.CommandValue.PROCESS && command.getCommandInformation().equals(ACCEPT_CLOSED)) {
			  
			  setNote(ACCEPT_CLOSED);
			  setProcessed(true);
			  setState(CLOSED_NAME);
			  
			 } else {
				  throw new UnsupportedOperationException("Invalid command.");
			  	}
		}
	  
	  /**
	   * Returns the name of the state
	   * @return Registering
	   */
	  @Override
	  public String getStateName() { 
		  return REGISTERING_NAME; 
		  }
	  }
	
   /**
    * Represents the state of a paper that is closed
    */
   private class ClosedState implements PaperState {
	
	  /**
	   * Handles commands for a paper in the Closed state and Since the paper is finalized, all commands are invalid
	   * and will throw an IllegalArgumentException
	   * @param command the command to process 
	   * @throws IllegalArgumentException always
	   */
	  @Override
	  public void updateState(Command command) { 
		  throw new UnsupportedOperationException("Invalid command.");
	  }
	  
	  /**
	   * Returns the name of the state
	   * @return Closed
	   */
	  @Override
	  public String getStateName() { 
		  return CLOSED_NAME; 
		  }
	  
	    }
	  
	 }

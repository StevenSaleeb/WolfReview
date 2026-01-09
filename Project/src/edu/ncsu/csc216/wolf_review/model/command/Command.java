
package edu.ncsu.csc216.wolf_review.model.command;

import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * This class represents a command issued by a user in the WolfReview system and
 * each Command tells the system what action to perform on a Paper
 * @author Steven Saleeb
 * 
 */
public class Command {

	/**
	 * All possible commands values a user can perform on a paper
	 */
	public enum CommandValue { 
		
		/** Command to accept a paper */
		ACCEPT,
		
		/** Command to assign a reviewer */
		ASSIGN, 
		
		/** Command to process a paper */
		PROCESS, 
		
		/** Command to propose a paper */
		PROPOSE, 
		
		/** Command to submit a paper */
		SUBMIT, 
		
		/** Command to modify a paper */
		MODIFY, 
		
		/** Command to recommend a paper */
		RECOMMEND, 
		/** Command to close a paper */
		CLOSE 
		
	} 
	
/** The type of the command that the user will perform on a paper */
private CommandValue command;

/** Extra information some commands need it */
private String commandInformation;


/**
 * Creates a new Command with the given type and optional information
 * This constructor checks that the command and information make sense together,
 * If they don’t match the allowed rules, it throws an IllegalArgumentException.
 * @param command the action the user will perform on a paper
 * @param commandInformation the extra information needed for some commands
 * @throws IllegalArgumentException if the command or information is invalid
 */
	public Command(CommandValue command, String commandInformation) {
	this.command = command;
	this.commandInformation = commandInformation;
	
	if (command == CommandValue.ACCEPT && commandInformation != null) {
	    throw new IllegalArgumentException("Invalid information.");
	}
	if (command == null) { 
		throw new IllegalArgumentException("Invalid information.");
	}
	if ((command == CommandValue.ASSIGN || command == CommandValue.PROCESS || command == CommandValue.RECOMMEND
			|| command == CommandValue.CLOSE) && (commandInformation == null || commandInformation.isEmpty())) {
		    throw new IllegalArgumentException("Invalid information.");
		} 
	
	if ((command == CommandValue.MODIFY || command == CommandValue.SUBMIT || command == CommandValue.PROPOSE)
			&& commandInformation != null && !commandInformation.isEmpty()) {
		throw new IllegalArgumentException("Invalid information.");
	}
	
	if (command == CommandValue.RECOMMEND) {
		if (!commandInformation.equals(Paper.RECOMMEND_STRONG_ACCEPT) &&
		    !commandInformation.equals(Paper.RECOMMEND_WEAK_ACCEPT) &&
		    !commandInformation.equals(Paper.RECOMMEND_WEAK_REJECT) &&
		    !commandInformation.equals(Paper.RECOMMEND_STRONG_REJECT) ) {
					throw new IllegalArgumentException("Invalid information.");
				}
	}
			
	if (command == CommandValue.CLOSE) {
		if (!commandInformation.equals(Paper.WITHDRAW_CLOSED) &&
			!commandInformation.equals(Paper.REJECT_CLOSED) &&
			!commandInformation.equals(Paper.DUPLICATE_CLOSED)) {
				throw new IllegalArgumentException("Invalid information.");
	}
		}
	
	if (command == CommandValue.PROCESS) {
		if (!commandInformation.equals(Paper.ACCEPT_CLOSED)) {
		throw new IllegalArgumentException("Invalid information.");
		} 
	}
	 
}
			
	
	
	/**
	 * Returns the value of the command
	 * @return the command value
	 */
	public CommandValue getCommand() {
		return command;
	
}
	/**
	 * Returns the extra information associated with this command
	 * @return the command information
	 */
	public String getCommandInformation() {
		return commandInformation;
		
	}
	
}

package edu.ncsu.csc216.wolf_review.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.wolf_review.model.command.Command;
import edu.ncsu.csc216.wolf_review.model.manager.WolfReview;
import edu.ncsu.csc216.wolf_review.model.paper.Paper;

/**
 * Container for the WolfReview system that has the menu options for new paper track 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
 */
public class WolfReviewGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Wolf Review";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the add paper track menu item. */
	private static final String ADD_TITLE = "Add Paper Track";
	/** Text for the Load menu item. */
	private static final String LOAD_TITLE = "Load Paper Tracks File";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save Paper Tracks";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new paper track. */
	private JMenuItem itemAddPaperTrack;
	/** Menu item for loading a file containing one or more Paper tracks and their papers. */
	private JMenuItem itemLoadPaperTrack;
	/** Menu item for saving the paper tracks and their papers. */
	private JMenuItem itemSavePaperTrack;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the paper. */
	private JPanel panel;
	/** Constant to identify PaperTrackPanel for CardLayout. */
	private static final String PAPERTRACK_LIST_PANEL = "PaperTrackPanel";
	/** Constant to identify SubmittedPanel for CardLayout. */
	private static final String SUBMITTED_PANEL = "SubmittedPanel";
	/** Constant to identify RegisteringPanel for CardLayout. */
	private static final String REGISTERING_PANEL = "RegisteringPanel";
	/** Constant to identify ReviewingPanel for CardLayout. */
	private static final String REVIEWING_PANEL = "ReviewingPanel";
	/** Constant to identify RevisingPanel for CardLayout. */
	private static final String REVISING_PANEL = "RevisingPanel";
	/** Constant to identify ClosedPanel for CardLayout. */
	private static final String CLOSED_PANEL = "ClosedPanel";
	/** Constant to identify AddPaperPanel for CardLayout. */
	private static final String ADD_PAPER_PANEL = "AddPaper";
	/** Constant to identify AddPaperTrackPanel for CardLayout. */
	private static final String ADD_PAPERTRACK_PANEL = "AddPaperTrack";
	
	/** PaperTrack panel - we only need one instance, so it's final. */
	private final PaperTrackListPanel pnlPaperTrackList = new PaperTrackListPanel();
	/** Submitted panel - we only need one instance, so it's final. */
	private final SubmittedPanel pnlSubmitted = new SubmittedPanel();
	/** Registering panel - we only need one instance, so it's final. */
	private final RegisteringPanel pnlRegistering = new RegisteringPanel();
	/** Reviewing panel - we only need one instance, so it's final. */
	private final ReviewingPanel pnlReviewing = new ReviewingPanel();
	/** Revising panel - we only need one instance, so it's final. */
	private final RevisingPanel pnlRevising = new RevisingPanel();
	/** Closed panel - we only need one instance, so it's final. */
	private final ClosedPanel pnlClosed = new ClosedPanel();

	/** Add Paper panel - we only need one instance, so it's final. */
	private final AddPaperPanel pnlAddPaper = new AddPaperPanel();
	/** Add PaperTrack panel - we only need one instance, so it's final. */
	private final AddPaperTrackPanel pnlAddPaperTrack = new AddPaperTrackPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a WolfReviewGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the WolfReview system.
	 */
	public WolfReviewGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlPaperTrackList, PAPERTRACK_LIST_PANEL);
		panel.add(pnlSubmitted, SUBMITTED_PANEL);
		panel.add(pnlRegistering, REGISTERING_PANEL);
		panel.add(pnlReviewing, REVIEWING_PANEL);
		panel.add(pnlRevising, REVISING_PANEL);
		panel.add(pnlClosed, CLOSED_PANEL);
		panel.add(pnlAddPaper, ADD_PAPER_PANEL);
		panel.add(pnlAddPaperTrack, ADD_PAPERTRACK_PANEL);
		cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Add window listener to save when closing
		addWindowListener(new WindowAdapter() {

			/**
			 * Ask the user to save the papertracks file when closing GUI.
			 * @param e WindowEvent leading to GUI closing
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				WolfReview model = WolfReview.getInstance();
				try {
					model.savePaperTracksToFile(getFileName(false));
				} catch (IllegalArgumentException exp) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, exp.getMessage());
				} catch (IllegalStateException exp) {
					//Don't do anything - user canceled (or error)
				}
			}
			
		});
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading/saving a paperTrack
	 * file containing papers or for quitting the paper.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemAddPaperTrack = new JMenuItem(ADD_TITLE);
		itemLoadPaperTrack = new JMenuItem(LOAD_TITLE);
		itemSavePaperTrack = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemAddPaperTrack.addActionListener(this);
		itemLoadPaperTrack.addActionListener(this);
		itemSavePaperTrack.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSavePaperTrack.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemAddPaperTrack);
		menu.add(itemLoadPaperTrack);
		menu.add(itemSavePaperTrack);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//Use singleton to create/get the sole instance.
		WolfReview model = WolfReview.getInstance();
		if (e.getSource() == itemAddPaperTrack) {
			//Create a new paperTrack
			try {
				cardLayout.show(panel, ADD_PAPERTRACK_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			}
		} else if (e.getSource() == itemLoadPaperTrack) {
			//Load an existing paper track list
			try {
				model.loadTracksFromFile(getFileName(true));
				itemSavePaperTrack.setEnabled(true);
				pnlPaperTrackList.updatePaperTrack();
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSavePaperTrack) {
			//Save paperTracks and papers
			try {
				model.savePaperTracksToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.savePaperTracksToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the WolfReview paper.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new WolfReviewGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the paperTrack and it's list of papers
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class PaperTrackListPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label for selecting active paperTrack */
		private JLabel lblActivePaperTrack;
		/** Combo box for PaperTrack list */
		private JComboBox<String> comboPaperTrackList;
		/** Button to update to the select paperTrack */
		private JButton btnUpdatePaperTrack;
		
		/** Button for creating a new paper */
		private JButton btnAdd;
		/** Button for deleting the selected paper in the list */
		private JButton btnDelete;
		/** Button for editing the selected paper in the list */
		private JButton btnEdit;
		
		/** Label for filter by state */
		private JLabel lblFilterState;
		/** Combo box for states */
		private JComboBox<String> comboFilterState;
		/** Button to update to filter by state */
		private JButton btnFilterState;
		
		/** JTable for displaying the list of papers */
		private JTable tablePapers;
		/** TableModel for papers */
		private PaperTableModel tableModel;
		
		/**
		 * Creates the paperTrack panel with paper list.
		 */
		public PaperTrackListPanel() {
			super(new BorderLayout());
			
			//Set up JPanel for paperTracks
			lblActivePaperTrack = new JLabel("Paper Tracks");
			comboPaperTrackList = new JComboBox<String>();
			btnUpdatePaperTrack = new JButton("Select Paper Track");
			btnUpdatePaperTrack.addActionListener(this);
						
			//Set up the JPanel that will hold action buttons		
			btnAdd = new JButton("Add Paper");
			btnAdd.addActionListener(this);
			btnDelete = new JButton("Delete Paper");
			btnDelete.addActionListener(this);
			btnEdit = new JButton("Edit Paper");
			btnEdit.addActionListener(this);
			
			//Set up JPanel for filter
			lblFilterState = new JLabel("Filter by State");
			comboFilterState = new JComboBox<String>();
			btnFilterState = new JButton("Filter");
			btnFilterState.addActionListener(this);
			
			comboFilterState.addItem("All");
			comboFilterState.addItem(Paper.SUBMITTED_NAME);
			comboFilterState.addItem(Paper.REVIEWING_NAME);
			comboFilterState.addItem(Paper.REVISING_NAME);
			comboFilterState.addItem(Paper.REGISTERING_NAME);
			comboFilterState.addItem(Paper.CLOSED_NAME);
						
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(3, 3));
			pnlActions.add(lblActivePaperTrack);
			pnlActions.add(comboPaperTrackList);
			pnlActions.add(btnUpdatePaperTrack);
			pnlActions.add(btnAdd);
			pnlActions.add(btnDelete);
			pnlActions.add(btnEdit);
			pnlActions.add(lblFilterState);
			pnlActions.add(comboFilterState);
			pnlActions.add(btnFilterState);
						
			//Set up table
			tableModel = new PaperTableModel();
			tablePapers = new JTable(tableModel);
			tablePapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablePapers.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tablePapers.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(tablePapers);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
			
			updatePaperTrack();
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnUpdatePaperTrack) {
				int idx = comboPaperTrackList.getSelectedIndex();
				
				if (idx == -1) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No track selected");
				} else {				
					String paperTrackName = comboPaperTrackList.getItemAt(idx);
					WolfReview.getInstance().loadTrack(paperTrackName);
				}
				updatePaperTrack();
			} else if (e.getSource() == btnFilterState) {
				int idx = comboFilterState.getSelectedIndex();
				
				if (idx == -1) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No state selected");
				} else {				
					String stateName = comboFilterState.getItemAt(idx);
					tableModel.updateData(stateName);
				}
			} else if (e.getSource() == btnAdd) {
				//If the add button is clicked switch to the AddPaperPanel
				cardLayout.show(panel,  ADD_PAPER_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is clicked, delete the paper
				int row = tablePapers.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No paper selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						WolfReview.getInstance().deletePaperById(id);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid id");
					}
				}
				updatePaperTrack();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is clicked, switch panel based on state
				int row = tablePapers.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No paper selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						String stateName = WolfReview.getInstance().getPaperById(id).getState();
						if (stateName.equals(Paper.SUBMITTED_NAME)) {
							cardLayout.show(panel, SUBMITTED_PANEL);
							pnlSubmitted.setInfo(id);
						}
						if (stateName.equals(Paper.REVIEWING_NAME)) {
							cardLayout.show(panel, REVIEWING_PANEL);
							pnlReviewing.setInfo(id);
						}
						if (stateName.equals(Paper.REVISING_NAME)) {
							cardLayout.show(panel, REVISING_PANEL);
							pnlRevising.setInfo(id);
						} 
						if (stateName.equals(Paper.REGISTERING_NAME)) {
							cardLayout.show(panel, REGISTERING_PANEL);
							pnlRegistering.setInfo(id);
						}  
						if (stateName.equals(Paper.CLOSED_NAME)) {
							cardLayout.show(panel, CLOSED_PANEL);
							pnlClosed.setInfo(id);
						} 

						
					} catch (NumberFormatException | NullPointerException nfe) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid id");
					} 
				}
			} 
			WolfReviewGUI.this.repaint();
			WolfReviewGUI.this.validate();
		}
		
		/**
		 * Updates the paper track.
		 */
		public void updatePaperTrack() {
			tableModel.updateData("All");
			
			String paperTrackName = WolfReview.getInstance().getActivePaperTrackName();
			
			if (paperTrackName == null) {
				paperTrackName = "Create a Paper Track";
				btnAdd.setEnabled(false);
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				btnUpdatePaperTrack.setEnabled(false);
				btnFilterState.setEnabled(false);
			} else {
				btnAdd.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
				btnUpdatePaperTrack.setEnabled(true);
				btnFilterState.setEnabled(true);
			}
			
			comboPaperTrackList.removeAllItems();
			String [] paperTrackList = WolfReview.getInstance().getPaperTrackList();
			for (int i = 0; i < paperTrackList.length; i++) {
				comboPaperTrackList.addItem(paperTrackList[i]);
			}
			comboPaperTrackList.setSelectedItem(WolfReview.getInstance().getActivePaperTrackName());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Paper Track: " + paperTrackName);
			setBorder(border);
			setToolTipText("Project: " + paperTrackName);
		}
		
		/**
		 * PaperTableModel is the object underlying the JTable object that displays
		 * the list of papers to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
		 */
		private class PaperTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"ID", "State", "Author Names", "Paper Type"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the PaperTableModel by requesting the latest information
			 * from the PaperTableModel.
			 */
			public PaperTableModel() {
				updateData("All");
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			@Override
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			@Override
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			@Override
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with paper information from the WolfReview system.
			 * @param stateName name of state to filter on. 
			 */
			private void updateData(String stateName) {
				WolfReview m = WolfReview.getInstance();
				data = m.getPapersAsArray(stateName);
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a submitted paper.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class SubmittedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** PaperPanel that presents the paper's information to the user */
		private PaperPanel pnlInfo;
		/** Current paper's id */
		private int id;
		
		/** Label - reviewer */
		private JLabel lblReviewer;
		/** Text Field - reviewer */
		private JTextField txtReviewer;
		/** Button - Assign w/ reviewer */
		private JButton btnAssign;
		
		/** Label - close reason */
		private JLabel lblCLoseReason;
		/** ComboBox - close reason */
		private JComboBox<String> comboCloseReason;
		/** Button - Close w/ close reason */
		private JButton btnClose;
		/** Button - Accept  */
		private JButton btnAccept;
		/** Button - Submit */
		private JButton btnSubmit;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a paper in the SubmittedState
		 */
		public SubmittedPanel() {
			super(new BorderLayout());
			
			pnlInfo = new PaperPanel();		
			
			lblReviewer = new JLabel("Reviewer Id:");
			txtReviewer = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			lblCLoseReason = new JLabel("Close Reason:");
			comboCloseReason = new JComboBox<String>();
			comboCloseReason.addItem("Rejected");
			comboCloseReason.addItem("Withdrawn");
			comboCloseReason.addItem("Duplicate");
			btnClose = new JButton("Close");
			
			btnAccept = new JButton("Accept");
			btnCancel = new JButton("Cancel");
			btnSubmit = new JButton("Submit");
			
			btnAssign.addActionListener(this);
			btnClose.addActionListener(this);
			btnCancel.addActionListener(this);
			btnAccept.addActionListener(this);
			btnSubmit.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAssign = new JPanel();
			pnlAssign.setLayout(new GridLayout(1, 3));
			pnlAssign.add(lblReviewer);
			pnlAssign.add(txtReviewer);
			pnlAssign.add(btnAssign);
			
			JPanel pnlClose = new JPanel();
			pnlClose.setLayout(new GridLayout(1, 3));
			pnlClose.add(lblCLoseReason);
			pnlClose.add(comboCloseReason);
			pnlClose.add(btnClose);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnAccept);
			pnlBtnRow.add(btnSubmit);
			pnlBtnRow.add(btnCancel);
			
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlAssign, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlClose, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
					
			add(pnlInfo, BorderLayout.NORTH);
					
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given paper data.
		 * @param id id of the paper
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setPaperInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {				
				String reviewer = txtReviewer.getText();
				//Try a command.  If problem, go back to paper list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, reviewer);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnClose) {
				int idx = comboCloseReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No close reason selected");
				} else {				
					String closeReason = comboCloseReason.getItemAt(idx);
					//Try a command.  If problem, go back to paper list.
					try {
						Command c = new Command(Command.CommandValue.CLOSE, closeReason);
						WolfReview.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnSubmit) {
				try {
					Command c = new Command(Command.CommandValue.SUBMIT, null);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnAccept) {
				try {
					Command c = new Command(Command.CommandValue.ACCEPT, null);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to paperTrack
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
				txtReviewer.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an paper in the ReviewingState.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class ReviewingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Panel that presents the paper's information to the user */
		private PaperPanel pnlInfo;
		/** Current paper's id */
		private int id;
		
		/** Button - Close */
		private JButton btnClose;
		
		/** Button - Propose */
		private JButton btnPropose;
		
		/** Label - recommendation */
		private JLabel lblRecommendation;
		/** ComboBox - recommendation */
		private JComboBox<String> comboRecommendation;
		/** Button - Recommend w/ recommendation */
		private JButton btnRecommend;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an paper in the Reviewing state
		 */
		public ReviewingPanel() {
			super(new BorderLayout());
			
			pnlInfo = new PaperPanel();		
			
			btnClose = new JButton("Withdraw");
			
			btnPropose = new JButton("Propose");
			
			lblRecommendation = new JLabel("Recommendation:");
			comboRecommendation = new JComboBox<String>();
			comboRecommendation.addItem("Strong Accept");
			comboRecommendation.addItem("Weak Accept");
			comboRecommendation.addItem("Weak Reject");
			comboRecommendation.addItem("Strong Reject");
			btnRecommend = new JButton("Recommend");
			
			btnCancel = new JButton("Cancel");
			
			btnClose.addActionListener(this);
			btnPropose.addActionListener(this);
			btnRecommend.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlReject = new JPanel();
			pnlReject.setLayout(new GridLayout(1, 3));
			pnlReject.add(lblRecommendation);
			pnlReject.add(comboRecommendation);
			pnlReject.add(btnRecommend);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnClose);
			pnlBtnRow.add(btnPropose);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlReject, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given paper data.
		 * @param id id of the paper
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setPaperInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnClose) {				
				//Try a command.  If problem, go back to paper list.
				try {
					Command c = new Command(Command.CommandValue.CLOSE, Paper.WITHDRAW_CLOSED);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnPropose ) {
				//Try a command.  If problem, go back to paper list.
				try {
					Command c = new Command(Command.CommandValue.PROPOSE, null);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnRecommend) {
				int idx = comboRecommendation.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "No recommendation selected");
				} else {				
					String recommendation = comboRecommendation.getItemAt(idx);
					//Try a command.  If problem, go back to paper list.
					if ("Strong Accept".equals(recommendation)) {
						recommendation = Paper.RECOMMEND_STRONG_ACCEPT;
					} else if ("Weak Accept".equals(recommendation)) {
						recommendation = Paper.RECOMMEND_WEAK_ACCEPT;
					} else if ("Weak Reject".equals(recommendation)) {
						recommendation = Paper.RECOMMEND_WEAK_REJECT;
					} else if ("Strong Reject".equals(recommendation)) {
						recommendation = Paper.RECOMMEND_STRONG_REJECT;
					}
					try {
						Command c = new Command(Command.CommandValue.RECOMMEND, recommendation);
						WolfReview.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead back to paper track
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an paper in the RevisingState.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class RevisingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Panel that presents the paper's information to the user */
		private PaperPanel pnlInfo;
		/** Current paper's id */
		private int id;
		
		/** Button - Modify as per proposed changes */
		private JButton btnModify;
		
		/** Button - Withdraw */
		private JButton btnClose;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an paper in the Revising state
		 */
		public RevisingPanel() {
			super(new BorderLayout());
			
			pnlInfo = new PaperPanel();		
			
			btnModify = new JButton("Modify");
			btnClose = new JButton("Withdraw");
			btnCancel = new JButton("Cancel");
			
			btnModify.addActionListener(this);
			btnClose.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnModify);
			pnlBtnRow.add(btnClose);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
			
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given paper data.
		 * @param id id of the paper
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setPaperInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnModify) {				
				//Try a command.  If problem, go back to paper list.
				try {
					Command c = new Command(Command.CommandValue.MODIFY, null);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnClose) {
					try {
						Command c = new Command(Command.CommandValue.CLOSE, Paper.WITHDRAW_CLOSED);
						WolfReview.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
						reset = false;
					}
			}
			if (reset) {
				//All buttons lead back to paper track
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with an paper in the registering state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class RegisteringPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Panel that presents the paper's information to the user */
		private PaperPanel pnlInfo;
		/** Current paper's id */
		private int id;
		
		/** Button - Reopen*/
		private JButton btnProcess;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an paper in the RegisteringState
		 */
		public RegisteringPanel() {
			super(new BorderLayout());
			
			pnlInfo = new PaperPanel();		
			
			btnProcess = new JButton("Process");
			btnCancel = new JButton("Cancel");
			
			btnProcess.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnProcess);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given paper data.
		 * @param id id of the paper
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setPaperInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnProcess) {
				try {
					Command c = new Command(Command.CommandValue.PROCESS, Paper.ACCEPT_CLOSED);
					WolfReview.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead back to paper track
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the paper.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class ClosedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** Panel that presents the paper's information to the user */
		private PaperPanel pnlInfo;
		/** Current Paper's id */
		private int id;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing an paper in the Closed state
		 */
		public ClosedPanel() {
			super(new BorderLayout());
			
			pnlInfo = new PaperPanel();		
			
			btnCancel = new JButton("Cancel");
			
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlCommands.add(pnlBtnRow, c);
						
			add(pnlInfo, BorderLayout.NORTH);
			
			add(pnlCommands, BorderLayout.SOUTH);
			
		}
		
		/**
		 * Set the PaperInfoPanel with the given paper data.
		 * @param id id of the paper
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setPaperInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//All buttons lead back to paper track
			cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
			pnlPaperTrackList.updatePaperTrack();
			WolfReviewGUI.this.repaint();
			WolfReviewGUI.this.validate();
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the paper.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class PaperPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
				
		/** Label - state */
		private JLabel lblState;
		/** JTextField - state */
		private JTextField txtState;
		
		/** Label - first name */
		private JLabel lblAuthorNames;
		/** JTextField - first name */
		private JTextField txtAuthorNames;
		
		/** Label - paper type */
		private JLabel lblPaperType;
		/** JTextField - paper type */
		private JTextField txtPaperType;
		
		/** Label - processed value */
		private JLabel lblProcessed;
		/** JTextField - processed value */
		private JTextField txtProcessed;
		
		/** Label - reviewer */
		private JLabel lblReviwer;
		/** JTextField - reviewer */
		private JTextField txtReviewer;
		
		/** Label - note */
		private JLabel lblNote;
		/** JTextField - note */
		private JTextField txtNote;
		
		/** 
		 * Construct the panel for the information.
		 */
		public PaperPanel() {
			super(new GridBagLayout());
			
			lblState = new JLabel("State: ");
			lblAuthorNames = new JLabel("Author Names: ");
			lblPaperType = new JLabel("Paper Type: ");
			lblProcessed = new JLabel("Processed: ");
			lblReviwer = new JLabel("Reviewer Id: ");
			lblNote = new JLabel("Note: ");

			txtState = new JTextField(15);
			txtAuthorNames = new JTextField(15);
			txtPaperType = new JTextField(15);
			txtProcessed = new JTextField(15);
			txtReviewer = new JTextField(15);
			txtNote = new JTextField(15);
			
			txtState.setEditable(false);
			txtAuthorNames.setEditable(false);
			txtPaperType.setEditable(false);
			txtProcessed.setEditable(false);
			txtReviewer.setEditable(false);
			txtNote.setEditable(false);	
			
			GridBagConstraints c = new GridBagConstraints();
						
					
			//Row 1 - state
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblState);
			row2.add(txtState);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row2, c);
			
			//Row 2 - author names
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblAuthorNames);
			row3.add(txtAuthorNames);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row3, c);
			
			//Row 3 - paper type
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblPaperType);
			row4.add(txtPaperType);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row4, c);
			
			//Row 4 - processed value
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(lblProcessed);
			row5.add(txtProcessed);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row5, c);
			
			//Row 5 - reviewer id
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 2));
			row6.add(lblReviwer);
			row6.add(txtReviewer);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row6, c);
			
			//Row 6 - note
			JPanel row7 = new JPanel();
			row7.setLayout(new GridLayout(1, 2));
			row7.add(lblNote);
			row7.add(txtNote);
			
			c.gridx = 0;
			c.gridy = 7;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(row7, c);
		}
		
		/**
		 * Adds information about the paper to the display.  
		 * @param id the id for the paper to display information about.
		 */
		public void setPaperInfo(int id) {
			//Get the paper from the model
			Paper paper = WolfReview.getInstance().getPaperById(id);
			if (paper == null) {
				//If the paper doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(WolfReviewGUI.this, "Invalid id");
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
			} else {
				//Set the border information with the paper track name and paper id
				String paperTrackName = WolfReview.getInstance().getActivePaperTrackName();
				
				Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, paperTrackName + "-  Paper #" + paper.getId());
				setBorder(border);
				setToolTipText(paperTrackName + " paper " + paper.getId() + " - " + paper.getState());
				
				//Set all of the fields with the information
				txtState.setText(paper.getState());
				txtAuthorNames.setText(paper.getAuthorNames());
				txtPaperType.setText(paper.getPaperType());
				if(paper.isProcessed()) {
					txtProcessed.setText("Yes");
				} else {
					txtProcessed.setText("No");
				}
				txtReviewer.setText(paper.getReviewer());
				txtNote.setText(paper.getNote());
				
				if (paper.getState().equals(Paper.CLOSED_NAME)) {
					lblNote.setText("Closing Reason: ");
				} else if (paper.getState().equals(Paper.SUBMITTED_NAME) && paper.isProcessed()) {
					lblNote.setText("Recommendation: ");
				}
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new paper.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AddPaperPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;

		/** Label - author names */
		private JLabel lblAuthorNames;
		/** JTextField - author names */
		private JTextField txtAuthorNames;
		
		/** Button to add a paper */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the JPanel for adding new papers to the 
		 * paper track.
		 */
		public AddPaperPanel() {
			super(new BorderLayout());  
			
			//Construct widgets
			lblAuthorNames = new JLabel("Author Names: ");
						
			txtAuthorNames = new JTextField(15);
						
			btnAdd = new JButton("Submit Paper");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlAdd = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Add Paper");
			pnlAdd.setBorder(border);
			pnlAdd.setToolTipText("Add Paper");
			
			pnlAdd.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - Title
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblAuthorNames);
			row1.add(txtAuthorNames);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row1, c);
			
			//Row 2 - Buttons
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(btnAdd);
			row5.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row5, c);
			
			add(pnlAdd, BorderLayout.NORTH);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				String authorNames = txtAuthorNames.getText();

				//Get instance of model and add paper
				try {
					WolfReview.getInstance().addPaperToPaperTrack(authorNames);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Paper cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back paper list
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
				//Reset fields
				txtAuthorNames.setText("");
			}
		}
	}
	
	
	
	/**
	 * Inner class that creates the JPanel for creating a new paper track.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 * @author Dr. Chandrika Satyavolu (jsatyav@ncsu.edu)
	 */
	private class AddPaperTrackPanel extends JPanel implements ActionListener {
		/** Serial Version UID */
		private static final long serialVersionUID = 1L;
		
		/** Label - paper track name */
		private JLabel lblPaperTrackName;
		/** Text Field - paper track name */
		private JTextField txtPaperTrackName;
		
		/** Label - page limit */
		private JLabel lblpageLimit;
		/** Text Field - page limit */
		private JTextField txtpageLimit;
		
		/** Label - pay rate */
		private JLabel lblPayRate;
		/** Text Field - pay rate */
		private JTextField txtPayRate;
		
		/** Button - add paper track */
		private JButton btnAdd;
		/** Button - cancel */
		private JButton btnCancel;
		
		/**
		 * Constructs the new paper track panel
		 */
		public AddPaperTrackPanel() {
			super(new BorderLayout()); 
			
			lblPaperTrackName = new JLabel("Paper Track Name: ");
			lblpageLimit = new JLabel("Page Limit: ");
			lblPayRate = new JLabel("Extra Pages Pay Rate: ");
			
			txtPaperTrackName = new JTextField(25);
			txtpageLimit = new JTextField(5);
			txtPayRate = new JTextField(5);
			
			btnAdd = new JButton("Add Paper Track");
			btnCancel = new JButton("Cancel");
			
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlAdd = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Add Paper Track");
			pnlAdd.setBorder(border);
			pnlAdd.setToolTipText("Add Paper Track");
			
			pnlAdd.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			//Row 1 - paper track name
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblPaperTrackName);
			row1.add(txtPaperTrackName);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 0;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row1, c);
			
			//Row 2 - page limit
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblpageLimit);
			row2.add(txtpageLimit);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 0;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row2, c);
			
			//Row 3 - pay rate 
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblPayRate);
			row3.add(txtPayRate);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 0;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row3, c);
			
			//Row 4 - buttons
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(btnAdd);
			row4.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 20;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			pnlAdd.add(row4, c);
			
			add(pnlAdd, BorderLayout.NORTH);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				try {
					String paperTrackName = txtPaperTrackName.getText();
					int pageLimit = Integer.parseInt(txtpageLimit.getText());
					int payRate = Integer.parseInt(txtPayRate.getText());
					
					//Get instance of model and add paper
					WolfReview.getInstance().addNewPaperTrack(paperTrackName, pageLimit, payRate);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(WolfReviewGUI.this, "Paper Track cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back paper list
				cardLayout.show(panel, PAPERTRACK_LIST_PANEL);
				pnlPaperTrackList.updatePaperTrack();
				WolfReviewGUI.this.repaint();
				WolfReviewGUI.this.validate();
				//Reset fields
				txtPaperTrackName.setText("");
				txtpageLimit.setText("");
				txtPayRate.setText("");
			}
		}
		
		
	}
}
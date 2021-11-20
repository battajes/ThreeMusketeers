package assignment2;

import javafx.animation.PauseTransition;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.CipherOutputStream;

public class View {

    ThreeMusketeers model;
    Stage stage;
    BorderPane borderPane;

    ThreeMusketeers.GameMode gameMode;

    Label messageLabel = new Label("");
    Label gameModeLabel = new Label("");
    BoardPanel boardPanel;
    Button undoButton, saveButton, restartButton;
    TextField saveFileNameTextField;
    Label saveFileErrorLabel;

    // must use these strings to update saveFileErrorLabel when saving a board
    static String saveFileSuccess = "Saved board";
    static String saveFileExistsError = "Error: File already exists";
    static String saveFileNotTxtError = "Error: File must end with .txt";

    public View(ThreeMusketeers model, Stage stage) {
        this.model = model;
        this.stage = stage;
        initUI();
    }

    /**
     * Initializes the UI and shows the main menu
     *
     * Contains default alignment and styles which can be modified
     */
    private void initUI() {
        borderPane = new BorderPane();

        // DO NOT MODIFY IDs
        borderPane.setId("BorderPane");  // DO NOT MODIFY ID
        gameModeLabel.setId("GameModeLabel");  // DO NOT MODIFY ID
        messageLabel.setId("MessageLabel"); // DO NOT MODIFY ID

        var threeMusketeersLabel = new Label("Three Musketeers");

        // Default styles which can be modified

        borderPane.setStyle("-fx-background-color: #121212;");

        threeMusketeersLabel.setFont(new Font(30));
        threeMusketeersLabel.setStyle("-fx-text-fill: #e8e6e3");

        gameModeLabel.setText("");
        gameModeLabel.setFont(new Font(20));
        gameModeLabel.setStyle("-fx-text-fill: #e8e6e3");

        messageLabel.setFont(new Font(20));
        messageLabel.setStyle("-fx-text-fill: #e8e6e3");

        VBox labels = new VBox(threeMusketeersLabel, gameModeLabel);
        labels.setAlignment(Pos.TOP_CENTER);
        borderPane.setTop(labels);

        showMainMenu();

        var scene = new Scene(borderPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Updates the view to show the Main menu
     */
    private void showMainMenu(){
        ModeInputPanel modeInputPanel = new ModeInputPanel(this);

        VBox vBox = new VBox(20, messageLabel, modeInputPanel);
        vBox.setAlignment(Pos.CENTER);

        borderPane.setCenter(vBox);
        borderPane.setBottom(null);
    }

    /**
     * Updates the view to show the BoardPanel and game controls
     */
    private void showBoard() {
        boardPanel = new BoardPanel(this, model.getBoard());
        
        undoButton = new Button("Undo move");
        undoButton.setId("UndoButton");   // DO NOT MODIFY ID
        undoButton.setPrefSize(150, 50);
        undoButton.setFont(new Font(12));
        undoButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        undoButton.setOnAction(e -> undo());
        setUndoButton();

        saveButton = new Button("Save board");
        saveButton.setId("SaveButton");  // DO NOT MODIFY ID
        saveButton.setPrefSize(150, 50);
        saveButton.setFont(new Font(12));
        saveButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        saveButton.setOnAction(e -> saveBoard());

        String boardName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".txt";
        saveFileNameTextField = new TextField(boardName);
        saveFileNameTextField.setId("SaveFileNameTextField");  // DO NOT MODIFY ID
        saveFileNameTextField.setStyle("-fx-background-color: #181a1b; -fx-text-fill: white;");

        saveFileErrorLabel = new Label("");
        saveFileErrorLabel.setId("SaveFileErrorLabel");  // DO NOT MODIFY ID
        saveFileErrorLabel.setStyle("-fx-text-fill: #e8e6e3;");

        restartButton = new Button("New game");
        restartButton.setId("RestartButton");  // DO NOT MODIFY ID
        restartButton.setPrefSize(150, 50);
        restartButton.setFont(new Font(12));
        restartButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        restartButton.setOnAction(e -> restart());

        GridPane controls = new GridPane();
        controls.addRow(0, undoButton, restartButton);
        controls.addRow(1, saveFileNameTextField, saveButton);
        controls.add(saveFileErrorLabel, 0, 2, 2, 1);
        controls.setHgap(20);
        controls.setVgap(20);
        controls.setAlignment(Pos.BOTTOM_CENTER);
        GridPane.setHalignment(saveFileErrorLabel, HPos.CENTER);

        VBox vBox = new VBox(20, messageLabel, boardPanel, controls);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
        if (!(model.getCurrentAgent() instanceof HumanAgent)) {
            runMove();
        } else {
            messageLabel.setText(String.format("[%s turn] Select a piece", model.getBoard().getTurn().getType()));
        }
    }

    /**
     * Updates the messageLabel to the given String
     * @param messageLabel String to use for the text of the messageLabel
     */
    protected void setMessageLabel(String messageLabel) {

        this.messageLabel.setText(messageLabel);
    }

    /**
     * Handles running a move for both Human and Computer agents
     * messageLabel must always contain 'MUSKETEER' or 'GUARD'
     * Updates the view after performing the move, the board can be updated by calling BoardPanel.updateCells
     * Checks if the game is over and updates the view accordingly
     * On game over, messageLabel must contain the winner ('MUSKETEER' or 'GUARD') and all Cells must be disabled
     *
     * All Cells and Buttons must be disabled while a computer moves
     *
     */
    protected void runMove() { // TODO
    	this.setUndoButton();
        if(!this.model.getBoard().isGameOver()) {
            if (this.model.getBoard().getTurn() == Piece.Type.MUSKETEER && this.model.getMusketeerAgent() != null) {
            
            try {
                this.model.move(this.model.getMusketeerAgent());
                this.setMessageLabel("MUSKETEER");
                this.boardPanel.updateCells();
            }
            catch (Exception e) {
            	this.setMessageLabel("MUSKETEER");
            	
            }
            	
               
        }
            if (this.model.getBoard().getTurn() == Piece.Type.GUARD && this.model.getGuardAgent() != null) {
            	try {
                this.model.move(model.getGuardAgent());
                this.setMessageLabel("GUARD");
                this.boardPanel.updateCells();
                
            	}
            	catch (Exception e) {
            		this.setMessageLabel("GUARD");
            		
            	}

        }

	        }
	        else {
	            if (this.model.getBoard().getWinner() == Piece.Type.MUSKETEER) {
	                this.setMessageLabel("MUSKETEERS WIN");
	            }
	            else {
	                this.setMessageLabel("GUARDS WIN");
	            }
	        }
	        
	



    }

    /*
     *  Enables or disables the undo button depending on if there are moves to undo
     */
    protected void setUndoButton() { // TODO

    	try {
        if (model.getMovesSize() !=0) {
            undoButton.setDisable(false);
        }
        if (model.getMovesSize() ==0) {
            undoButton.setDisable(true);
        }
    	}
    	catch (Exception e) {
    		
    	}


    }



    /**
     * Sets the GameMode to the given mode
     * Shows the SideSelector (Not needed for Human vs Human) or the BoardPanel accordingly
     * @param mode the selected GameMode
     */
    protected void setGameMode(ThreeMusketeers.GameMode mode) { // TODO
            gameMode = mode;
            if ((this.gameMode.name().equals("HumanGreedy"))|| (this.gameMode.name().equals("HumanRandom"))) {
                this.showSideSelector();
            }
            if (this.gameMode.name().equals("Human")) {
                this.setSide(Piece.Type.GUARD);
                this.setSide(Piece.Type.MUSKETEER);
                this.showBoard();

     
            }

        }

    /**
     * Handles setting the correct agents based on the selected GameMode and the player's piece type by
     * calling model.selectMode
     * Shows the BoardPanel once the side and mode are selected
     * @param sideType the selected Piece Type for the human player in Human vs Computer games
     */
    protected void setSide(Piece.Type sideType) { // TODO
    	this.model.selectMode(gameMode, sideType);
    	this.showBoard();
    	

    }


    /**
     * Handler for the Undo button
     * Undoes the latest move
     */
    private void undo() { // TODO
    	

    	this.model.undoMove();
    	
    	
    	if ( this.model.getCurrentAgent() instanceof RandomAgent || this.model.getCurrentAgent() instanceof GreedyAgent){
            if (this.model.getBoard().getTurn() == Piece.Type.MUSKETEER) {
    		this.model.move(model.getMusketeerAgent());
            
            }
            if (this.model.getBoard().getTurn() == Piece.Type.GUARD) {
            	this.model.move(model.getGuardAgent());
            	
            }
        	runMove();
    	}
    
    	this.boardPanel.updateCells();
    }

    /**
     * Handler for the Save Board button
     * Saves the current board state to a text file
     * Uses saveFileNameTextField to get user input for the name of the file (must end with ".txt")
     * Contains error handling to make sure the file does not already exist and the input ends with ".txt"
     * Updates saveFileErrorLabel with the appropriate message
     *
     * Must use saveFileSuccess, saveFileExistsError, or saveFileNotTxtError to set as the text of saveFileErrorLabel
     */
    private void saveBoard() { // TODO
    	
    	
    	this.saveButton.setOnAction(e -> this.saveBoard());
    	if (this.saveFileNameTextField.getText().endsWith(".txt")){
    		File file = new File("boards", this.saveFileNameTextField.getText());
    		try {
				if (file.createNewFile() == false) {
					this.saveFileErrorLabel.setText(saveFileExistsError.toString());
				}
				
				else {
					this.saveFileErrorLabel.setText(saveFileSuccess.toString());
					this.model.getBoard().saveBoard(file);
					
		    		
				}
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}

    	}
    	else {
    		this.saveFileErrorLabel.setText(saveFileNotTxtError.toString());
    	}
    	
    	 
    }
    
    	
    
    private void restart() { // TODO
    	
    	
    	this.initUI();
        stage.setTitle("Three Musketeers");
        stage.setMinHeight(900);
        stage.setMinWidth(600);
        stage.getIcons().add(new Image("file:images/musketeer.png"));

        this.model = new ThreeMusketeers();
       this.stage = stage;
    	
    	
    }

    /**
     * Updates the view to show the side selector
     */
    private void showSideSelector() {
        VBox vBox = new VBox(20, messageLabel, new SideInputPanel(this));
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);
    }


}

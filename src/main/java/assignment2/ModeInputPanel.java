package assignment2;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import  java.io.FileFilter;
import   javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
import java.io.FilenameFilter;
 


public class ModeInputPanel extends GridPane {
    private final View view;

    /**
     * Constructs a new GridPane with the main menu of the game, to select a game mode and load saved boards
     * @param view
     */
    public ModeInputPanel(View view) {
        this.view = view;
        this.setAlignment(Pos.CENTER);
        this.setVgap(10);

        view.setMessageLabel("Main Menu");

        createModeButtons();
        createListView();
    }

    /**
     * Creates the Game mode buttons
     */
    private void createModeButtons(){
        for (ThreeMusketeers.GameMode mode: ThreeMusketeers.GameMode.values()) {
            Button button = new Button(mode.getGameModeLabel());
            button.setId(mode.getGameModeLabel().replaceAll(" ", "")); // DO NOT MODIFY ID

            // Default styles which can be modified
            button.setPrefSize(500, 100);
            button.setFont(new Font(20));
            button.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            button.setOnAction(e -> this.view.setGameMode(mode));

            this.add(button, 0, this.getRowCount());
        }
    }

    /**
     * Creates the ListView to select a board to load
     */
    private void createListView(){
        Label selectBoardLabel = new Label(String.format("Current board: %s", view.model.getBoardFile().getName()));
        selectBoardLabel.setId("CurrentBoard"); // DO NOT MODIFY ID

        ListView<String> boardsList = new ListView<>();
        boardsList.setId("BoardsList");  // DO NOT MODIFY ID

        boardsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        int starterIndex = getFiles(boardsList);
        boardsList.getSelectionModel().select(starterIndex);

        Button selectBoardButton = new Button("Change board");
        selectBoardButton.setId("ChangeBoard"); // DO NOT MODIFY ID

        selectBoardButton.setOnAction(e -> selectBoard(selectBoardLabel, boardsList));

        VBox selectBoardBox = new VBox(10, selectBoardLabel, boardsList, selectBoardButton);

        // Default styles which can be modified

        boardsList.setPrefHeight(100);

        selectBoardLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectBoardLabel.setFont(new Font(16));

        selectBoardButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectBoardButton.setPrefSize(200, 50);
        selectBoardButton.setFont(new Font(16));

        selectBoardBox.setAlignment(Pos.CENTER);

        this.add(selectBoardBox, 0, this.getRowCount());
    }

    /**
     * Gets all the text files from the boards directory and puts them into the given listView
     * @param listView ListView to update
     * @return the index in the listView of Starter.txt
     */
    private int getFiles(ListView<String> listView) { // TODO
  
    	
    	int i = 0;
    	int starterFile = 0;

    	        // Creates an array in which we will store the names of files and directories
    	        String[] pathnames;

    	        // Creates a new File instance by converting the given pathname string
    	        // into an abstract pathname
    	        File f = new File("boards");

    	        // Populates the array with names of files and directories
    	        pathnames = f.list();

    	        // For each pathname in the pathnames array
    	        for (String pathname : pathnames) {
    	        	i = i + 1;
    	        	listView.getItems().add(pathname);
    	            // Print the names of files and directories
    	            if (pathname == "Starter.txt") {
    	            	starterFile = i;
    	            	
    	            }
    	        }
    return starterFile;	    
    }
    /**
     * Loads the board file selected in the boardsList and updates the selectBoardLabel with the name of the new Board file
     * @param selectBoardLabel a message Label to update which board is currently selected
     * @param boardsList a ListView populated with boards to load
     */
    private void selectBoard(Label selectBoardLabel, ListView<String> boardsList) { // TODO
    	
    	String item = boardsList.getSelectionModel().getSelectedItem();
        // Creates an array in which we will store the names of files and directories
    	File f = new File("boards");
    	File[] hi = f.listFiles();
    	for (File x : hi) {
    		System.out.println(x.getName().toString());
    		System.out.println(item);
    		System.out.println(x.getName().toString().equals(item));
    		if (x.getName().toString().equals(item)) {
    			
    			this.view.model.setBoard(x);
    		}
    	}

    	
    	
    	selectBoardLabel.setText(item);
    }
}

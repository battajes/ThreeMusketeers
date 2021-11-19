package assignment2;

import java.util.List;

import javax.xml.xpath.XPathEvaluationResult.XPathResultType;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class BoardPanel extends GridPane implements EventHandler<ActionEvent> {

    private final View view;
    private final Board board;
    
    
    private Boolean clicked;
    private Cell movefrom;
    /**
     * Constructs a new GridPane that contains a Cell for each position in the board
     *
     * Contains default alignment and styles which can be modified
     * @param view
     * @param board
     */
    public BoardPanel(View view, Board board) {
        this.view = view;
        this.board = board;
       
        this.clicked = false;
        
        // Can modify styling
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #181a1b;");
        int size = 550;
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);

        
        setupBoard();
        updateCells();
    }


    /**
     * Setup the BoardPanel with Cells
     */
    private void setupBoard(){ // TODO
    
        
        
    	List<Cell> musketters= this.board.getMusketeerCells();
    	List<Cell> guards = this.board.getGuardCells();

    	for (Cell x: musketters) {
    		int corfrom = x.getCoordinate().col;
    		int corto = x.getCoordinate().row;
    		this.add(x, corfrom, corto);
    		x.setOnAction(e -> this.handle(e));


    }
    	for (Cell y: guards) {
    		int corfrom = y.getCoordinate().col;
    		int corto = y.getCoordinate().row;
    		this.add(y, corfrom, corto);
    		y.setOnAction(e -> this.handle(e));

    	}
    }
    	

    /**
     * Updates the BoardPanel to represent the board with the latest information
     *
     * If it's a computer move: disable all cells and disable all game controls in view
     *
     * If it's a human player turn and they are picking a piece to move:
     *      - disable all cells
     *      - enable cells containing valid pieces that the player can move
     * If it's a human player turn and they have picked a piece to move:
     *      - disable all cells
     *      - enable cells containing other valid pieces the player can move
     *      - enable cells containing the possible destinations for the currently selected piece
     *
     * If the game is over:
     *      - update view.messageLabel with the winner ('MUSKETEER' or 'GUARD')
     *      - disable all cells
     */
    protected void updateCells(){ // TODO
    	
    	
    	for (Cell x: this.view.model.getBoard().getAllCells()) {
    		
    		x.setDisable(true);
    		try {
    		 if (((this.view.model.getCurrentAgent() instanceof HumanAgent)) && (!this.view.model.getBoard().getPossibleDestinations(x).isEmpty() )) {
    			 //System.out.println(this.board.getTurn());
    			 if (this.rightTurn(x)) {
    				 x.setDisable(false);
    			 }
    		
    		 }
    		}
    		catch(Exception e) {
	
    		}
    		 
    		 if((this.view.model.getCurrentAgent() instanceof HumanAgent) && (this.clicked && possibledest(this.movefrom, x))) {
    			 this.movefrom.setDisable(true);
    			 x.setDisable(false);
    		 }

	}

	if (this.view.model.getBoard().isGameOver() == true) {
		for (Cell x: this.view.model.getBoard().getAllCells()) {
			x.setDisable(true);
			
		
	}

		
	
}
	this.view.runMove();
    }


    /**
     * Handles Cell clicks and updates the board accordingly
     * When a Cell gets clicked the following must be handled:
     *  - If it's a valid piece that the player can move, select the piece and update the board
     *  - If it's a destination for a selected piece to move, perform the move and update the board
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) { // TODO

    	
    	if (!this.clicked &&  this.isValidCell((Cell)actionEvent.getSource())) {
    		this.clicked = true;
    		this.movefrom = (Cell) actionEvent.getSource();
    		this.updateCells();
    		
	}
	if (this.clicked && this.possibledest(this.movefrom, (Cell)actionEvent.getSource())) {
			view.model.move(new Move( this.movefrom, (Cell) actionEvent.getSource()));
			this.clicked = false;
			

			updateCells();
		}
    
    }
    
    public boolean isValidCell(Cell Piece) {

    	if (this.view.model.getBoard().getPossibleCells().contains(Piece)) {
    		return true;
    	}
    	return false;
    	
    }
    
    
    public boolean possibledest(Cell Piece, Cell contains) {
    	if (this.view.model.getBoard().getPossibleDestinations(Piece).contains(contains)) {
    		return true;
    	}
    	return false;
    }
    
    public boolean rightTurn(Cell cell) {
    	
    	
    	if (this.board.getTurn().toString().contains("MUSKETEER")) {
    		if (cell.toString().contains("X")) {
    			return true;
    		}
    	}
    	if (this.board.getTurn().toString().contains("GUARD")) {
    		if (cell.toString().contains("O")) {
    			return true;
    		}
    	}
    return false;
    }
}


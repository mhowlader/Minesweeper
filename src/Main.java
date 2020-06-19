/* Mohtasim Howlader and Jeremiah Son
 * 112937689
 * CSE 160
 * Final Project
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;

public class Main extends Application{
	
	int height = 10; //num squares
	int width = 10;
	int numBombs = 15;
	int size = 50; //width of a squar
	int remSquares = height*width - numBombs;
	
	private Square[][] board = new Square[height][width];
	private Rectangle[][] boardrec = new Rectangle[height][width];
	/**
	 *
	 */
	/**
	 *
	 */
	/**
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
//		

		
		
		Pane p = new Pane();
		//GridPain grid = new GridPain();
		int remBombs = numBombs;
		
		//creates grid
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Rectangle rect = new Rectangle(size*i,size*j,size,size);
				rect.setFill(Color.WHITE);
				rect.setStroke(Color.BLACK);
				boardrec[i][j]=rect;
				boolean placeBomb = false;
				if (remBombs > 0) {
					//there are bombs left
					if (Math.random()<(numBombs/(height*width*1.0))) { 
						placeBomb = true;
						remBombs--;
					}	
				}
				Square box = new Square(i,j, placeBomb);
				board[i][j]=box;
				Text name = new Text(" ");

//				if (board[i][j].isBomb()) {
//					boardrec[i][j].setFill(Color.BLACK);
//				} //set bombs to black

				//p.getChildren().add(box);
				p.getChildren().add(rect);

			}
		}
		
		//ensure all Bombs are placed
		while (remBombs>0) {
			int x = (int) Math.random()*height;
			int y = (int) Math.random() *width;
			board[x][y].setBomb();
			//boardrec[x][y].setFill(Color.BLACK);
			remBombs--;
		}
		
		//System.out.println(remBombs);
		
		//mouse click on grid
		p.setOnMouseClicked(e -> {
			double mX= e.getX();
			double mY= e.getY();
			
			int colX = (int)(mX/size); //array indices for board and board rec
			int colY = (int)(mY/size); 
			
			Square sel = board[colX][colY];
			Rectangle selRect = boardrec[colX][colY];
			if (!sel.getClicked()) {
				
			
				if (sel.isBomb()) {
					if (e.getButton() == MouseButton.PRIMARY) {
						for (int k = 0; k < height; k++) {
							for (int l = 0; l < width; l++) {
								if (board[k][l].isBomb()) {
									boardrec[k][l].setFill(Color.BLACK);
								}
							}
						}
						gameOver();
					}
					else {
						selRect.setFill(Color.GREEN);
					}
					//primaryStage.close();
				}
				else {
					sel.wasClicked();
					remSquares--;
					//System.out.println(remSquares);
					if (remSquares == 0) {
						victory();
						primaryStage.close();
					}
					ArrayList<Square> neighbors = getNeighbors(board[colX][colY]);
	//				for (Square o:n) {
	//					System.out.println(o.xcor + " " + o.ycor);
					int countBombs = 0;
					for (Square sq:neighbors) {
						if (sq.isBomb()) {
						countBombs++;
						}
					}
					if (countBombs>0) {
						Text numBombs = new Text("" + countBombs);
						numBombs.setFont(Font.font ("Verdana", 30));
						numBombs.setX(selRect.getX()+ (size/2.0) - 7);
						numBombs.setY(selRect.getY()+ (size/2.0) + 7);
						p.getChildren().add(numBombs);
					}
					else {
						Text numBombs = new Text("" + countBombs);
						numBombs.setFont(Font.font ("Verdana", 30));
						numBombs.setX(selRect.getX()+ (size/2.0) - 7);
						numBombs.setY(selRect.getY()+ (size/2.0) + 7);
						p.getChildren().add(numBombs);
						for (Square sq:neighbors) {
							if (!sq.getClicked()) {
								sq.wasClicked();
								remSquares--;
								if (remSquares == 0) {
									victory();
									primaryStage.close();
								}
								Rectangle temp = boardrec[sq.xcor][sq.ycor]; 
								int neiBombs = numNeighborsBomb(sq);
								Text numBombs2 = new Text("" + neiBombs);
								numBombs2.setFont(Font.font ("Verdana", 30));
								numBombs2.setX(temp.getX()+ (size/2.0) - 7);
								numBombs2.setY(temp.getY()+ (size/2.0) + 7);
								p.getChildren().add(numBombs2);
							}
						}
					}
				}
			
			}
			
		});
		
		
		Scene s = new Scene(p);

		primaryStage.setScene(s);
		primaryStage.show();
		
	}
	
//	public void main2() throws Exception {
//		
//	}
	
	
	public int numNeighborsBomb(Square sq) {
		ArrayList<Square> neighbors = getNeighbors(sq);
//		for (Square o:n) {
//			System.out.println(o.xcor + " " + o.ycor);
		int countBombs = 0;
		for (Square s:neighbors) {
			if (s.isBomb()) {
			countBombs++;
			}
		}
		return countBombs;
	}
	
	public void victory() {
		Stage newStage = new Stage();
		Pane comp = new Pane();
		Text over = new Text("Victory!!");
		over.setX(100); 
	    over.setY(150); 
		over.setFont(Font.font ("Verdana", 40));
		comp.getChildren().add(over);
		Scene stageScene = new Scene(comp, 300, 300);
		newStage.setScene(stageScene);
		newStage.show();
			
	}
	public void gameOver() {
		Stage newStage = new Stage();
		Pane comp = new Pane();
		Text over = new Text("Game Over");
		over.setX(40); 
	    over.setY(100); 
		over.setFont(Font.font ("Verdana", 40));
		comp.getChildren().add(over);
		Scene stageScene = new Scene(comp, 300, 300);
		newStage.setScene(stageScene);
		newStage.show();



		
	}
	
	public ArrayList<Square> getNeighbors(Square sq) {
		ArrayList<Square> nei = new ArrayList<Square>();
		int curX = sq.xcor;
		int curY = sq.ycor;
		
		int newX = curX - 1;
		int newY = curY -1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX;
		newY = curY -1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX + 1;
		newY = curY -1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX + 1;
		newY = curY ;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX + 1;
		newY = curY + 1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX;
		newY = curY +1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX - 1;
		newY = curY +1;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		
		newX = curX - 1;
		newY = curY ;
		
		if (newX >= 0 && newX < width && newY >=0 && newY< height) {
			nei.add(board[newX][newY]);
		}
		return nei;
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	private class Square {
		private boolean BOMB;
		public int xcor, ycor; //array indices
		ArrayList<Square> neighbors;
		private boolean clicked = false;;
		
		public Square(int xcor, int ycor, boolean BOMB) {
			this.xcor=xcor;
			this.ycor=ycor;
			this.BOMB = BOMB;
			
			Rectangle shape = new Rectangle(40,40);
	
			shape.setStroke(Color.BLACK);
			shape.setFill(Color.WHITE);
			//getChildren().add(shape);
			
		}
		
		public boolean isBomb() {
			return BOMB;
		}
		public void setBomb() {
			BOMB=true;
		}
		public boolean getClicked() {
			return clicked;
		}
		public void wasClicked() {
			clicked = true;
		}
		
		
	}
}


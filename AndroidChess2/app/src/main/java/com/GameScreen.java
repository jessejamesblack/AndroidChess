package com;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.model.ChessPiece;
import com.model.Game;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

// ##### System.out ----> Log.e("GameScreen","Insert Message");

public class GameScreen extends AppCompatActivity {

    private boolean gameOver = false;
    private boolean draw = false;
    private boolean stalemate = false;
    private boolean whiteWon = false;
    private boolean itsWhitesMove = true;
    private String previousMove = null, currentMove = null;
    private int initialFile, initialRank, finalFile, finalRank;
    public TextView whoseMoveIsIt;
    private Game game = null;
    private boolean madeMove = false;
    private View[] position;
    private boolean castle = false;
    private TextView status;
    private GridLayout gridLayout;
    private String filename = "savegames";
    private FileOutputStream outputStream;
    private String[] moves;
    private String gameName = "Game";
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        whoseMoveIsIt = (TextView) findViewById(R.id.whoseMoveIsIt);
        whoseMoveIsIt.setText("White to move");
        gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        game = new Game(this);
    }

    public void registerMove(View view) {
        // white's turn
        if (itsWhitesMove) {
            // this is the first click of the move, it must be a white piece. Can't be empty
            if (previousMove == null && view.getContentDescription() != null &&
                    view.getContentDescription().toString().charAt(0) == 'w') {
                // don't attempt to execute the move
                // extract the position of the first click and set previous move to it
                previousMove = view.getTag().toString();
            }
            // this is the second click so check if this is a valid move. This square can be empty or have a black piece
            // extract the move feed it to the Game class for processing
            else if (previousMove != null && (view.getContentDescription() == null || view.getContentDescription().toString().charAt(0) == 'b')) {
                currentMove = view.getTag().toString();
                initialFile = previousMove.charAt(0) - 48;
                initialRank = previousMove.charAt(1) - 48;
                finalFile = currentMove.charAt(0) - 48;
                finalRank = currentMove.charAt(1) - 48;
                boolean isAValidMove = game.isThisAValidMove(initialFile, initialRank, finalFile, finalRank);
                // execute the move
                if (isAValidMove) {
                    // execute the move by feeding it into the Game class
                    game.ProcessMove(initialFile, initialRank, finalFile, finalRank);
                    // update the chess board
                    try {
                        updateChessBoard(itsWhitesMove, initialFile, initialRank, finalFile, finalRank);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }                    // now it's blacks turn
                    // update the textView
                    whoseMoveIsIt.setText("Black to move");
                    previousMove = currentMove;
                } else {
                    previousMove = null;
                }
            }
        }
        // black's turn
        else {
            // this is the first click of the move, it must be a white piece. Can't be empty
            if (previousMove == null && view.getContentDescription() != null &&
                    view.getContentDescription().toString().charAt(0) == 'b') {
                // don't attempt to execute the move
                // extract the position of the first click and set previous move to it
                previousMove = view.getTag().toString();
            }
            // this is the second click so check if this is a valid move. This square can be empty or have a black piece
            // extract the move feed it to the Game class for processing
            else if (previousMove != null && (view.getContentDescription() == null || view.getContentDescription().toString().charAt(0) == 'w')) {
                currentMove = view.getTag().toString();
                initialFile = previousMove.charAt(0) - 48;
                initialRank = previousMove.charAt(1) - 48;
                finalFile = currentMove.charAt(0) - 48;
                finalRank = currentMove.charAt(1) - 48;
                boolean isAValidMove = game.isThisAValidMove(initialFile, initialRank, finalFile, finalRank);
                // execute the move
                if (isAValidMove) {
                    // execute the move by feeding it into the Game class
                    game.ProcessMove(initialFile, initialRank, finalFile, finalRank);
                    // update the chess board
                    try {
                        updateChessBoard(itsWhitesMove, initialFile, initialRank, finalFile, finalRank);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // update the textView
                    whoseMoveIsIt.setText("White to move");
                    previousMove = currentMove;
                } else {
                    previousMove = null;
                }
            }
        }
    }

    public void updateChessBoard() {
        // clear pieces off all squares
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView image = (ImageView) gridLayout.getChildAt(i);
            image.setImageResource(0);
            image.setContentDescription(null);
        }
        // add pieces back onto board
        ArrayList<ChessPiece> chessPieceArrayList = game.getChessPieceArray();
        int file, rank;
        String contentDescription, tag;
        ImageView imageView;
        for (int i = 0; i < chessPieceArrayList.size(); i++) {
            file = chessPieceArrayList.get(i).getFile();
            rank = chessPieceArrayList.get(i).getRank();
            contentDescription = chessPieceArrayList.get(i).getName();
            tag = Integer.toString(file) + Integer.toString(rank);

            imageView = returnSquareWithTheFollowingTag(tag);
            imageView.setContentDescription(contentDescription);
            int drawable = returnDrawableForTheFollowingChessPiece(contentDescription);
            if (drawable != -1) {
                imageView.setImageResource(drawable);
            }
        }
    }

    public void updateChessBoard(boolean itsWhitesMove, int initialFile, int initialRank, int finalFile, int finalRank) throws FileNotFoundException {
        previousMove = null;
        currentMove = null;
        String color = "white";

        if (MainActivity.record == true) {
            if (itsWhitesMove == true) {
                color = "White";
            } else {
                color = "Black";
            }
            String string = color + ", initial position: " + initialFile + " " + initialRank + ", final position: " + finalFile + " " + finalRank + "\n\n";
            try {
                outputStream = openFileOutput(filename, MODE_APPEND);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // clear pieces off all squares
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView image = (ImageView) gridLayout.getChildAt(i);
            image.setImageResource(0);
            image.setContentDescription(null);
        }
        // add pieces back onto board
        ArrayList<ChessPiece> chessPieceArrayList = game.getChessPieceArray();
        int file, rank;
        String contentDescription, tag;
        ImageView imageView;
        for (int i = 0; i < chessPieceArrayList.size(); i++) {
            file = chessPieceArrayList.get(i).getFile();
            rank = chessPieceArrayList.get(i).getRank();
            contentDescription = chessPieceArrayList.get(i).getName();
            tag = Integer.toString(file) + Integer.toString(rank);

            imageView = returnSquareWithTheFollowingTag(tag);
            imageView.setContentDescription(contentDescription);
            int drawable = returnDrawableForTheFollowingChessPiece(contentDescription);
            if (drawable != -1) {
                imageView.setImageResource(drawable);
            }
        }
    }

    public void undoPreviousMove(View view) {
        //unfinished
        if (!gameOver) {
            if (madeMove == true) {

                int firstID = position[0].getId();
                int secondID = position[1].getId();

                Button originalLocation = (Button) findViewById(firstID);
                Button currentLocation = (Button) findViewById(secondID);

                originalLocation.setText(currentLocation.getText());
                currentLocation.setText("");


                madeMove = false;
                if (itsWhitesMove == true) {
                    status.setText("Turn: Player 1       Piece Selected: ");
                } else {
                    status.setText("Turn: Player 2       Piece Selected: ");
                }
                return;
            } else {
                if (itsWhitesMove == true) {
                    Toast.makeText(GameScreen.this, "No move made?", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    public void offerDraw(View view) {
        if (!gameOver) {
            AlertDialog.Builder drawAlert = new AlertDialog.Builder(this);
            drawAlert.setNegativeButton("Decline",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            drawAlert.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            draw = true;
                            gameOver = true;
                            gameOver();
                        }
                    });

            drawAlert.setCancelable(true);

            if (itsWhitesMove == true) {
                drawAlert.setMessage("Player 1 has offered a draw.");
                drawAlert.setTitle("Player 2");
            } else {
                drawAlert.setMessage("Player 2 has offered a draw.");
                drawAlert.setTitle("Player 1");
            }
            drawAlert.create().show();

        }
    }

    public void resign(View view) {
        if (!gameOver) {
            AlertDialog.Builder resignAlert = new AlertDialog.Builder(this);

            if (itsWhitesMove == true) {
                resignAlert.setTitle("Player 1");
            } else {
                resignAlert.setTitle("Player 2");
            }
            resignAlert.setNegativeButton("No", null);
            resignAlert.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (itsWhitesMove == true) {
                                whiteWon = false;
                                gameOver = true;
                                gameOver();
                            } else {
                                whiteWon = true;
                                gameOver = true;
                                gameOver();
                            }
                        }
                    });
            resignAlert.setMessage("Are you sure that you want to resign?");
            resignAlert.create().show();

        }
        //gameOver();
    }

    public void gameOver() {

        // check final state of the game
        if(MainActivity.record == false){
            Intent endGameIntent = new Intent(GameScreen.this, MainActivity.class);
            startActivity(endGameIntent);
        }
        if (MainActivity.record ==  true) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if (stalemate == true) {
                builder.setTitle("Stalemate save game?");
            } else if (draw == true) {
                builder.setTitle("Draw save game?");
            } else if (whiteWon == true) {
                builder.setTitle("White has won save game?");
            } else {
                builder.setTitle("Black won save game?");
            }
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent newGameIntent = new Intent(GameScreen.this, MainActivity.class);
                    gameName = input.getText().toString();
                    Date date = new Date();
                    String gameNumberString = date + ": " + gameName + "\n";
                    try {
                        outputStream = openFileOutput(filename, MODE_APPEND);
                        outputStream.write(gameNumberString.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(gameNumberString.length() > 0) {
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                        Intent endGameIntent = new Intent(GameScreen.this, MainActivity.class);
                        startActivity(endGameIntent);
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();

        }

    }

    //*****************************************************Utility methods************************************************//

    public ImageView returnSquareWithTheFollowingTag(String tag) {
        if (tag == null) {
            return null;
        }
        ImageView imageView;
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            imageView = (ImageView) gridLayout.getChildAt(i);
            if (imageView.getTag().toString().compareTo(tag) == 0) {
                return imageView;
            }
        }
        return null;
    }

    //********************************************************************************************************************//
    private int returnDrawableForTheFollowingChessPiece(String contentDescription) {
        if (contentDescription.compareTo("wp") == 1) {
            return R.drawable.white_pawn;
        } else if (contentDescription.compareTo("wN") == 1) {
            return R.drawable.white_knight;
        } else if (contentDescription.compareTo("wB") == 1) {
            return R.drawable.white_bishop;
        } else if (contentDescription.compareTo("wR") == 1) {
            return R.drawable.white_rook;
        } else if (contentDescription.compareTo("wQ") == 1) {
            return R.drawable.white_queen;
        } else if (contentDescription.compareTo("wK") == 1) {
            return R.drawable.white_king;
        } else if (contentDescription.compareTo("bp") == 1) {
            return R.drawable.black_pawn;
        } else if (contentDescription.compareTo("bN") == 1) {
            return R.drawable.black_knight;
        } else if (contentDescription.compareTo("bB") == 1) {
            return R.drawable.black_bishop;
        } else if (contentDescription.compareTo("bR") == 1) {
            return R.drawable.black_rook;
        } else if (contentDescription.compareTo("bQ") == 1) {
            return R.drawable.black_queen;
        } else if (contentDescription.compareTo("bK") == 1) {
            return R.drawable.black_king;
        } else {
            return -1;
        }
    }

    //********************************************************************************************************************//
    public char returnColorOfPieceOnTheFollowingSquare(int file, int rank) {
        String tag = Integer.toString(file) + Integer.toString(rank);
        ImageView imageView = returnSquareWithTheFollowingTag(tag);
        if (imageView.getContentDescription() == null) {
            //Log.e("GameScreen", "e");
            return 'e'; // e for empty space
        }
        String name = imageView.getContentDescription().toString();
        //Log.e("GameScreen", Character.toString(name.charAt(0)));
        return name.charAt(0); // return either w or b
    }

    //********************************************************************************************************************//
    public boolean getItsWhitesMove() {
        return this.itsWhitesMove;
    }

    //********************************************************************************************************************//
    public void autoMove(View view){
        //Log.e("GameScreen.autoMove", "$");
        game.autoMove();
    }

    //********************************************************************************************************************//
    public void setItsWhitesMove(boolean value){
        this.itsWhitesMove = value;
    }

    //********************************************************************************************************************//
    public void endGame(char whoseTurnWasIsIt, boolean whiteWasInCheck, boolean blackWasInCheck){
        this.gameOver = true;

        //AlertDialog.Builder drawAlert = new AlertDialog.Builder(this);
        //drawAlert.setCancelable(true);
        //drawAlert.setTitle("Game Over");

        if(whoseTurnWasIsIt == 'w' && whiteWasInCheck == true){
            this.whiteWon = false;
            //drawAlert.setMessage("White wins");
            Toast.makeText(GameScreen.this, "BLACK WINS!", Toast.LENGTH_LONG).show();

        }
        else if(whoseTurnWasIsIt == 'b' && blackWasInCheck == true){
            this.whiteWon = true;
            //drawAlert.setMessage("Black wins");
            Toast.makeText(GameScreen.this, "WHITE WINS!", Toast.LENGTH_LONG).show();
        }
        else if(whiteWasInCheck == false && blackWasInCheck == false){
            this.stalemate = true;
            //drawAlert.setMessage("Stalemate");
            Toast.makeText(GameScreen.this, "STALEMATE!", Toast.LENGTH_LONG).show();
        }
        //drawAlert.create().show();
        gameOver();
    }
    //********************************************************************************************************************//

}

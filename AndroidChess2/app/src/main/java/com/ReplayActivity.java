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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/***     color piece initial final
 currently reads as White, initial position: 0 1, final position: 0 3
 followed by a game label
 should probably read moves till the label that is selected by the user. IE. user clicks Game 1
 read moves up to that point but not including previous games

 1) read all moves from file
 2) load chessboard as default
 ***/

public class ReplayActivity extends AppCompatActivity {
    public TextView whoseMoveIsIt;
    Button previousButton, nextButton;
    ArrayList<String> moves;
    String savedGame;
    private boolean draw = false;
    private boolean stalemate = false;
    private boolean gameOver = false;
    private boolean whiteWon = false;
    private boolean itsWhitesMove = true;
    private String previousMove = null, currentMove = null;
    private Game game = null;
    private GridLayout gridLayout;
    private String filename;

    public static void fixList(String selectedFromList) {
        System.out.println(selectedFromList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        //reads the file
        whoseMoveIsIt = (TextView) findViewById(R.id.whoseMoveIsIt);
        whoseMoveIsIt.setText("White to move");
        gridLayout = (GridLayout) findViewById(R.id.GridLayout);
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();
        try {
            FileInputStream inputStream = this.openFileInput("savegames");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder finalString = new StringBuilder();
            String line;
            moves = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null) {
                finalString.append(line);
                moves.add(line);
                //System.out.println(moves);
            }

            bufferedReader.close();
            inputStream.close();
            inputStreamReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(moves);
        //ends reading all moves should be in array now.
    }

    public void updateChessBoard(boolean itsWhitesMove, int initialFile, int initialRank, int finalFile, int finalRank) throws FileNotFoundException {
        previousMove = null;
        currentMove = null;

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

    public void nextMove(View view) {
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iterate to next move color piece initial final
            }
        });

    }

    public void previousMove(View view) {
        previousButton = (Button) findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iterate to previous move color piece initial final
            }
        });
    }

    public void gameOver() {

        // check final state of the game
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (stalemate == true) {
            builder.setTitle("Stalemate");
        } else if (draw == true) {
            builder.setTitle("Draw ");
        } else if (whiteWon == true) {
            builder.setTitle("White has won");
        } else {
            builder.setTitle("Black won");
        }
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent endGameIntent = new Intent(ReplayActivity.this, MainActivity.class);
                startActivity(endGameIntent);
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
    public void setItsWhitesMove(boolean value) {
        this.itsWhitesMove = value;
    }

    //********************************************************************************************************************//
    public void endGame(char whoseTurnWasIsIt, boolean whiteWasInCheck, boolean blackWasInCheck) {
        this.gameOver = true;
        if (whoseTurnWasIsIt == 'w' && whiteWasInCheck == true) {
            this.whiteWon = false;
            Toast.makeText(ReplayActivity.this, "BLACK WINS!", Toast.LENGTH_LONG).show();
        } else if (whoseTurnWasIsIt == 'b' && blackWasInCheck == true) {
            this.whiteWon = true;
            Toast.makeText(ReplayActivity.this, "WHITE WINS!", Toast.LENGTH_LONG).show();
        } else if (whiteWasInCheck == false && blackWasInCheck) {
            this.stalemate = true;
            Toast.makeText(ReplayActivity.this, "STALEMATE!", Toast.LENGTH_LONG).show();
        }
        gameOver();
    }
    //********************************************************************************************************************//

}
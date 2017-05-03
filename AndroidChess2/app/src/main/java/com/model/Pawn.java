package com.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;

import com.GameScreen;
import com.R;

import java.util.Scanner;

public class Pawn extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
    private boolean moved2SpacesLastTurn;
    public static int choiceForPawnPromotion;
    public static Pawn pawnToPromote;
    //*************************************************METHODS****************************************************//
    public Pawn(String color, int file, int rank, Game game, GameScreen gameScreen) {
        super(color, file, rank, game, gameScreen);
        this.setName();
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wp ");
        } else {
            super.setName("bp ");
        }
    }
    //************************************************************************************************************//
    public String getName() {
        return super.getName();
    }
    //************************************************************************************************************//
    public boolean canMove(int oldX, int oldY, int newX, int newY) {
        int deltaX = newX - oldX;
        int deltaY = newY - oldY;
        int currentFile = this.getFile(); // column in board[][]
        int currentRank = this.getRank(); // row in board[][]
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

        //Log.e("Pawn", Boolean.toString(this.hasthisMoved()));
        this.checkForPromotion(newY);
        // white
        if (this.isWhite() == true) {
            // move up 1 space
            if (deltaY == 1 && deltaX == 0 && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 1) == 'e') {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Aw");
                return true;
            }
            // move up 2 spaces
            else if (this.hasthisMoved() == false && deltaY == 2 && deltaX == 0
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 1) == 'e'
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 2) == 'e') {
                this.moved2SpacesLastTurn = true;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Bw");
                return true;
            }
            //capture enemy piece (diagonally to the right)
            else if ((deltaY == 1) && (deltaX == 1) && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank + 1) == 'b')) {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Cw");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == 1) && (deltaX == -1) && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank + 1) == 'b')) {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Dw");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == 1)////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'b') ) {
                Log.e("Pawn", "Ew");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    Log.e("Pawn", "E2w");
                    game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    this.thisPieceHasMoved();
                    this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == -1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'b') ) {
                Log.e("Pawn", "Fw");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    Log.e("Pawn", "F2w");
                    game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    this.thisPieceHasMoved();
                    this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
        }
        // black
        else {
            // move down one space
            if (deltaY == -1 && deltaX == 0 && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 1) != 'w') {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Ab");
                return true;
            }
            // move up 2 spaces
            else if (this.hasthisMoved() == false && deltaY == -2 && deltaX == 0 && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 2) == 'e') {
                // pawn attempting to move through another piece
                if (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 1) != 'e') {
                    return false;
                }
                // path is clear
                this.moved2SpacesLastTurn = true;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Bb");
                return true;
            }
            //capture enemy piece (diagonally to the right)
            else if ((deltaY == -1) && (deltaX == 1) && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank - 1) == 'w')) {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Cb");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == -1) && (deltaX == -1) && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank - 1) == 'w')) {
                this.moved2SpacesLastTurn = false;
                this.thisPieceHasMoved();
                Log.e("Pawn", "Db");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == -1) ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'w') ) {
                Log.e("Pawn", "Eb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    Log.e("Pawn", "E2b");
                    game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    this.thisPieceHasMoved();
                    this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == 1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'w') ) {
                Log.e("Pawn", "Fb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    Log.e("Pawn", "F2b");
                    game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    this.thisPieceHasMoved();
                    this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
        }
        // invalid
        return false;
    }
    //************************************************************************************************************//
    public boolean canMoveWithoutEditingHasMoved(int oldX, int oldY, int newX, int newY) {
        int deltaX = newX - oldX;
        int deltaY = newY - oldY;
        int currentFile = this.getFile(); // column in board[][]
        int currentRank = this.getRank(); // row in board[][]
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

        //Log.e("Pawn", Boolean.toString(this.hasthisMoved()));
        // white
        if (this.isWhite() == true) {
            // move up 1 space
            if (deltaY == 1 && deltaX == 0 && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 1) == 'e') {
                //Log.e("Pawn", "Aw");
                return true;
            }
            // move up 2 spaces
            else if (this.hasthisMoved() == false && deltaY == 2 && deltaX == 0
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 1) == 'e'
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank + 2) == 'e') {
                //Log.e("Pawn", "Bw");
                return true;
            }
            //capture enemy piece (diagonally to the right)
            if ((deltaY == 1) && (deltaX == 1)  && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile+1,currentRank + 1) == 'b') {
                //Log.e("Pawn", "Cw");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == 1) && (deltaX == -1)  && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile-1,currentRank + 1) == 'b') {
                //Log.e("Pawn", "Dw");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == 1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'b') ) {
                //Log.e("Pawn", "Ew");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    //Log.e("Pawn", "E2w");
                    game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == -1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'b') ) {
                //Log.e("Pawn", "Fw");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    //Log.e("Pawn", "F2w");
                    game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    return true;
                }
            }
        }
        // black
        else {
            // move down one space
            if (deltaY == -1 && deltaX == 0 && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 1) != 'w') {
                //Log.e("Pawn", "Ab");
                //Log.e("Pawn", this.getName() + ", " + Integer.toString(this.getFile()) + " " + Integer.toString(this.getRank()));
                return true;
            }
            // move up 2 spaces
            else if (this.hasthisMoved() == false && deltaY == -2 && deltaX == 0
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 1) == 'e'
                    && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,currentRank - 2) == 'e') {
                //Log.e("Pawn", "Bw");
                return true;
            }
            //capture enemy piece (diagonally to the right)
            if ((deltaY == -1) && (deltaX == 1)  && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile+1,currentRank - 1) == 'w') {
                // checkForPromotion(newY);
                //Log.e("Pawn", "Cb");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == -1) && (deltaX == -1)  && gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile-1,currentRank - 1) == 'w') {
                //Log.e("Pawn", "Db");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == -1) ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'w') ) {
                //Log.e("Pawn", "Eb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    //Log.e("Pawn", "E2b");
                    game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == 1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'w') ) {
                //Log.e("Pawn", "Fb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    //Log.e("Pawn", "F2b");
                    game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    return true;
                }
            }
        }
        // invalid
        return false;
    }
    //************************************************************************************************************//
    public boolean canPawnAttackThisSquare(int oldX, int oldY, int newX, int newY) {
        int deltaX = newX - oldX;
        int deltaY = newY - oldY;
        int currentFile = this.getFile(); // column in board[][]
        int currentRank = this.getRank(); // row in board[][]
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

        //Log.e("Pawn", Boolean.toString(this.hasthisMoved()));
        // white
        if (this.isWhite() == true) {
            //capture enemy piece (diagonally to the right)
            if ((deltaY == 1) && (deltaX == 1)) {
                //this.moved2SpacesLastTurn = false;
                //checkForPromotion(newY);
                //Log.e("Pawn", "Cw");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == 1) && (deltaX == -1)) {
                //this.moved2SpacesLastTurn = false;
                //checkForPromotion(newY);
                //Log.e("Pawn", "Dw");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == 1)////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'b') ) {
                //Log.e("Pawn", "Ew");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    //Log.e("Pawn", "E2w");
                    game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    //this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == 1) && (deltaX == -1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'b') ) {
                //Log.e("Pawn", "Fw");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    //Log.e("Pawn", "F2w");
                    //game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    //this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
        }
        // black
        else {
            //capture enemy piece (diagonally to the right)
            if ((deltaY == -1) && (deltaX == 1)) {
                //this.moved2SpacesLastTurn = false;
                //checkForPromotion(newY);
                //Log.e("Pawn", "Cb");
                return true;
            }
            //capture enemy piece (diagonally to the left)
            else if ((deltaY == -1) && (deltaX == -1)) {
                //this.moved2SpacesLastTurn = false;
                //checkForPromotion(newY);
                //Log.e("Pawn", "Db");
                return true;
            }
            // en passant diagonally to the right (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == -1) ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile - 1,currentRank) == 'w') ) {
                //Log.e("Pawn", "Eb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile - 1, currentRank) == true) {
                    //Log.e("Pawn", "E2b");
                    //game.removeThePawnLocatedAt(currentFile - 1, currentRank);
                    //this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
            // en passant diagonally to the left (send a message to the chessboard if this occured so piece can be removed)
            else if ((deltaY == -1) && (deltaX == 1)
                    && (gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile + 1,currentRank) == 'w') ) {
                //Log.e("Pawn", "Fb");
                // now check to see if that piece moved 2 spaces last turn and if so remove it
                if (game.hasThePawnLocatedHereMoved2SpacesLastTurn(currentFile + 1, currentRank) == true) {
                    //Log.e("Pawn", "F2b");
                    //game.removeThePawnLocatedAt(currentFile + 1, currentRank);
                    //this.moved2SpacesLastTurn = false;
                    return true;
                }
            }
        }
        // invalid
        return false;
    }
    //************************************************************************************************************//
    public int getFile() {
        return super.getFile();
    }
    //************************************************************************************************************//
    public int getRank() {
        return super.getRank();
    }
    //************************************************************************************************************//
    public void setFileRank(int file, int rank) {
        super.setFileRank(file, rank);
    }
    //************************************************************************************************************//
    public boolean hasMoved2SpacesLastTurn() {
        return moved2SpacesLastTurn;
    }
    //************************************************************************************************************//
    public boolean checkForPromotion(int finalRank){
        //Log.e("Pawn.checkforpromote", this.getName() + ", " + Integer.toString(this.getFile()) + " " + Integer.toString(this.getRank())
        //        + ", " + Integer.toString(finalRank) );
        char color = this.getName().charAt(0);
        //Log.e("Pawn", Character.toString(color) + Integer.toString(finalRank));
        if( (color == 'w' && finalRank == 7) || (color == 'b' && finalRank == 0)){
            this.promote();
            //Log.e("Pawn", "01");
            return true;
        }
        return false;
    }
    //************************************************************************************************************//
    public void setMoved2SpacesLastTurn(boolean value){
        this.moved2SpacesLastTurn = value;
    }
    //************************************************************************************************************//
    public void promote(){
        Log.e("Pawn.promote", this.getName() + ", " + Integer.toString(this.getFile()) + " " + Integer.toString(this.getRank()));
        Pawn.pawnToPromote = this;

        AlertDialog.Builder promotionDialog = new AlertDialog.Builder(getGameScreen());
        promotionDialog.setTitle("Promote pawn to: ");
        String[] options = {"Knight", "Bishop", "Rook", "Queen"};

        promotionDialog.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Pawn.choiceForPawnPromotion = which;
                getGame().promotePawn(Pawn.pawnToPromote,Pawn.choiceForPawnPromotion);
                //Log.e("Pawn", "which = " + Integer.toString(which) + ", choiceForPawnPromotion = " + Integer.toString(choiceForPawnPromotion));
            }
        });
        promotionDialog.create().show();
    }
}
package com.model;

import android.util.Log;

import com.GameScreen;

public class Bishop extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
//*************************************************METHODS****************************************************//
    public Bishop(String color, int file, int rank, Game game, GameScreen gameScreen) {
        super(color, file, rank, game, gameScreen);
        this.setName();
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wB ");
        } else {
            super.setName("bB ");
        }
    }
    //************************************************************************************************************//
    public String getName() {
        return super.getName();
    }
    //************************************************************************************************************//
    public boolean canMove(int oldX, int oldY, int newX, int newY/*, String[][] board, ChessBoard chessBoard*/) {
        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);
        int currentFile = this.getFile(); // column in board[][]
        int currentRank = this.getRank(); // row in board[][]
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

//////////////////////////////Moving Diagonally//////////////////////////////
        if ( (deltaX == deltaY) && (deltaX != 0) ) {
            //Log.e("Bishop", "A");
            // no need to check if bishop is trying to move through another piece if deltaX == 1
            if(deltaX == 1){
                //Log.e("Bishop", "B");
                return true;
            }
            if(newY > oldY){
                //Log.e("Bishop", "C");
                // moving up and to the right
                if(newX > oldX){
                    //Log.e("Bishop", "D");
                    for(int i = oldX+1, j = oldY+1; i < newX; i++,j++){
                        //Log.e("Bishop", "E");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Bishop", "F");
                            return false;
                        }
                    }
                }
                // moving up and to the left
                else{
                    //Log.e("Bishop", "G");
                    for(int i = oldX-1, j = oldY+1; j < newY; i--,j++){
                        //Log.e("Bishop", "H");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Bishop", "I");
                            return false;
                        }
                    }
                }
            }
            else{
                // moving down and to the right
                if(newX > oldX){
                    //Log.e("Bishop", "J");
                    for(int i = oldX+1, j = oldY-1; i < newX; i++,j--){
                        //Log.e("Bishop", "K");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Bishop", "L");
                            return false;
                        }
                    }
                }
                // moving down and to the left
                else{
                    //Log.e("Bishop", "M");
                    for(int i = oldX-1, j = oldY-1; i > newX; i--,j--){
                        //Log.e("Bishop", "M");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Bishop", "N");
                            return false;
                        }
                    }
                }
            }
            //Log.e("Bishop", "O");
            // all error checks passed
            return true;
        }
        //Log.e("Bishop", "P");
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
}
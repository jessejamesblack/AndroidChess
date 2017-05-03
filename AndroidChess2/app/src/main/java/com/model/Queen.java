package com.model;

import com.GameScreen;

public class Queen extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
//*************************************************METHODS****************************************************//
    public Queen(String color, int file, int rank, Game game, GameScreen gameScreen) {
        super(color, file, rank, game, gameScreen);
        this.setName();
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wQ ");
        } else {
            super.setName("bQ ");
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
            //Log.e("Queen", "A");
            // no need to check if Queen is trying to move through another piece if deltaX == 1
            if(deltaX == 1){
                //Log.e("Queen", "B");
                return true;
            }
            if(newY > oldY){
                //Log.e("Queen", "C");
                // moving up and to the right
                if(newX > oldX){
                    //Log.e("Queen", "D");
                    for(int i = oldX+1, j = oldY+1; i < newX; i++,j++){
                        //Log.e("Queen", "E");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Queen", "F");
                            return false;
                        }
                    }
                }
                // moving up and to the left
                else{
                    //Log.e("Queen", "G");
                    for(int i = oldX-1, j = oldY+1; j < newY; i--,j++){
                        //Log.e("Queen", "H");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Queen", "I");
                            return false;
                        }
                    }
                }
            }
            else{
                // moving down and to the right
                if(newX > oldX){
                    //Log.e("Queen", "J");
                    for(int i = oldX+1, j = oldY-1; i < newX; i++,j--){
                        //Log.e("Queen", "K");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Queen", "L");
                            return false;
                        }
                    }
                }
                // moving down and to the left
                else{
                    //Log.e("Queen", "M");
                    for(int i = oldX-1, j = oldY-1; i > newX; i--,j--){
                        //Log.e("Queen", "M");
                        // piece trying to move through a non-empty space
                        if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,j) != 'e'){
                            //Log.e("Queen", "N");
                            return false;
                        }
                    }
                }
            }
            //Log.e("Queen", "O");
            // all error checks passed
            return true;
        }
//////////////////////////////Moving Linearly//////////////////////////////
        if (deltaX == 0 && deltaY >= 1) {
            //Log.e("Queen2", "A");
            // no need to check if Queen2 is trying to move through another piece if deltaY == 1
            if(Math.abs(deltaY) == 1){
                return true;
            }
            // moving up
            if(newY - oldY > 0){
                //Log.e("Queen2", "B");
                for(int i = oldY+1; i < newY; i++){
                    //Log.e("Queen2", "C");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Queen2", "D");
                        return false;
                    }
                }
            }
            // moving down
            else{
                //Log.e("Queen2", "E");
                for(int i = oldY-1; i > newY; i--){
                    //Log.e("Queen2", "F");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Queen2", "G");
                        return false;
                    }
                }
            }
            //Log.e("Queen2", "H");
            return true;
        }
        else if (deltaX >= 1 && deltaY == 0) {
            //Log.e("Queen2", "I");
            // no need to check if Queen2 is trying to move through another piece if deltaX == 1
            if(Math.abs(deltaX) == 1){
                //Log.e("Queen2", "J");
                return true;
            }
            // moving right
            if(newX - oldX > 0){
                //Log.e("Queen2", "K");
                for(int i = oldX+1; i < newX; i++){
                    //Log.e("Queen2", "L");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Queen2", "M");
                        //System.out.println("Q-G");
                        return false;
                    }
                }
            }
            // moving left
            else{
                //Log.e("Queen2", "N");
                for(int i = oldX-1; i > newX; i--){
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Queen2", "O");
                        //System.out.println("Q-H");
                        return false;
                    }
                }
            }
            //Log.e("Queen2", "P");
            // all error checks passed
            return true;
        }
        //Log.e("Queen2", "Q");
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
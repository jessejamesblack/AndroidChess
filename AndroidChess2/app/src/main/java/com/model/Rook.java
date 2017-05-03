package com.model;

import android.util.Log;

import com.GameScreen;

public class Rook extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
    // 'R' if right-side rook, 'L' if left-side, or 'X' if rook obtained through pawn promotion
    public char side;
    //*************************************************METHODS****************************************************//
    public Rook(String color, int file, int rank, Game game, GameScreen gameScreen, char side) {
        super(color, file, rank, game, gameScreen);
        this.setName();
        this.side = side;
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wR ");
        } else {
            super.setName("bR ");
        }
    }
    //************************************************************************************************************//
    public String getName() {
        return super.getName();
    }
    //************************************************************************************************************//
    public boolean canMove(int oldX, int oldY, int newX, int newY) {
        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);
        int currentFile = this.getFile();
        int currentRank = this.getRank();
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

//////////////////////////////Moving Linearly//////////////////////////////
        if (deltaX == 0 && deltaY >= 1) {
            //Log.e("Rook", "A");
            // no need to check if rook is trying to move through another piece if deltaY == 1
            if(Math.abs(deltaY) == 1){
                this.thisPieceHasMoved();
                return true;
            }
            // moving up
            if(newY - oldY > 0){
                //Log.e("Rook", "B");
                for(int i = oldY+1; i < newY; i++){
                    //Log.e("Rook", "C");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Rook", "D");
                        return false;
                    }
                }
            }
            // moving down
            else{
                //Log.e("Rook", "E");
                for(int i = oldY-1; i > newY; i--){
                    //Log.e("Rook", "F");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Rook", "G");
                        return false;
                    }
                }
            }
            //Log.e("Rook", "H");
            this.thisPieceHasMoved();
            return true;
        }
        else if (deltaX >= 1 && deltaY == 0) {
            //Log.e("Rook", "I");
            // no need to check if rook is trying to move through another piece if deltaX == 1
            if(Math.abs(deltaX) == 1){
                //Log.e("Rook", "J");
                this.thisPieceHasMoved();
                return true;
            }
            // moving right
            if(newX - oldX > 0){
                //Log.e("Rook", "K");
                for(int i = oldX+1; i < newX; i++){
                    //Log.e("Rook", "L");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Rook", "M");
                        //System.out.println("Q-G");
                        return false;
                    }
                }
            }
            // moving left
            else{
                //Log.e("Rook", "N");
                for(int i = oldX-1; i > newX; i--){
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Rook", "O");
                        //System.out.println("Q-H");
                        return false;
                    }
                }
            }
            //Log.e("Rook", "P");
            // all error checks passed
            this.thisPieceHasMoved();
            return true;
        }
        //Log.e("Rook", "Q");
        // invalid
        return false;
    }
    //************************************************************************************************************//
    public boolean canMoveWithoutEditingHasMoved(int oldX, int oldY, int newX, int newY) {
        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);
        int currentFile = this.getFile();
        int currentRank = this.getRank();
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

//////////////////////////////Moving Linearly//////////////////////////////
        if (deltaX == 0 && deltaY >= 1) {
            //Log.e("Rook", "A");
            // no need to check if rook is trying to move through another piece if deltaY == 1
            if(Math.abs(deltaY) == 1){
                return true;
            }
            // moving up
            if(newY - oldY > 0){
                //Log.e("Rook", "B");
                for(int i = oldY+1; i < newY; i++){
                    //Log.e("Rook", "C");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Rook", "D");
                        return false;
                    }
                }
            }
            // moving down
            else{
                //Log.e("Rook", "E");
                for(int i = oldY-1; i > newY; i--){
                    //Log.e("Rook", "F");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(currentFile,i) != 'e'){
                        //Log.e("Rook", "G");
                        return false;
                    }
                }
            }
            //Log.e("Rook", "H");
            return true;
        }
        else if (deltaX >= 1 && deltaY == 0) {
            //Log.e("Rook", "I");
            // no need to check if rook is trying to move through another piece if deltaX == 1
            if(Math.abs(deltaX) == 1){
                //Log.e("Rook", "J");
                return true;
            }
            // moving right
            if(newX - oldX > 0){
                //Log.e("Rook", "K");
                for(int i = oldX+1; i < newX; i++){
                    //Log.e("Rook", "L");
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Rook", "M");
                        return false;
                    }
                }
            }
            // moving left
            else{
                //Log.e("Rook", "N");
                for(int i = oldX-1; i > newX; i--){
                    // piece trying to move through a non-empty space
                    if(gameScreen.returnColorOfPieceOnTheFollowingSquare(i,currentRank) != 'e'){
                        //Log.e("Rook", "O");
                        return false;
                    }
                }
            }
            //Log.e("Rook", "P");
            // all error checks passed
            return true;
        }
        //Log.e("Rook", "Q");
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
package com.model;

import com.GameScreen;

public class Knight extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
//*************************************************METHODS****************************************************//
    public Knight(String color, int file, int rank, Game game, GameScreen gameScreen) {
        super(color, file, rank, game, gameScreen);
        this.setName();
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wN ");
        } else {
            super.setName("bN ");
        }
    }
    //********************************************************************************f****************************//
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

        if (deltaX == 2 && deltaY == 1) {
            return true;
        } else if (deltaX == 1 && deltaY == 2) {
            return true;
        }
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
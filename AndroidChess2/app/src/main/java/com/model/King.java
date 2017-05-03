package com.model;

import android.util.Log;

import com.GameScreen;

public class King extends ChessPiece {
    //*************************************************FIELDS*****************************************************//
//*************************************************METHODS****************************************************//
    public King(String color, int file, int rank, Game game, GameScreen gameScreen) {
        super(color, file, rank, game, gameScreen);
        this.setName();
    }
    //************************************************************************************************************//
    public void setName() {
        if (this.isWhite() == true) {
            super.setName("wK ");
        } else {
            super.setName("bK ");
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
        char color = this.getName().charAt(0);
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

        //System.out.println("deltaX " + deltaX + " deltaY " + deltaY);
        if ( (deltaX == deltaY) && (deltaX == 1) ) {
            this.thisPieceHasMoved();
            game.setPositionOfKing(color, newX, newY);
            return true;
        } else if (deltaX == 0 && deltaY == 1) {
            this.thisPieceHasMoved();
            game.setPositionOfKing(color, newX, newY);
            return true;
        } else if (deltaX == 1 && deltaY == 0) {
            this.thisPieceHasMoved();
            game.setPositionOfKing(color, newX, newY);
            return true;
        }
        // castle to the right
        else if ( (newX - oldX == 2) && (deltaY == 0) && (canThisKingCastle("RIGHT")) ) {
            //Log.e("King", "A");
            this.thisPieceHasMoved();
            game.setPositionOfKing(color, newX, newY);
            return true;
        }
        // castle to the left
        else if ( (newX - oldX == -2) && (deltaY == 0) && (canThisKingCastle("LEFT")) ) {
            //Log.e("King", "B");
            this.thisPieceHasMoved();
            game.setPositionOfKing(color, newX, newY);
            return true;
        }
        //Log.e("King", "C");
        return false;
    }
    //************************************************************************************************************//
    public boolean canMoveWithoutEditingHasMoved(int oldX, int oldY, int newX, int newY) {
        int deltaX = Math.abs(newX - oldX);
        int deltaY = Math.abs(newY - oldY);
        int currentFile = this.getFile(); // column in board[][]
        int currentRank = this.getRank(); // row in board[][]
        Game game = this.getGame(); // contains the chess pieces that may be modified by the pawn
        GameScreen gameScreen = this.getGameScreen(); // contains the state of the board

        if ( (deltaX == deltaY) && (deltaX == 1) ) {
            return true;
        } else if (deltaX == 0 && deltaY == 1) {
            return true;
        } else if (deltaX == 1 && deltaY == 0) {
            return true;
        }
        // castle to the right
        else if ( (newX - oldX == 2) && (deltaY == 0) && (canThisKingCastle("RIGHT")) ) {
            //Log.e("King", "A");
            return true;
        }
        // castle to the left
        else if ( (newX - oldX == -2) && (deltaY == 0) && (canThisKingCastle("LEFT")) ) {
            //Log.e("King", "B");
            return true;
        }
        //Log.e("King", "C");
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
    public boolean KingIsInCheck(String[][] board) {
        int file = this.getFile();
        int rank = this.getRank();
        return false;
    }
    //************************************************************************************************************//
    private boolean canThisKingCastle(String direction){
        //Log.e("King", "100");
        if(this.hasthisMoved() == true){
            //Log.e("King", "200");
            return false;
        }
        // check castle right
        if(direction.compareToIgnoreCase("RIGHT") == 0){
            //Log.e("King", "300");
            Rook rook = getGame().returnRightSideRook(this.getName().charAt(0));
            if(rook == null || rook.hasthisMoved() == true){
                //Log.e("King", "400");
                return false;}
            // if neither of them moved they should be on the same rank
            if( getGameScreen().returnColorOfPieceOnTheFollowingSquare(this.getFile()+1,this.getRank()) != 'e'
                    || getGameScreen().returnColorOfPieceOnTheFollowingSquare(this.getFile()+2,this.getRank()) != 'e' ){
                //Log.e("King", "500");
                return false;
            }
            // also check if either square the king is crossing being attacked by another piece
            // move rook to the left side of king
            //Log.e("King", "600");
            //rook.setFileRank(this.getFile()+1, rook.getRank());
        }
        // check castle left
        else if(direction.compareToIgnoreCase("LEFT") == 0){
            //Log.e("King", "700");
            Rook rook = getGame().returnLeftSideRook(this.getName().charAt(0));
            if(rook == null || rook.hasthisMoved() == true){
                //Log.e("King", "800");
                return false;}
            // if neither of them moved they should be on the same rank
            if( getGameScreen().returnColorOfPieceOnTheFollowingSquare(this.getFile()-1,this.getRank()) != 'e'
                    || getGameScreen().returnColorOfPieceOnTheFollowingSquare(this.getFile()-2,this.getRank()) != 'e' ){
                //Log.e("King", "900");
                return false;
            }
            // also check if either square the king is crossing being attacked by another piece
            // move rook to the right side of king
            //Log.e("King", "1000");
            //rook.setFileRank(this.getFile()-1, rook.getRank());
        }
        return true;
    }
    //************************************************************************************************************//
    public void castle(String direction){
        if(direction.compareToIgnoreCase("RIGHT") == 0){
            //Log.e("King", "300");
            Rook rook = getGame().returnRightSideRook(this.getName().charAt(0));
            rook.setFileRank(this.getFile()+1, rook.getRank());
        }
        // check castle left
        else if(direction.compareToIgnoreCase("LEFT") == 0){
            //Log.e("King", "700");
            Rook rook = getGame().returnLeftSideRook(this.getName().charAt(0));
            rook.setFileRank(this.getFile()-1, rook.getRank());
        }
    }
    //************************************************************************************************************//
}
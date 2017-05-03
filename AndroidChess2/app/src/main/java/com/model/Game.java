package com.model;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.GameScreen;

import java.io.FileNotFoundException;
import java.util.ArrayList;

// ##### System.out ----> Log.e("Game","Insert Message");

public class Game {
    //*************************************************FIELDS*****************************************************//
    private ArrayList<ChessPiece> chessPieceArray;
    private boolean[][] spacesBeingAttackedByAWhitePiece; // [file][rank]
    private boolean[][] spacesBeingAttackedByABlackPiece; // [file][rank]
    private boolean gameOver = false;
    private GameScreen gameScreen;
    private String positionOfWhiteKing = "40";
    private String positionOfBlackKing = "47";

    //*************************************************METHODS****************************************************//
    public Game(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.chessPieceArray = new ArrayList<ChessPiece>();
        this.spacesBeingAttackedByABlackPiece = new boolean[8][8];
        this.spacesBeingAttackedByAWhitePiece = new boolean[8][8];
        this.resetAttackedSpacesArrayBlack(); // sets all elements to false
        this.resetAttackedSpacesArrayWhite(); // sets all elements to false
        //******Populate arrayList with chess pieces******//
        // add pawns
        for (int i = 0; i < 8; i++) {
            Pawn pawn = new Pawn("white", i, 1, this, gameScreen);
            chessPieceArray.add(pawn);
        }
        for (int i = 0; i < 8; i++) {
            Pawn pawn = new Pawn("black", i, 6, this, gameScreen);
            chessPieceArray.add(pawn);
        }
        // add rooks
        Rook rook1 = new Rook("white", 0, 0, this, gameScreen, 'L');
        chessPieceArray.add(rook1);
        Rook rook2 = new Rook("white", 7, 0, this, gameScreen, 'R');
        chessPieceArray.add(rook2);
        Rook rook3 = new Rook("black", 0, 7, this, gameScreen, 'L');
        chessPieceArray.add(rook3);
        Rook rook4 = new Rook("black", 7, 7, this, gameScreen, 'R');
        chessPieceArray.add(rook4);
        // add bishops
        Bishop bishop1 = new Bishop("white", 2, 0, this, gameScreen);
        chessPieceArray.add(bishop1);
        Bishop bishop2 = new Bishop("white", 5, 0, this, gameScreen);
        chessPieceArray.add(bishop2);
        Bishop bishop3 = new Bishop("black", 2, 7, this, gameScreen);
        chessPieceArray.add(bishop3);
        Bishop bishop4 = new Bishop("black", 5, 7, this, gameScreen);
        chessPieceArray.add(bishop4);
        // add knights
        Knight knight1 = new Knight("white", 1, 0, this, gameScreen);
        chessPieceArray.add(knight1);
        Knight knight2 = new Knight("white", 6, 0, this, gameScreen);
        chessPieceArray.add(knight2);
        Knight knight3 = new Knight("black", 1, 7, this, gameScreen);
        chessPieceArray.add(knight3);
        Knight knight4 = new Knight("black", 6, 7, this, gameScreen);
        chessPieceArray.add(knight4);
        // add queens
        Queen queen1 = new Queen("white", 3, 0, this, gameScreen);
        chessPieceArray.add(queen1);
        Queen queen2 = new Queen("black", 3, 7, this, gameScreen);
        chessPieceArray.add(queen2);
        // add kings
        King king1 = new King("white", 4, 0, this, gameScreen);
        chessPieceArray.add(king1);
        King king2 = new King("black", 4, 7, this, gameScreen);
        chessPieceArray.add(king2);

        this.updateSpacesThatAreBeingAttackedByABlackPieceArrayMethod();
        this.updateSpacesThatAreBeingAttackedByAWhitePieceArrayMethod();

        //printAttackSpacesArray();
    }

    //************************************************************************************************************//
    public void ProcessMove(int initialFile, int initialRank, int finalFile, int finalRank) {
        //Log.e("Game.ProcessMove", "whiteKing = " + positionOfWhiteKing + ", blackKing = " + positionOfBlackKing);
        // white's turn
        if (gameScreen.getItsWhitesMove() == true) {
            // check if white has any valid moves, otherwise stalemate
            this.beginWhitesTurn(initialFile, initialRank, finalFile, finalRank);
            // update array
            this.resetAttackedSpacesArrayWhite(); // sets all elements to false
            this.updateSpacesThatAreBeingAttackedByAWhitePieceArrayMethod();
            // check if black king is in check
            if(isBlackKingIsInCheck()){
                Toast.makeText(gameScreen, "Black in check", Toast.LENGTH_LONG).show();
                if(this.doesCurrentPlayerHaveValidMove('b') == 999){
                    gameScreen.endGame('b', this.isWhiteKingIsInCheck(), this.isBlackKingIsInCheck());
                }
            }
            // if black is in check and has no moves white wins
            gameScreen.setItsWhitesMove(false);
        }
        // black's turn
        else {
            // check if black has any valid moves, otherwise stalemate
            this.beginBlacksTurn(initialFile, initialRank, finalFile, finalRank);
            // update array
            this.resetAttackedSpacesArrayBlack(); // sets all elements to false
            this.updateSpacesThatAreBeingAttackedByABlackPieceArrayMethod();
            // check if white king is in check
            if(isWhiteKingIsInCheck()){
                Toast.makeText(gameScreen, "White in check", Toast.LENGTH_LONG).show();
                if(this.doesCurrentPlayerHaveValidMove('w') == 999){
                    gameScreen.endGame('w', this.isWhiteKingIsInCheck(), this.isBlackKingIsInCheck());
                }
            }
            // if white is in check and has no moves black wins
            gameScreen.setItsWhitesMove(true);
        }
    }

    //************************************************************************************************************//
    public void beginWhitesTurn(int initialFile, int initialRank, int finalFile, int finalRank) {
        ChessPiece pieceAttemptingToBeMoved = null;

        // locate the piece the user is trying to move in the chessPieceArrayList
        int index = returnIndexOfPieceLocatedAt(initialFile, initialRank);
        if (index != -1) {
            pieceAttemptingToBeMoved = chessPieceArray.get(index);
        }

        // an empty space or space outside of the board was selected
        if (pieceAttemptingToBeMoved == null) {
            return;
        }
        // the move did not result in the piece moving to a new location
        if (initialFile == finalFile && initialRank == finalRank) {
            return;
        }

        // execute the move
        // if there is a piece on the new space, remove it from play by removing it from the chessPiece Array
        int index2 = returnIndexOfPieceLocatedAt(finalFile, finalRank);
        if (index2 != -1) {
            chessPieceArray.remove(index2);
        }

        pieceAttemptingToBeMoved.thisPieceHasMoved();
        if(pieceAttemptingToBeMoved instanceof Pawn){
            ((Pawn) pieceAttemptingToBeMoved).checkForPromotion(finalRank);
            if(Math.abs(finalRank-initialRank) == 2){
                ((Pawn) pieceAttemptingToBeMoved).setMoved2SpacesLastTurn(true);
            }
            else{
                ((Pawn) pieceAttemptingToBeMoved).setMoved2SpacesLastTurn(false);
            }
        }
        if(pieceAttemptingToBeMoved instanceof King){
            if((finalRank-initialRank) == 0){
                if(finalFile-initialFile == 2){
                    ((King) pieceAttemptingToBeMoved).castle("RIGHT");
                }
                else if(finalFile-initialFile == -2){
                    ((King) pieceAttemptingToBeMoved).castle("LEFT");
                }
            }
        }
        pieceAttemptingToBeMoved.setFileRank(finalFile, finalRank);
    }

    //************************************************************************************************************//
    public void beginBlacksTurn(int initialFile, int initialRank, int finalFile, int finalRank) {
        // variable declarations
        ChessPiece pieceAttemptingToBeMoved = null;
        // if a valid move is entered, the loop will break, otherwise it will keep repeating

        // locate the piece the user is trying to move in the chessPieceArrayList
        int index = returnIndexOfPieceLocatedAt(initialFile, initialRank);
        if (index != -1) {
            pieceAttemptingToBeMoved = chessPieceArray.get(index);
        }

        // an empty space or space outside of the board was selected
        if (pieceAttemptingToBeMoved == null) {
            return;
        }
        // the move did not result in the piece moving to a new location
        if (initialFile == finalFile && initialRank == finalRank) {
            return;
        }

        // execute the move
        // if there is a piece on the new space, remove it from play by removing it from the chessPiece Array
        int index2 = returnIndexOfPieceLocatedAt(finalFile, finalRank);
        if (index2 != -1) {
            chessPieceArray.remove(index2);
        }

        pieceAttemptingToBeMoved.thisPieceHasMoved();
        if(pieceAttemptingToBeMoved instanceof Pawn){
            ((Pawn) pieceAttemptingToBeMoved).checkForPromotion(finalRank);
            if(Math.abs(finalRank-initialRank) == 2){
                ((Pawn) pieceAttemptingToBeMoved).setMoved2SpacesLastTurn(true);
            }
            else{
                ((Pawn) pieceAttemptingToBeMoved).setMoved2SpacesLastTurn(false);
            }
        }
        if(pieceAttemptingToBeMoved instanceof King){
            if((finalRank-initialRank) == 0){
                if(finalFile-initialFile == 2){
                    ((King) pieceAttemptingToBeMoved).castle("RIGHT");
                }
                else if(finalFile-initialFile == -2){
                    ((King) pieceAttemptingToBeMoved).castle("LEFT");
                }
            }
        }
        pieceAttemptingToBeMoved.setFileRank(finalFile, finalRank);
    }

    //************************************************************************************************************//
    public boolean whiteHasWon() {
        return true;
    }

    //************************************************************************************************************//
    public boolean blackHasWon() {
        return true;
    }

    //************************************************************************************************************//
    public boolean gameIsOver() {
        return this.gameOver;
    }

    //************************************************************************************************************//
    public void printChessPieceArray() {
        for (int i = 0; i < this.chessPieceArray.size(); i++) {
            Log.e("Game",this.chessPieceArray.get(i).getName() + ", " + this.chessPieceArray.get(i).getFile()
                    + this.chessPieceArray.get(i).getRank() + Boolean.toString(this.chessPieceArray.get(i).hasthisMoved()));
        }
    }

    //************************************************************************************************************//
    public int returnIndexOfPieceLocatedAt(int file, int rank) {
        for (int i = 0; i < this.chessPieceArray.size(); i++) {
            // find the piece located at "initialPosition"
            if (file == this.chessPieceArray.get(i).getFile() &&
                    rank == this.chessPieceArray.get(i).getRank()) {
                return i;
            }
        }
        return -1;
    }

    //************************************************************************************************************//
    public boolean hasThePawnLocatedHereMoved2SpacesLastTurn(int file, int rank) {
        int index = returnIndexOfPieceLocatedAt(file, rank);
        if (index == -1) {
            return false;
        }
        ChessPiece piece = chessPieceArray.get(index);
        if (piece == null) {
            return false;
        }
        if(piece.getName().charAt(1) != 'p'){
            return false;
        }
        return ((Pawn) piece).hasMoved2SpacesLastTurn();
    }

    //************************************************************************************************************//
    public void removeThePawnLocatedAt(int file, int rank) {
        int index = returnIndexOfPieceLocatedAt(file, rank);
        if (index == -1) {
            return;
        }
        ChessPiece piece = chessPieceArray.get(index);
        if (piece == null) {
            return;
        }
        if (piece.getName().charAt(1) != 'p') {
            return;
        }
        this.chessPieceArray.remove(index);
    }

    //************************************************************************************************************//
    public boolean wouldWhiteKingStillBeInCheck(int initialFile, int initialRank, int finalFile, int finalRank, ChessPiece pieceAttemptingToBeMoved) {
        //printAttackSpacesArray();
        // store king's old position (will be of use if king is moved)
        String positionOfWhiteKingCopy = this.positionOfWhiteKing;
        //Log.e("Game.wouldKingbein", "A");
        // save content description of square [finalFile][finalRank]
        String tag = Integer.toString(finalFile) + Integer.toString(finalRank);
        //Log.e("Game.wouldKingbein", "B");
        ImageView imageView = this.gameScreen.returnSquareWithTheFollowingTag(tag);
        //Log.e("Game.wouldKingbein", "C");
        String contentDescription = (String) imageView.getContentDescription();
        //Log.e("Game.wouldKingbein", "D");
        // save a reference to the captured piece (will be null if no piece was captured)
        int index = this.returnIndexOfPieceLocatedAt(finalFile,finalRank);
        //Log.e("Game.wouldKingbein", "E");
        ChessPiece capturedPiece = null;
        if(index != -1){
            capturedPiece = this.chessPieceArray.get(index);
            //Log.e("Game.wouldKingbein", "F");
            this.chessPieceArray.remove(index);
            //Log.e("Game.wouldKingbein", "G");
        }
        // move pieceAttempting to be moved
        pieceAttemptingToBeMoved.setFileRank(finalFile, finalRank);
        //Log.e("Game.wouldKingbein", "H");
        // change king's position if the king was moved
        if(pieceAttemptingToBeMoved instanceof King){
            String newPosition = Integer.toString(finalFile) + Integer.toString(finalRank);
            //Log.e("Game.wouldKingbein", "I");
            this.positionOfWhiteKing = newPosition;
            //Log.e("Game.wouldKingbein", "J");
        }
        // change content description
        imageView.setContentDescription(pieceAttemptingToBeMoved.getName());
        //Log.e("Game.wouldKingbein", "K");
        // update black attacked spaces array
        this.resetAttackedSpacesArrayBlack();
        //Log.e("Game.wouldKingbein", "L");
        this.updateSpacesThatAreBeingAttackedByABlackPieceArrayMethod();
        //Log.e("Game.wouldKingbein", "M");
        //printAttackSpacesArray();
        // see if king would be in check
        boolean result = this.isWhiteKingIsInCheck();
        //Log.e("Game.wouldKingbein", "N");
        // restore king's previous position
        this.positionOfWhiteKing = positionOfWhiteKingCopy;
        //Log.e("Game.wouldKingbein", "O");
        // restore content description
        imageView.setContentDescription(contentDescription);
        //Log.e("Game.wouldKingbein", "P");
        // restore captured piece
        if(index != -1 && capturedPiece != null){
            this.chessPieceArray.add(capturedPiece);
            //Log.e("Game.wouldKingbein", "Q");
        }
        // move piece back to original position
        pieceAttemptingToBeMoved.setFileRank(initialFile, initialRank);
        //Log.e("Game.wouldKingbein", "R");
        // update attack spaces array
        this.resetAttackedSpacesArrayBlack();
        //Log.e("Game.wouldKingbein", "S");
        this.updateSpacesThatAreBeingAttackedByABlackPieceArrayMethod();
        //Log.e("Game.wouldKingbein", "T");
        //Log.e("Game.WStillBeInCheck", Boolean.toString(result));
        //printAttackSpacesArray();
        return result;
    }

    //************************************************************************************************************//
    public boolean wouldBlackKingStillBeInCheck(int initialFile, int initialRank, int finalFile, int finalRank, ChessPiece pieceAttemptingToBeMoved) {
        //printAttackSpacesArray();
        // store king's old position (will be of use if king is moved)
        String positionOfBlackKingCopy = this.positionOfBlackKing;
        // save content description of square [finalFile][finalRank]
        String tag = Integer.toString(finalFile) + Integer.toString(finalRank);
        ImageView imageView = this.gameScreen.returnSquareWithTheFollowingTag(tag);
        String contentDescription = (String) imageView.getContentDescription();
        // save a reference to the captured piece (will be null if no piece was captured)
        int index = this.returnIndexOfPieceLocatedAt(finalFile,finalRank);
        ChessPiece capturedPiece = null;
        if(index != -1){
            capturedPiece = this.chessPieceArray.get(index);
            this.chessPieceArray.remove(index);
        }
        // move pieceAttempting to be moved
        pieceAttemptingToBeMoved.setFileRank(finalFile, finalRank);
        // change king's position if the king was moved
        if(pieceAttemptingToBeMoved instanceof King){
            String newPosition = Integer.toString(finalFile) + Integer.toString(finalRank);
            this.positionOfBlackKing = newPosition;
        }
        // change content description
        imageView.setContentDescription(pieceAttemptingToBeMoved.getName());
        // update black attacked spaces array
        this.resetAttackedSpacesArrayWhite();
        this.updateSpacesThatAreBeingAttackedByAWhitePieceArrayMethod();
        //printAttackSpacesArray();
        // see if king would be in check
        boolean result = this.isBlackKingIsInCheck();
        // restore king's previous position
        this.positionOfBlackKing = positionOfBlackKingCopy;
        // restore content description
        imageView.setContentDescription(contentDescription);
        // restore captured piece
        if(index != -1 && capturedPiece != null){
            this.chessPieceArray.add(capturedPiece);
        }
        // move piece back to original position
        pieceAttemptingToBeMoved.setFileRank(initialFile, initialRank);
        // update attack spaces array
        this.resetAttackedSpacesArrayWhite();
        this.updateSpacesThatAreBeingAttackedByAWhitePieceArrayMethod();
        Log.e("Game.WStillBeInCheck", Integer.toString(initialFile) + Integer.toString(initialRank) + "->"
                + Integer.toString(finalFile) + Integer.toString(finalRank) + ", " +  Boolean.toString(result));
        //printAttackSpacesArray();
        return result;
    }

    //************************************************************************************************************//
    public void updateSpacesThatAreBeingAttackedByABlackPieceArrayMethod() {
        // cycle through all black pieces
        for(ChessPiece piece: chessPieceArray){
            if(piece.getName().charAt(0) == 'b'){
                int currentFile = piece.getFile();
                int currentRank = piece.getRank();
                // cycle through every possible move
                // cycle through all files
                for(int i = 0; i < 8; i++){
                    // cycle through all ranks
                    for(int j = 0; j < 8; j++){
                        // pawns can only attack diagonally
                        if(piece instanceof Pawn){
                            if(((Pawn) piece).canPawnAttackThisSquare(currentFile, currentRank, i, j) && Math.abs(i-currentFile) == Math.abs(j-currentRank)){
                                this.spacesBeingAttackedByABlackPiece[i][j] = true;
                            }
                        }
                        else if(piece instanceof King){
                            if(((King) piece).canMoveWithoutEditingHasMoved(currentFile, currentRank, i, j)){
                                this.spacesBeingAttackedByABlackPiece[i][j] = true;
                            }
                        }
                        else if(piece instanceof Rook){
                            if(((Rook) piece).canMoveWithoutEditingHasMoved(currentFile, currentRank, i, j)){
                                this.spacesBeingAttackedByABlackPiece[i][j] = true;
                            }
                        }
                        else if(piece.canMove(currentFile, currentRank, i, j)){
                            this.spacesBeingAttackedByABlackPiece[i][j] = true;
                        }
                        //Log.e("Game.updateAttackSpaces", currentFile+""+currentRank + "->" + i+""+j + " = " + piece.canMove(currentFile, currentRank, i, j));
                    }
                }
            }
        }
    }

    //************************************************************************************************************//
    public void updateSpacesThatAreBeingAttackedByAWhitePieceArrayMethod() {
        // cycle through all white pieces
        for(ChessPiece piece: chessPieceArray){
            if(piece.getName().charAt(0) == 'w'){
                //Log.e("Game", piece.getName());
                int currentFile = piece.getFile();
                int currentRank = piece.getRank();
                // cycle through every possible move
                // cycle through all files
                for(int i = 0; i < 8; i++){
                    // cycle through all ranks
                    for(int j = 0; j < 8; j++){
                        // pawns can only attack diagonally
                        if(piece instanceof Pawn){
                            if(((Pawn) piece).canPawnAttackThisSquare(currentFile, currentRank, i, j) && Math.abs(i-currentFile) == Math.abs(j-currentRank)){
                                this.spacesBeingAttackedByAWhitePiece[i][j] = true;
                            }
                        }
                        else if(piece instanceof King){
                            if(((King) piece).canMoveWithoutEditingHasMoved(currentFile, currentRank, i, j)){
                                this.spacesBeingAttackedByAWhitePiece[i][j] = true;
                            }
                        }
                        else if(piece instanceof Rook){
                            if(((Rook) piece).canMoveWithoutEditingHasMoved(currentFile, currentRank, i, j)){
                                this.spacesBeingAttackedByAWhitePiece[i][j] = true;
                            }
                        }
                        else if(piece.canMove(currentFile, currentRank, i, j)){
                            this.spacesBeingAttackedByAWhitePiece[i][j] = true;
                        }
                        //Log.e("Game.updateAttackSpaces", currentFile+""+currentRank + "->" + i+""+j + " = " + piece.canMove(currentFile, currentRank, i, j));
                    }
                }
            }
        }
    }

    //************************************************************************************************************//
    public void printAttackSpacesArray() {
        Log.e("Game", "Attacked by black pieces");
        String line = "";
        for (int j = 7; j >= 0; j--) {
            for (int k = 0; k < 8; k++) {
                if(this.spacesBeingAttackedByABlackPiece[k][j]){line += "T--";}
                else{line += "F--";}
                //Log.e("Game", j+""+k + " = " + this.spacesBeingAttackedByABlackPiece[j][k]);
            }
            Log.e("Game", line);
            line = "";
        }
        /*
        Log.e("Game", "--------------------------------------------------------------");
        Log.e("Game", "Attacked by white pieces");
        line = "";
        for (int j = 7; j >= 0; j--) {
            for (int k = 0; k < 8; k++) {
                if(this.spacesBeingAttackedByAWhitePiece[k][j]){line += "T--";}
                else{line += "F--";}
                //Log.e("Game", j+""+k + " = " + this.spacesBeingAttackedByABlackPiece[j][k]);
            }
            Log.e("Game", line);
            line = "";
        }
        */
    }

    //************************************************************************************************************//
    public void resetAttackedSpacesArrayBlack() {
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                spacesBeingAttackedByABlackPiece[i][k] = false;
            }
        }
    }

    //************************************************************************************************************//
    public void resetAttackedSpacesArrayWhite() {
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                spacesBeingAttackedByAWhitePiece[i][k] = false;
            }
        }
    }

    //************************************************************************************************************//
    public ArrayList<ChessPiece> getChessPieceArray() {
        return this.chessPieceArray;
    }

    //************************************************************************************************************//
    public boolean isThisAValidMove(int initialFile, int initialRank, int finalFile, int finalRank) {
        if(initialFile > 7 || initialFile < 0 || initialRank > 7 || initialRank < 0
                || finalFile > 7 || finalFile < 0 || finalRank > 7 || finalRank < 0
                    || (initialFile == finalFile && initialRank == finalRank) ){
            return false;
        }
        // player clicked consecutively clicked 2 empty spaces
        if(returnIndexOfPieceLocatedAt(initialFile, initialRank) == -1){
            return false;
        }
        ChessPiece piece = chessPieceArray.get(returnIndexOfPieceLocatedAt(initialFile, initialRank));
        if(piece == null){
            return false;
        }
        // make sure white isn't trying to capture a white piece or black isn't trying to capture a black piece
        int index = returnIndexOfPieceLocatedAt(finalFile, finalRank);
        ChessPiece pieceToBeCaptured = null;
        if(index != -1){
            pieceToBeCaptured = chessPieceArray.get(index);
            if( (gameScreen.getItsWhitesMove() == true && pieceToBeCaptured.getName().charAt(0) == 'w')
                    || (gameScreen.getItsWhitesMove() == false && pieceToBeCaptured.getName().charAt(0) == 'b')){
                return false;
            }
        }

        if(piece instanceof Pawn){
            if(((Pawn) piece).canMoveWithoutEditingHasMoved(initialFile, initialRank, finalFile, finalRank) == true){
                if(gameScreen.getItsWhitesMove() == true
                        && wouldWhiteKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false) {
                    return true;
                }
                else if(gameScreen.getItsWhitesMove() == false
                        && wouldBlackKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false){
                    return true;
                }
            }
        }
        else if(piece instanceof King){
            //Log.e("Game.isThisAValidMove", "King");
            if(((King) piece).canMoveWithoutEditingHasMoved(initialFile, initialRank, finalFile, finalRank) == true){
                if(gameScreen.getItsWhitesMove() == true
                        && wouldWhiteKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false) {
                    return true;
                }
                else if(gameScreen.getItsWhitesMove() == false
                        && wouldBlackKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false){
                    return true;
                }
            }
        }
        else if(piece instanceof Rook){
            //Log.e("Game.isThisAValidMove", "Rook");
            if(((Rook) piece).canMoveWithoutEditingHasMoved(initialFile, initialRank, finalFile, finalRank) == true){
                if(gameScreen.getItsWhitesMove() == true
                        && wouldWhiteKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false) {
                    return true;
                }
                else if(gameScreen.getItsWhitesMove() == false
                        && wouldBlackKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false){
                    return true;
                }
            }
        }
        else if(piece.canMove(initialFile, initialRank, finalFile, finalRank) == true){
            //Log.e("Game.isThisAValidMove", "Other");
            if(gameScreen.getItsWhitesMove() == true
                    && wouldWhiteKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false) {
                return true;
            }
            else if(gameScreen.getItsWhitesMove() == false
                    && wouldBlackKingStillBeInCheck(initialFile, initialRank, finalFile, finalRank,  piece) == false){
                return true;
            }
        }
        return false;
    }

    //************************************************************************************************************//
    public void promotePawn(Pawn pawn, int choice){
        //printChessPieceArray();
        //Log.e("Game","------------------------------------------------------------------------");
        //Log.e("Game", "@ " + pawn.getName() + ", " +Integer.toString(choice));
        ChessPiece piece = null;
        String color;
        if(pawn.getName().charAt(0) == 'w'){color = "white";}
        else{color = "black";}
        if(choice == 0){piece = new Knight(color, pawn.getFile(), pawn.getRank(), this, this.gameScreen);}
        else if(choice == 1){piece = new Bishop(color, pawn.getFile(), pawn.getRank(), this, this.gameScreen);}
        else if(choice == 2){piece = new Rook(color, pawn.getFile(), pawn.getRank(), this, this.gameScreen, 'X');}
        else if(choice == 3){piece = new Queen(color, pawn.getFile(), pawn.getRank(), this, this.gameScreen);}
        if(pawn != null){
            this.chessPieceArray.remove(pawn);
        }
        if(piece != null){
            this.chessPieceArray.add(piece);
        }
        //printChessPieceArray();
        gameScreen.updateChessBoard();
    }

    //************************************************************************************************************//
    public Rook returnRightSideRook(char color){
        //Log.e("Game", "@");
        String target = Character.toString(color) + "R ";
        for(ChessPiece piece: chessPieceArray){
            //Log.e("Game", "@@");
            if(piece.getName().compareToIgnoreCase(target) == 0){
                //Log.e("Game", "@@@");
                Rook rook = (Rook) piece;
                if(rook.side == 'R'){
                    //Log.e("Game", "@@@@");
                    return rook;
                }
            }
        }
        return null;
    }

    //************************************************************************************************************//
    public Rook returnLeftSideRook(char color){
        //Log.e("Game", "@");
        String target = Character.toString(color) + "R ";
        for(ChessPiece piece: chessPieceArray){
            //Log.e("Game", "@@");
            if(piece.getName().compareToIgnoreCase(target) == 0){
                //Log.e("Game", "@@@");
                Rook rook = (Rook) piece;
                if(rook.side == 'L'){
                    //Log.e("Game", "@@@@");
                    return rook;
                }
            }
        }
        return null;
    }

    //************************************************************************************************************//
    public void setPositionOfKing(char color, int file, int rank){
        if(color == 'w'){
            this.positionOfWhiteKing = Integer.toString(file) + Integer.toString(rank);
        }
        else if(color == 'b'){
            this.positionOfBlackKing = Integer.toString(file) + Integer.toString(rank);
        }
        else{
            //Log.e("Game.setPositionOfKing", "Error in call to setPositionOfKing");
        }
    }

    //************************************************************************************************************//
    public boolean isWhiteKingIsInCheck(){
        int file = this.positionOfWhiteKing.charAt(0) - 48;
        int rank =  this.positionOfWhiteKing.charAt(1) - 48;
        //Log.e("Game.isWhiteKingInCheck", Integer.toString(file) + ", " + Integer.toString(rank) + ", " + Boolean.toString(this.spacesBeingAttackedByABlackPiece[file][rank]));
        return this.spacesBeingAttackedByABlackPiece[file][rank];
    }

    //************************************************************************************************************//
    public boolean isBlackKingIsInCheck(){
        int file = this.positionOfBlackKing.charAt(0) - 48;
        int rank =  this.positionOfBlackKing.charAt(1) - 48;
        return this.spacesBeingAttackedByAWhitePiece[file][rank];
    }

    //************************************************************************************************************//
    public void autoMove(){
        char player;
        if(gameScreen.getItsWhitesMove()){
            player = 'w';
        }
        else{
            player = 'b';
        }
        try{
            // cycle through all pieces of current players color pieces
            for(ChessPiece piece: this.chessPieceArray){
                if(piece.getName().charAt(0) == player){
                    int currentFile = piece.getFile();
                    int currentRank = piece.getRank();
                    // cycle through every possible move
                    // cycle through all files
                    for(int i = 0; i < 8; i++){
                        // cycle through all ranks
                        for(int j = 0; j < 8; j++){
                            if(isThisAValidMove(currentFile, currentRank, i, j)){
                                // execute the move by feeding it into the Game class
                                this.ProcessMove(currentFile, currentRank, i, j);
                                piece.thisPieceHasMoved();
                                // update the chess board
                                try {
                                    gameScreen.updateChessBoard(gameScreen.getItsWhitesMove(), currentFile, currentRank, i, j);
                                }
                                catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }                    // now it's blacks turn
                                if(player == 'w'){
                                    gameScreen.setItsWhitesMove(false);
                                    // update the textView
                                    gameScreen.whoseMoveIsIt.setText("Black to move");
                                }
                                else{
                                    gameScreen.setItsWhitesMove(true);
                                    // update the textView
                                    gameScreen.whoseMoveIsIt.setText("White to move");
                                }
                                return;
                            }
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //************************************************************************************************************//
    public int doesCurrentPlayerHaveValidMove(char player){
        // cycle through all pieces of current players color pieces
        for(ChessPiece piece: this.chessPieceArray){
            if(piece.getName().charAt(0) == player){
                int currentFile = piece.getFile();
                int currentRank = piece.getRank();
                // cycle through every possible move
                // cycle through all files
                for(int i = 0; i < 8; i++){
                    // cycle through all ranks
                    for(int j = 0; j < 8; j++){
                        if(isThisAValidMove(currentFile, currentRank, i, j) == true){
                            Log.e("Game.doesCurrentPlayer", "not GAME OVER");
                            return 999;
                        }
                    }
                }
            }
        }
        Log.e("Game.doesCurrentPlayer", "GAME OVER");
        return 999;
    }

    //************************************************************************************************************//
    public ChessPiece returnPieceLocatedAtIndex(int index){
        return this.chessPieceArray.get(index);
    }

    //************************************************************************************************************//

}

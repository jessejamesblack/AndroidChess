package com.model;

import com.GameScreen;

public abstract class ChessPiece {
    //*************************************************FIELDS*****************************************************//
    private int currentFile;
    private int currentRank;
    private String name;
    private boolean isWhite = true;
    private boolean hasMoved = false;
    private Game game;
    private GameScreen gameScreen;
    //*************************************************METHODS****************************************************//
    public ChessPiece(String color, int file, int rank, Game game, GameScreen gameScreen) {
        if (color.equals("black") == true) {
            this.isWhite = false;
        }
        this.setFileRank(file, rank);
        this.game = game;
        this.gameScreen = gameScreen;
    }
    //***************************************************GETTERS**************************************************//
    public boolean isWhite() {
        return this.isWhite;
    }
    //************************************************************************************************************//
    public boolean hasthisMoved() {
        return this.hasMoved;
    }
    //************************************************************************************************************//
    public String getName() {
        return name;
    }
    //************************************************************************************************************//
    public int getFile() {
        return this.currentFile;
    }
    //************************************************************************************************************//
    public int getRank() {
        return this.currentRank;
    }
    //************************************************************************************************************//
    public Game getGame(){
        return this.game;
    }
    //************************************************************************************************************//
    public GameScreen getGameScreen(){
        return this.gameScreen;
    }
    //*****************************************************SETTERS************************************************//
    public void setBlack() {
        this.isWhite = false;
    }
    //************************************************************************************************************//
    public void thisPieceHasMoved() {
        this.hasMoved = true;
    }
    //************************************************************************************************************//
    public void setName(String name) {
        this.name = name;
    }
    //************************************************************************************************************//
    public void setFileRank(int file, int rank) {
        this.currentFile = file;
        this.currentRank = rank;
    }
    //*******************************************************MISC*************************************************//
    public abstract boolean canMove(int oldX, int oldY, int newX, int newY);
//************************************************************************************************************//
}
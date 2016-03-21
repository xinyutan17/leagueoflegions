package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.model.unit.Base;

import java.util.ArrayList;

public class Game {
    public static final String GAME_TYPE = "game_type";
    public enum GameType {CAMPAIGN, MULTIPLAYER, TUTORIAL}

    private Map map;
    private ArrayList<Player> players;
    private ArrayList<Unit> units;
    private ArrayList<Unit> unitAddQueue;
    private ArrayList<Unit> unitRemoveQueue;

    private long startTime;     // start time (milliseconds)
    private float gameTime;     // game time (seconds)
    private float elapsedTime;  // elapsed time since last update (seconds)

    public Game(){
        map = new Map();
        players = new ArrayList<Player>();
        units = new ArrayList<Unit>();
        unitAddQueue = new ArrayList<Unit>();
        unitRemoveQueue = new ArrayList<Unit>();

        startTime = System.currentTimeMillis();
        gameTime = 0f;
        elapsedTime = 0f;
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public void addPlayer(float[] color, float baseX, float baseY) {
        Player player = new Player(this, color);
        player.addUnit(new Base(player, baseX, baseY));
        players.add(player);
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public ArrayList<Unit> getUnitAddQueue() {
        return unitAddQueue;
    }

    public void addUnitAddQueue(Unit unit) {
        unitAddQueue.add(unit);
    }

    public ArrayList<Unit> getUnitRemoveQueue() {
        return unitRemoveQueue;
    }

    public void addUnitRemoveQueue(Unit unit) {
        unitRemoveQueue.add(unit);
    }

    public float getTime() {
        return gameTime;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void tick() {
        float prevGameTime = gameTime;
        gameTime = (System.currentTimeMillis() - startTime)/1000f;
        elapsedTime = gameTime - prevGameTime;

        for(Unit unit : units) {
            unit.tick();
        }

        units.addAll(unitAddQueue);
        units.removeAll(unitRemoveQueue);
    }

    public void tickCleanUp() {
        // This should be called immediately after tick
        unitAddQueue.clear();
        unitRemoveQueue.clear();
    }
}

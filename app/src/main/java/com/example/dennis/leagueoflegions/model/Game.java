package com.example.dennis.leagueoflegions.model;

import com.example.dennis.leagueoflegions.model.unit.Base;
import com.example.dennis.leagueoflegions.model.unit.Unit;

import java.util.ArrayList;

public class Game {
    public static final String GAME_TYPE = "game_type";
    public enum GameType {CAMPAIGN, MULTIPLAYER, TUTORIAL}

    private Map map;
    private ArrayList<Player> players;
    private ArrayList<GameObject> gameObjects;
    private ArrayList<GameObject> gameObjectAddQueue;
    private ArrayList<GameObject> gameObjectRemoveQueue;

    private ArrayList<Unit> units;

    private long startTime;     // start time (milliseconds)
    private float gameTime;     // game time (seconds)
    private float elapsedTime;  // elapsed time since last update (seconds)

    public Game(){
        map = new Map();
        players = new ArrayList<Player>();
        gameObjects = new ArrayList<GameObject>();
        gameObjectAddQueue = new ArrayList<GameObject>();
        gameObjectRemoveQueue = new ArrayList<GameObject>();

        units = new ArrayList<Unit>();

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

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public ArrayList<GameObject> getGameObjectAddQueue() {
        return gameObjectAddQueue;
    }

    public void addGameObjectAddQueue(GameObject gameObject) {
        gameObjectAddQueue.add(gameObject);
    }

    public ArrayList<GameObject> getGameObjectRemoveQueue() {
        return gameObjectRemoveQueue;
    }

    public void addGameObjectRemoveQueue(GameObject gameObject) {
        gameObjectRemoveQueue.add(gameObject);
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

        units.clear();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Unit) {
                units.add((Unit) gameObject);
            }
        }

        for(GameObject gameObject : gameObjects) {
            gameObject.tick();
        }

        gameObjects.addAll(gameObjectAddQueue);
        gameObjects.removeAll(gameObjectRemoveQueue);
    }

    public void tickCleanUp() {
        // This should be called immediately after tick
        gameObjectAddQueue.clear();
        gameObjectRemoveQueue.clear();
    }

    public ArrayList<Unit> getUnitsWithinRadius(float x, float y, float radius) {
        ArrayList<Unit> unitsWithinRadius = new ArrayList<Unit>();
        for (Unit unit : units) {
            float dx = unit.getX() - x;
            float dy = unit.getY() - y;
            float s = unit.getScale();
            if (dx*dx + dy*dy <= radius*radius + s*s) {
                unitsWithinRadius.add(unit);
            }
        }
        return unitsWithinRadius;
    }

    public ArrayList<Unit> getFriendlyUnitsWithinRadius(Player player, float x, float y, float radius) {
        ArrayList<Unit> friendlyUnitsWithinRadius = new ArrayList<Unit>();
        for (Unit unit : getUnitsWithinRadius(x, y, radius)) {
            if (unit.getPlayer().equals(player)) {
                friendlyUnitsWithinRadius.add(unit);
            }
        }
        return friendlyUnitsWithinRadius;
    }

    public ArrayList<Unit> getEnemyUnitsWithinRadius(Player player, float x, float y, float radius) {
        ArrayList<Unit> enemyUnitsWithinRadius = new ArrayList<Unit>();
        for (Unit unit : getUnitsWithinRadius(x, y, radius)) {
            if (!unit.getPlayer().equals(player)) {
                enemyUnitsWithinRadius.add(unit);
            }
        }
        return enemyUnitsWithinRadius;
    }

    public Unit getClosestUnit(float x, float y, ArrayList<Unit> units) {
        if (units.isEmpty()) {
            return null;
        }
        Unit closest = units.get(0);
        float minDist = Float.POSITIVE_INFINITY;
        for (Unit unit : units) {
            float dx = unit.getX() - x;
            float dy = unit.getY() - y;
            float s = unit.getScale();
            float dist = dx*dx + dy*dy - s*s;
            if (dist < minDist) {
                minDist = dist;
                closest = unit;
            }
        }
        return closest;
    }
}

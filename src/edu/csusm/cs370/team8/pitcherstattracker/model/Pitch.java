package edu.csusm.cs370.team8.pitcherstattracker.model;

public class Pitch {
    public enum type {
        Curveball, Fastball, Knuckleball
    }
    public enum Result {
        CalledStrike(0), Ball(1), Foul(2), Hit(3);
        private final int value;
        Result(int input) { this.value = input; }
        public int toInt() { return value; }
    }
    private Result result;

    private int bases;
    private boolean inZone;
    private int  id;
    private double speed;
    private boolean wasSwungAt;


}

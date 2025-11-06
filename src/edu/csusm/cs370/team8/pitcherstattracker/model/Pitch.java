package edu.csusm.cs370.team8.pitcherstattracker.model;

public class Pitch {
    public enum Type {
        Curveball, Fastball, Knuckleball
    }

    public enum Result {
        CalledStrike(0), Ball(1), Foul(2), Hit(3);
        private final int value;

        Result(int input) {
            this.value = input;
        }

        public int toInt() {
            return value;
        }
    }

    private Result result;
    private Type type;

    private int bases;
    private boolean inZone;
    private int id;
    private double speed;
    private boolean wasSwungAt;

    public Pitch(Type ballType, Result pitchResult, boolean inZone, double speed) {
        this.type = ballType;
        this.result = pitchResult;
        this.inZone = inZone;
        this.speed = speed;
    }
    public Result getResult() { return result;}
    public int getBases() { return bases;}
    public void setBases(int bases) { this.bases = bases;}

    public String toString() {
        return "Pitch{" +
                "type=" + type +
                ", result=" + result +
                ", inZone=" + inZone +
                ", speed=" + speed +
                '}';
    }
}

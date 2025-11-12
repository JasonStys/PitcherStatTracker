package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.Random; // Used for static method to create a random pitch for testing.

public class Pitch {
    public static final int MAX_BASES = 4;
    public static final int MIN_BASES = 0;
    public static final double MAX_SPEED = 130.000;
    public static final double MIN_SPEED = 0.000;

    public enum Type {
        Curveball(0), Fastball(1), Knuckleball(2);
        private final int value;

        Type(int input) { this.value = input; }
        public int toInt() { return value; }

        public static Type fromInt(int input) {
            for (Type t : Type.values()) {
                if (t.value == input) { return t; }
            }
            throw new IllegalArgumentException("No Type enum constant with code " + input);
        }
    }

    public enum Result {
        CalledStrike(0), Ball(1), Foul(2), Hit(3);
        private final int value;

        Result(int input) { this.value = input; }
        public int toInt() { return value; }

        public static Result fromInt(int input) {
            for (Result r : Result.values()) {
                if (r.value == input) { return r; }
            }
            throw new IllegalArgumentException("No Result enum constant with code " + input);
        }
    }

    private Result result;
    private Type type;

    private int bases;
    private boolean inZone;
    private int id;
    private double speed;
    private boolean wasSwungAt;

    public Pitch(Type ballType, Result pitchResult, int bases, boolean inZone, double speed, boolean wasSwungAt) {
        this.type = ballType;
        this.result = pitchResult;
        this.bases = bases;
        this.inZone = inZone;
        this.speed = speed;
        this.wasSwungAt = wasSwungAt;
    }
    public Result getResult() { return result; }
    public int getBases() { return bases; }
    public void setBases(int bases) { this.bases = bases; }

    @Override
    public String toString() {
        return "Pitch{" +
                "type=" + type +
                ", result=" + result +
                ", inZone=" + inZone +
                ", speed=" + speed +
                '}';
    }

    // Returns a randomly generated pitch, used for testing only.
    public static Pitch randomPitch() {
        Random random =  new Random();
        Pitch.Type type = Pitch.Type.fromInt(random.nextInt(Pitch.Type.values().length));
        Pitch.Result result = Pitch.Result.fromInt(random.nextInt(Pitch.Result.values().length));
        int bases = (Integer) random.nextInt(MAX_BASES);
        boolean inZone = random.nextBoolean();
        double speed = random.nextDouble(MAX_SPEED);
        boolean wasSwungAt = random.nextBoolean();

        return new Pitch(type, result, bases, inZone, speed, wasSwungAt);
    }
}

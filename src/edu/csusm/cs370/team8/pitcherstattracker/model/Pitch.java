package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.util.Random; // Used for static method to create a random pitch for testing.

public class Pitch {
    public static final int MAX_BASES = 4;
    public static final int MIN_BASES = 0;
    public static final double MAX_SPEED = 130.000;
    public static final double MIN_SPEED = 0.000;

    public enum Type {
        Fastball(0),
        Curveball(1),
        Slider(2),
        Changeup(3);
        private final int value;

        Type(int input) {
            this.value = input;
        }

        public int toInt() { return value; }

        public static Type fromInt(int input) {
            for (Type t : Type.values()) {
                if (t.value == input) {
                    return t;
                }
            }
            throw new IllegalArgumentException("No Type enum constant with code " + input);
        }
    }

    public enum Result {
        //Non-terminal
        Strike(0),
        Ball(1),
        Foul(2),
        //Terminal
        Hit(3),
        BallInPlayOut(4),
        ReachOnError(5),
        Walk(6),
        Strikeout(7),
        HitByPitch(8);
        private final int value;

        Result(int input) {
            this.value = input;
        }

        public int toInt() {
            return value;
        }

        public static Result fromInt(int input) {
            for (Result r : Result.values()) {
                if (r.value == input) {
                    return r;
                }
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

    public Pitch(Type ballType,
                 Result pitchResult,
                 int bases,
                 boolean inZone,
                 double speed,
                 boolean wasSwungAt) {

        this.type = ballType;
        this.result = pitchResult;
        this.bases = bases;
        this.inZone = inZone;
        this.speed = speed;
        this.wasSwungAt = wasSwungAt;
    }
    // Getters and Setters
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public int getBases() { return bases; }
    public void setBases(int bases) { this.bases = bases; }

    public boolean isInZone() { return inZone; }
    public void setInZone(boolean inZone) { this.inZone = inZone; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public boolean wasSwungAt() { return wasSwungAt; }
    public void setWasSwungAt(boolean wasSwungAt) { this.wasSwungAt = wasSwungAt; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

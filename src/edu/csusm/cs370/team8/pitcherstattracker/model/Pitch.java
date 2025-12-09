package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/* A Pitch is a specific, singular throw from the Pitcher and its Result
 * (such as if it was a hit with N number of bases gained by the opposing team).
 */
public class Pitch implements Comparable<Pitch>{
    public static final int MAX_BASES = 4;
    public static final int MIN_BASES = 0;
    public static final double MAX_SPEED = 130.000;
    public static final double MIN_SPEED = 0.000;

    // Enum for the type of throw that was thrown
    public enum Type {
        Fastball(0), Curveball(1), Slider(2), Changeup(3), Knuckleball(4), Slurve(5);
        private final int value;

        Type(int input) { this.value = input; }

        public int toInt() { return value; }

        public static Type fromInt(int input) {
            return switch (input) {
                case 0 -> Fastball;
                case 1 -> Curveball;
                case 2 -> Slider;
                case 3 -> Changeup;
                case 4 -> Knuckleball;
                case 5 -> Slurve;
                default -> throw new IllegalArgumentException("No Type enum constant with code " + input);
            };
        }
    }

    // Enum for the ultimate called result from this pitch
    public enum Result {
        //Non-terminal
        Strike(0), Ball(1), Foul(2),
        //Terminal
        Hit(3), BallInPlayOut(4), ReachOnError(5), Walk(6), Strikeout(7), HitByPitch(8);
        private final int value;

        Result(int input) { this.value = input; }

        public int toInt() { return value; }

        public static Result fromInt(int input) {
            return switch (input) {
                case 0 -> Strike;
                case 1 -> Ball;
                case 2 -> Foul;
                case 3 -> Hit;
                case 4 -> BallInPlayOut;
                case 5 -> ReachOnError;
                case 6 -> Walk;
                case 7 -> Strikeout;
                case 8 -> HitByPitch;
                default -> throw new IllegalArgumentException("No Result enum constant with code " + input);
            };
        }
    }

    // Fields
    private Result result;
    private Type type;
    private int bases;
    private int id = -1; // Negative 1 means it hasn't been assigned an ID yet
    private double speed;
    private boolean wasSwungAt;
    private boolean inZone;

    // Constructor: Needs all fields as argument except ID.
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
        this.wasSwungAt = wasSwungAt;
        // Rounds speed to 3 decimal places
        BigDecimal bd = new BigDecimal(speed).setScale(3, RoundingMode.HALF_EVEN);
        this.speed = bd.doubleValue();
    }

    // Allows pitches to be compared to each other based on ID, for sorting
    @Override
    public int compareTo(Pitch o) {
        return Integer.compare(id, o.id);
    }

    // Basic Getters and Setters. Most of these Setters are not used.
    public Result getResult() { return result; }
    public void setResult(Result result) { this.result = result; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public int getBases() { return bases; }
    public void setBases(int bases) { this.bases = bases; }

    public boolean isInZone() { return inZone; }
    public void setInZone(boolean inZone) { this.inZone = inZone; }

    public int getID() { return id; }
    public void setID(int id) { this.id = id; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public boolean wasSwungAt() { return wasSwungAt; }
    public void setWasSwungAt(boolean wasSwungAt) { this.wasSwungAt = wasSwungAt; }

    // toString() is what JList prints in the panels.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID " + id + ": ");
        sb.append(speed + " MPH, ");
        sb.append(type + ", ");
        sb.append(result + ", ");
        if (inZone) {sb.append("inZone, ");}
        else {sb.append("outZone, ");}
        if (wasSwungAt) {sb.append("SwungAt, ");}
        else {sb.append("NotSwungAt, ");}
        sb.append(bases + " bases");

        return sb.toString();
    }

    public Pitch() {
        // Empty constructor needed for Jackson
    }
}

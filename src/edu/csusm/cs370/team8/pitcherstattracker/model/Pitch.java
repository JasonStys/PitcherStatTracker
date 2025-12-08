package edu.csusm.cs370.team8.pitcherstattracker.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random; // Used for static method to create a random pitch for testing.

public class Pitch implements Comparable<Pitch>{
    public static final int MAX_BASES = 4;
    public static final int MIN_BASES = 0;
    public static final double MAX_SPEED = 130.000;
    public static final double MIN_SPEED = 0.000;

    public enum Type {
        Fastball(0), Curveball(1), Slider(2), Changeup(3), Knuckleball(4), Slurve(5);
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
        //Non-terminal
        Strike(0), Ball(1), Foul(2),
        //Terminal
        Hit(3), BallInPlayOut(4), ReachOnError(5), Walk(6), Strikeout(7), HitByPitch(8);
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
    private int id = -1; // Negative 1 means it hasn't been assigned an ID yet
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
        // Rounds speed to 3 decimal places
        BigDecimal bd = new BigDecimal(speed).setScale(3, RoundingMode.HALF_EVEN);
        this.speed = bd.doubleValue();
        this.wasSwungAt = wasSwungAt;
    }

    // Allows pitches to be compared to each other based on ID
    @Override
    public int compareTo(Pitch o) {
        return Integer.compare(id, o.id);
    }

    // Basic Getters and Setters
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

    //public int getId() { return id; }
    //public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id + ": " + speed + " MPH, " + type + ", " + result + ", ");
        if (inZone) {sb.append("inZone, ");}
        else {sb.append("outZone, ");}
        if (wasSwungAt) {sb.append("SwungAt, ");}
        else {sb.append("NotSwungAt, ");}
        sb.append(bases + " bases");

        return sb.toString();
    }

    // Returns a randomly generated pitch, used for testing only.
    public static Pitch randomPitch() {
        Random random = new Random();

        // 1) Pitch type (uniform among defined types should be adjusted later)
        Pitch.Type type = Pitch.Type.fromInt(random.nextInt(Pitch.Type.values().length));

        // 2) Speed band by type
        double minSpeed;
        double maxSpeed;
        switch (type) {
            case Fastball -> { minSpeed = 88.0; maxSpeed = 100.0; }
            case Slider   -> { minSpeed = 80.0; maxSpeed = 90.0; }
            case Curveball-> { minSpeed = 72.0; maxSpeed = 82.0; }
            case Changeup -> { minSpeed = 75.0; maxSpeed = 86.0; }
            case Knuckleball -> { minSpeed = 60.0; maxSpeed = 75.0; }
            case Slurve -> { minSpeed = 76.0; maxSpeed = 88.0; }
            default       -> { minSpeed = MIN_SPEED; maxSpeed = MAX_SPEED; }
        }

        double speed = minSpeed + random.nextDouble() * (maxSpeed - minSpeed);
        // make sure in global bounds just in case
        if (speed < MIN_SPEED) speed = MIN_SPEED;
        if (speed > MAX_SPEED) speed = MAX_SPEED;

        // Round to 3 decimal places
        BigDecimal bd = new BigDecimal(speed).setScale(3, RoundingMode.HALF_EVEN);
        speed = bd.doubleValue();

        // 3) Is it in the zone?
        boolean inZone = random.nextDouble() < 0.55;

        // 4) Does the batter swing? More likely on strikes.
        boolean wasSwungAt = random.nextDouble() < (inZone ? 0.6 : 0.25);

        Pitch.Result result;
        int bases = 0; // default: no bases unless it's a hit

        if (!wasSwungAt) {
            // Batter takes: it's either a called strike or a ball
            result = inZone ? Pitch.Result.Strike : Pitch.Result.Ball;
            // 2% chance a ball is a walk.
            if (result == Pitch.Result.Ball && random.nextDouble() < 0.02) {
                result  = Pitch.Result.Walk;
            }
        } else {
            // Batter swings: simple probability breakdown
            double r = random.nextDouble();

            if (r < 0.40) {
                result = Pitch.Result.Foul;              // ~40% of swings
            } else if (r < 0.70) {
                result = Pitch.Result.BallInPlayOut;     // ~30%
            } else if (r < 0.85) {
                result = Pitch.Result.Hit;               // ~15%
                // Bases for hits: mostly singles
                double h = random.nextDouble();
                if (h < 0.75)       bases = 1; // single
                else if (h < 0.93)  bases = 2; // double
                else if (h < 0.95)  bases = 3; // triple
                else                bases = 4; // HR
            } else if (r < 0.90) {
                result = Pitch.Result.ReachOnError;      // ~5%
            } else {
                result = Pitch.Result.Strikeout;         // ~10% (swing-and-miss K)
            }
        }

        return new Pitch(type, result, bases, inZone, speed, wasSwungAt);
    }

}

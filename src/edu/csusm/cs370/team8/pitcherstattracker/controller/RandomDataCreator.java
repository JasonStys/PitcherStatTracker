package edu.csusm.cs370.team8.pitcherstattracker.controller;

import edu.csusm.cs370.team8.pitcherstattracker.model.Pitch;
import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.model.Session;
import edu.csusm.cs370.team8.pitcherstattracker.model.UserAccount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

// Methods for generating random data. Used for testing.
// The final release program shouldn't run these unless in DEMO_MODE. Instead it loads from .json data
public class RandomDataCreator {

    // =============== Pitch ===============

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
            default       -> { minSpeed = Pitch.MIN_SPEED; maxSpeed = Pitch.MAX_SPEED; }
        }

        double speed = minSpeed + random.nextDouble() * (maxSpeed - minSpeed);
        // make sure in global bounds just in case
        if (speed < Pitch.MIN_SPEED) speed = Pitch.MIN_SPEED;
        if (speed > Pitch.MAX_SPEED) speed = Pitch.MAX_SPEED;

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

    // =============== Session ===============

    private static LocalDate randomDateLastFiveYears() {
        int currentYear = LocalDate.now().getYear();

        // Pick a random year in [currentYear - 4, currentYear]
        int year = ThreadLocalRandom.current().nextInt(currentYear - 4, currentYear + 1);

        // Rough MLB regular season window: April 1 – October 1
        LocalDate seasonStart = LocalDate.of(year, 4, 1);
        LocalDate seasonEnd   = LocalDate.of(year, 10, 1);

        long startEpochDay = seasonStart.toEpochDay();
        long endEpochDay   = seasonEnd.toEpochDay();

        // Random day in [startEpochDay, endEpochDay]
        long randomEpochDay = ThreadLocalRandom.current()
                .nextLong(startEpochDay, endEpochDay + 1);

        return LocalDate.ofEpochDay(randomEpochDay);
    }

    public static Session createRandomSessionData(int numPitches) {
        Session exampleSession = new Session();
        exampleSession.setTimestamp(randomDateLastFiveYears());
        for (int i = 0; i < numPitches; i++) {
            exampleSession.addPitch(RandomDataCreator.randomPitch());
        }
        return exampleSession;
    }

    // =============== Pitcher ===============

    // For random generation of Pitcher names
    private static final List<String> exampleNames = List.of(new String[]{
            "Unfathomable Ulysses",
            "Standout Savanna",
            "Exceptional Elliot",
            "Suitable Sally",
            "Decent Darren",
            "Average Alison",
            "Okay O'reilly",
            "Sketchy Steven",
            "Terrible Tony",
            "Blind Bartholomew",
            "Placeholder Platon"
    });

    public static String getRandomName() {
        Random random = new Random();
        return exampleNames.get(random.nextInt(exampleNames.size()));
    }
    public static Pitcher createRandomPitcher(int numSessions) {
        Random random =  new Random();
        int numPitches;
        Pitcher pitcher = new Pitcher();
        for (int i = 0; i < numSessions; i++) {
            numPitches = random.nextInt(15, 91);
            pitcher.addSession(RandomDataCreator.createRandomSessionData(numPitches));
        }
        pitcher.setName(getRandomName());
        return pitcher;
    }

    public static String getNextName(int i) {
        return exampleNames.get(i);
    }

    // =============== UserAccount ===============

    public static UserAccount generateCoach() {
        UserAccount account = new UserAccount("username", "password");
        Random random = new Random();
        // Get 10 random pitchers, unnamed, in a temporary list
        List<Pitcher> exPitchers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int numSessions = random.nextInt(15, 91);
            Pitcher examplePitcher = RandomDataCreator.createRandomPitcher(numSessions);
            exPitchers.add(examplePitcher);
        }
        // Sort them compared to their WHIP stat (roughly, the lower it is the better pitcher they are)
        exPitchers.sort(Pitcher.WhipComparator);
        for (int i = 0; i < exPitchers.size(); i++) {
            Pitcher examplePitcher = exPitchers.get(i);
            // Pitcher.getNextName() will output string names in order of best to worst
            examplePitcher.setName(RandomDataCreator.getNextName(i));
            account.addPitcher(examplePitcher); // Add them to the account we are going to return
        }

        /* Create an empty pitcher at the end.
        Pitcher emptyPitcher = new Pitcher();
        emptyPitcher.setName("Empty Eriksson");
        account.addPitcher(emptyPitcher);
         */
        return account;
    }
}

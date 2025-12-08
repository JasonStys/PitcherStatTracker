package edu.csusm.cs370.team8.pitcherstattracker.controller;

import edu.csusm.cs370.team8.pitcherstattracker.model.Pitcher;
import edu.csusm.cs370.team8.pitcherstattracker.view.TableWidget;

import java.util.*;

/*
 Controller that turns StatCalculator results into a TableWidget.
  - Depends on StatCalculator (stats engine).
  - Depends on TableWidget (view helper).
  - Panels can call this to get ready-to-use tables.
*/
public class PitcherStatsTable {

    /*
    Columns that can appear in a pitcher stats table.
    The panel can choose any subset and order it wants.
    */
    public enum Column {
        LABEL,
        IP,
        BF,
        PITCHES,
        H,
        SINGLES,
        DOUBLES,
        TRIPLES,
        HR,
        BB,
        K,
        HBP,
        AVG,
        OBP,
        SLG,
        OPS,
        WHIP
    }

    private final StatCalculator statCalculator;

    // Construct directly from a Pitcher (common case)
    public PitcherStatsTable(Pitcher pitcher) {
        this.statCalculator = new StatCalculator(pitcher);
    }

    // Construct with multiple pitchers
    public PitcherStatsTable(List<Pitcher> pitchers) {
        this.statCalculator = new StatCalculator(pitchers);
    }

    // Or pass in an existing StatCalculator if you want
    public PitcherStatsTable(StatCalculator statCalculator) {
        this.statCalculator = statCalculator;
    }

    //Simple default: career totals with a common set of columns.
    public TableWidget buildDefaultTable() {
        List<Column> cols = List.of(
                Column.LABEL,
                Column.IP,
                Column.BF,
                Column.PITCHES,
                Column.H,
                Column.BB,
                Column.K,
                Column.AVG,
                Column.OBP,
                Column.SLG,
                Column.WHIP
        );
        return buildTable(StatCalculator.Grouping.BY_YEAR, cols);
    }

    public TableWidget buildMultiTable() {
        List<Column> cols = List.of(
                Column.LABEL,
                Column.IP,
                Column.BF,
                Column.PITCHES,
                Column.H,
                Column.BB,
                Column.K,
                Column.AVG,
                Column.OBP,
                Column.SLG,
                Column.WHIP
        );
        return buildTable(StatCalculator.Grouping.MULTI_PITCHER, cols);
    }

    /*
     Generic table builder.

     @param grouping how to group stats: CAREER, BY_YEAR, BY_MONTH
     @param columns  which columns to show, in order
     @return         TableWidget ready to be added to a panel
     */
    public TableWidget buildTable(StatCalculator.Grouping grouping,
                                  List<Column> columns) {

        // normal buckets for requested grouping
        Map<String, StatCalculator.PitchingStats> buckets =
                statCalculator.aggregate(grouping);

        // optional career entry
        Map.Entry<String, StatCalculator.PitchingStats> careerEntry = null;
        if ((grouping != StatCalculator.Grouping.CAREER) &&
                (grouping != StatCalculator.Grouping.MULTI_PITCHER)) {
            Map<String, StatCalculator.PitchingStats> careerMap =
                    statCalculator.aggregate(StatCalculator.Grouping.CAREER);
            if (!careerMap.isEmpty()) {
                careerEntry = careerMap.entrySet().iterator().next();
            }
        }

        // column names
        String[] columnNames = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            columnNames[i] = headerFor(columns.get(i), grouping);
        }

        // sorted keys for normal rows
        List<String> keys = new ArrayList<>(buckets.keySet());
        // Messy override if we're dealing with pitchers already sorted.
        if (grouping != StatCalculator.Grouping.MULTI_PITCHER) {
            Collections.sort(keys);
        }

        int rowCount = keys.size() + (careerEntry != null ? 1 : 0);
        Object[][] rowData = new Object[rowCount][columns.size()];

        int row = 0;

        // normal rows
        for (String key : keys) {
            StatCalculator.PitchingStats stats = buckets.get(key);
            for (int col = 0; col < columns.size(); col++) {
                rowData[row][col] = valueForColumn(columns.get(col), key, stats);
            }
            row++;
        }

        // career row at bottom, if requested
        if (careerEntry != null) {
            String label = careerEntry.getKey();           // e.g., "Career"
            StatCalculator.PitchingStats stats = careerEntry.getValue();
            for (int col = 0; col < columns.size(); col++) {
                rowData[row][col] = valueForColumn(columns.get(col), label, stats);
            }
        }

        TableWidget table = new TableWidget(columnNames, rowData);
        for (int i = 0; i < columnNames.length; i++) {
            table.setColumnWidth(i, 60);
        }
        if (grouping == StatCalculator.Grouping.MULTI_PITCHER) {
            table.setColumnWidth(0, 125);
        }
        return table;
    }


    //helpers
    private String headerFor(Column c, StatCalculator.Grouping grouping) {
        return switch (c) {
            case LABEL   -> switch (grouping) {
                case CAREER  -> "Label";
                case BY_YEAR -> "Year";
                case BY_MONTH-> "Month";
                case BY_DAY -> "Day";
                case MULTI_PITCHER ->  "Pitcher";
            };
            case IP      -> "IP";
            case BF      -> "BF";
            case PITCHES -> "Pitches";
            case H       -> "H";
            case SINGLES -> "1B";
            case DOUBLES -> "2B";
            case TRIPLES -> "3B";
            case HR      -> "HR";
            case BB      -> "BB";
            case K       -> "K";
            case HBP     -> "HBP";
            case AVG     -> "AVG";
            case OBP     -> "OBP";
            case SLG     -> "SLG";
            case OPS     -> "OPS";
            case WHIP    -> "WHIP";
        };
    }

    private Object valueForColumn(Column c,
                                  String bucketLabel,
                                  StatCalculator.PitchingStats stats) {
        return switch (c) {
            case LABEL   -> bucketLabel;
            case IP      -> stats.getInningsPitchedString();
            case BF      -> stats.getTotalBattersFaced();
            case PITCHES -> stats.getTotalPitches();
            case H       -> stats.getHits();
            case SINGLES -> stats.getTotalSingles();
            case DOUBLES -> stats.getTotalDoubles();
            case TRIPLES -> stats.getTotalTriples();
            case HR      -> stats.getTotalHomeRuns();
            case BB      -> stats.getTotalWalks();
            case K       -> stats.getTotalStrikeouts();
            case HBP     -> stats.getTotalHitByPitch();
            case AVG     -> formatRate(stats.getAvg());
            case OBP     -> formatRate(stats.getObp());
            case SLG     -> formatRate(stats.getSlg());
            case OPS     -> formatRate(stats.getOps());
            case WHIP    -> formatWhip(stats.getWhip());
        };
    }

    private String formatRate(double v) {
        return Double.isNaN(v) ? "---" : String.format("%.3f", v);
    }

    private String formatWhip(double v) {
        return Double.isNaN(v) ? "---" : String.format("%.2f", v);
    }



}

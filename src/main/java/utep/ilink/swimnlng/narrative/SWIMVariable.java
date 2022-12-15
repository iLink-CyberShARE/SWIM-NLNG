package utep.ilink.swimnlng.narrative;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SWIMVariable {
    String scenarioID;
    String language;

    String modelID;
    HashMap <String, String> varInfoEnUS;
    HashMap <String, String> varInfoEsMX;

    String varName;
    int[] times;
    double[] values;

    List<HashMap <String, String>> benchmarksEnUS;
    List<HashMap <String, String>> benchmarksEsMX;

    public SWIMVariable(String s, String l, String m, HashMap<String, String> viEn, HashMap<String, String> viEs, String vn, int[] t, double[] vs, List<HashMap <String, String>> bEn, List<HashMap <String, String>> bEs){
        scenarioID = s;
        language = l;
        modelID = m;
        varInfoEnUS = viEn;
        varInfoEsMX = viEs;
        varName = vn;
        times = t;
        values = vs;
        benchmarksEnUS = bEn;
        benchmarksEsMX = bEs;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Scenario ID: " + scenarioID);
        builder.append("\nLanguage: " + language);
        builder.append("\nModel ID: " + modelID);
        builder.append("\nVariable Info EN-US: " + varInfoEnUS.toString());
        builder.append("\nVariable Info ES-MX: " + varInfoEsMX.toString());
        builder.append("\nVariable Name: " + varName);
        builder.append("\nTimes: " + Arrays.toString(times));
        builder.append("\nValues: " + Arrays.toString(values));
        builder.append("\nBenchmarks EN-US: " + benchmarksEnUS.toString());
        builder.append("\nBenchmarks ES-MX: " + benchmarksEsMX.toString());
        return builder.toString();
    }
}

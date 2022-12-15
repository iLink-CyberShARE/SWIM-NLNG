package utep.ilink.swimnlng.narrative;

import java.nio.file.Paths;
import java.util.*;

public class NarrativeBuilder {
    
    public static String buildNarrative (HashMap <String, List<String>> elements){
        return "Hello";
    }

    public static void main(String [] args){
        String path = Paths.get(".").toAbsolutePath().normalize().toString();

        List <String> filenames = new ArrayList<>();
        filenames.add(path + "/test-cases/EBStorageV2_af-wbenchmarks.json");
        filenames.add(path + "/test-cases/EBStorageV2_af.json");
        filenames.add(path + "/test-cases/HBCumLossV2.json");
        filenames.add(path + "/test-cases/HBRechargeCumV2.json");
        filenames.add(path + "/test-cases//MIDACumLossV2.json");
        filenames.add(path +"/test-cases/MIDARechargeV2.json");
        List<SWIMVariable> swimVariables = buildVariables(filenames);
        List<String> descriptions = findObservations(swimVariables);
        System.out.println("descriptions: "+ Arrays.toString(descriptions.toArray()));
    }

    public static List<SWIMVariable> buildVariables(List <String> filenames){
        List<SWIMVariable> variables = new ArrayList<>();
        
        for(String filename: filenames){
            // System.out.println("*** Narrative.buildVariables\nFilename: " + filename);
            SWIMJSONParser swimParser = new SWIMJSONParser(filename); //We are using filename as input, but we can also pass a JSONObject as input to the constructor.
            SWIMVariable var = swimParser.parseJSON();
            // System.out.println("*** Narrative.buildVariables\nSWIMVariable: " + var.toString());
            variables.add(var);
        }
        
        return variables;
    }

    public static List<String> findObservations(List<SWIMVariable> vars){
        Analysis analysis = new Analysis();
        List<String> descriptions = analysis.analyzeVars(vars);
        return descriptions;
    }

    

}

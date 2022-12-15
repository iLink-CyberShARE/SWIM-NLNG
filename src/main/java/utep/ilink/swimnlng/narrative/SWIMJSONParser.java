package utep.ilink.swimnlng.narrative;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/* Class takes as input either a filename of a JSON file or a JSONObject. This class is called by Narrative class. */
public class SWIMJSONParser {
  String scenarioID;
  String language;
  String modelID;
  String varName;
  int[] times;
  double[] values;
  HashMap<String, String> varInfoEnUS;
  HashMap<String, String> varInfoEsMX;
  List<HashMap <String, String>> benchmarksEnUS;
  List<HashMap <String, String>> benchmarksEsMX;
  JSONObject swimJSONObject;

  public SWIMJSONParser(String filename){
    this.scenarioID = "";
    this.language = "";
    this.modelID = "";
    this.varName = "";
    this.times = new int[0];
    this.values = new double[0];
    this.varInfoEnUS = new HashMap<>();
    this.varInfoEsMX = new HashMap<>();
    this.benchmarksEnUS = new ArrayList<HashMap<String,String>>();
    this.benchmarksEsMX = new ArrayList<HashMap<String,String>>();

    JSONParser parser = new JSONParser();
    try{
      Object obj = parser.parse(new FileReader(filename));
      swimJSONObject = (JSONObject)obj;
    } catch(Exception e){
      e.printStackTrace();
    }
  }

  public SWIMJSONParser(JSONObject jsonObject){
    this.scenarioID = "";
    this.language = "";
    this.modelID = "";
    this.varName = "";
    this.times = new int[0];
    this.values = new double[0];
    this.varInfoEnUS = new HashMap<>();
    this.varInfoEsMX = new HashMap<>();
    this.benchmarksEnUS = new ArrayList<HashMap<String,String>>();
    this.benchmarksEsMX = new ArrayList<HashMap<String,String>>();
    this.swimJSONObject = jsonObject;
  }

  public SWIMVariable parseJSON(){

      try {
        
        this.scenarioID = (String) this.swimJSONObject.get("scenarioid");
        // System.out.println("scenarioID: " + scenarioID);
        language = (String)this.swimJSONObject.get("language");
        // System.out.println("language: " + language);
        JSONObject output = (JSONObject) this.swimJSONObject.get("output");
        modelID = (String) output.get("modelID");
        // System.out.println("modelID: " + modelID);
        JSONArray varInfo = (JSONArray) output.get("varinfo");
        varName = (String)output.get("varName");
        // System.out.println("varName: " + varName);
        JSONArray varValue = (JSONArray) output.get("varValue");
        times = new int[varValue.size()];
        // System.out.println("times: " + Arrays.toString(times));
        values = new double[varValue.size()];
        // System.out.println("values: " + Arrays.toString(values));
        JSONArray varBenchmarks = (JSONArray) output.get("varBenchMarks"); 
        
        for(int i = 0; i < varInfo.size(); i++){
          JSONObject entry = (JSONObject)varInfo.get(i);
          String entryLang = (String)entry.get("lang");
          HashMap<String, String> map = new HashMap<>();
          map.put("varLabel", (String)entry.get("varLabel"));
          map.put("varCategory", (String)entry.get("varCategory"));
          map.put("lang", (String)entry.get("lang"));
          map.put("varDescription", (String)entry.get("varDescription"));
          map.put("varUnit", (String)entry.get("varUnit"));
          if (entryLang.equals("en-us")) varInfoEnUS = map;
          else varInfoEsMX = map;
         }

        for(int i = 0; i < varValue.size(); i++){
         JSONObject entry = (JSONObject)varValue.get(i);
         Long tEntry = (Long)entry.get("t");
         String valString = entry.get("value").toString();
         Double valEntry = Double.parseDouble(valString);
         times[i] = tEntry.intValue();
         values[i] = valEntry;
        }

        if (varBenchmarks != null){
          for(int i = 0; i < varBenchmarks.size(); i++){
            JSONObject entry = (JSONObject)varBenchmarks.get(i);
            String entryLang = (String)entry.get("benchmarkLang");
            HashMap<String, String> benchmark = new HashMap<>();
            benchmark.put("benchmarkDescription", (String)entry.get("benchmarkDescription"));
            benchmark.put("benchmarkLabel", (String)entry.get("benchmarkLabel"));
            benchmark.put("benchmarkValue", entry.get("benchmarkValue").toString());
            benchmark.put("benchmarkSource", (String)entry.get("benchmarkSource"));
            benchmark.put("benchmarkLang", (String)entry.get("benchmarkLang"));
            if (entryLang.equals("en-us")) benchmarksEnUS.add(benchmark);
            else benchmarksEsMX.add(benchmark);
          }
        }
        

        SWIMVariable swimVar = new SWIMVariable(scenarioID, language, modelID, varInfoEnUS, varInfoEsMX, varName, times, values, benchmarksEnUS,benchmarksEsMX);
        return swimVar;
      } catch(Exception e) {
         e.printStackTrace();
         return null;
      }
  }

}

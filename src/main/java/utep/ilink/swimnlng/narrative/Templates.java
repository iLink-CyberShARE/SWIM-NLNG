package utep.ilink.swimnlng.narrative;
import java.util.*;

public class Templates {
    
    private HashMap <HashMap<String, Integer>, String []> templates;

    public Templates(){
        templates = new HashMap<HashMap<String, Integer>, String []>();
        buildTemplates();
    }

    private void buildTemplates (){
        // System.out.println("Building templates");
        buildSharpestDecreaseTemplate();
        buildBigStressScenarioTemplate();
        buildUrbanPriceTemplate();
        buildAverageInflowsHistoricalTemplate();
        buildPeakVolumesTemplate();
        buildMinVolumesTemplate();
        buildYearlyComparisonInflowsHistoricalTemplate();
        buildYearFrameTemplate();
        //TODO call build templates that are giving issues
        // System.out.println("End: Building templates");
    }
    
    public List <String> buildNarratives (String [] variables, String [] labels){
        // System.out.println("Building narratives");
        try{
            HashMap <String, Integer> pattern = buildPattern(variables, labels);
            HashMap<String, String>vars = buildVariables(variables, labels);
            return findPattern (pattern, vars);
        } catch(Exception e){
            return null;
        }
    }

    public HashMap<String, Integer> buildPattern (String [] variables, String [] labels){
        // System.out.println("Building pattern");
        // System.out.println("\tvariables: " + Arrays.toString(variables));
        // System.out.println("\tlabels: " + Arrays.toString(labels));
        HashMap <String, Integer> map = new HashMap<>();
        for (String variable: variables){
            if(map.containsKey(variable)){
                int val = map.get(variable);
                val++;
                map.put(variable, val);
            } else map.put(variable, 1);
        }
        // System.out.println("End: Building pattern");
        return map;
    }

    public HashMap<String, String> buildVariables (String [] variables, String [] labels){
        // System.out.println("Building variables");
        HashMap <String, String> map = new HashMap<>();
        for (int i = 0; i < variables.length; i++){

            map.put(variables[i], labels[i]);
        }
        // System.out.println("End: Building variables");
        return map;
    }

    public List<String> findPattern (HashMap <String, Integer> pattern, HashMap <String, String> variables){
        // System.out.println("Finding Patterns");
        List<HashMap <String, Integer>> list = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();
        for(HashMap<String, Integer> templatePattern : templates.keySet()){
            //figure out if pattern given can be used in the templates
            if (matchPattern(pattern, templatePattern)){
                // System.out.println("Pattern found, calling build description");
                list.add(templatePattern);
                String desc = buildDescription(templates.get(templatePattern), variables);
                descriptions.add(desc);
                
            } 
        }
        // System.out.println("End: Finding Patterns");
        if (descriptions.size() == 0) descriptions.add("");
        return descriptions;
    }

    //first will be the given pattern, second will be the template pattern
    private boolean matchPattern (Map <String, Integer> first, Map<String, Integer> second){
        // System.out.println("Matching patterns");
        // System.out.println("\tfirst: " + first.toString());
        // System.out.println("\tsecond: " + second.toString());
        if(first.size() < second.size()) return false;
        for(String key: second.keySet()){
            if(first.get(key) == null || first.get(key) < second.get(key)) return false;
        }
        // System.out.println("Pattern found! End of matching patterns");
        return true;
    }

    public String buildDescription (String [] pattern, HashMap <String, String> variables){
        // System.out.println("Building description");
        // System.out.println("\tpattern: " + Arrays.toString(pattern));
        // System.out.println("\tvariables: " + variables.toString());
        String description = "";
            for (String block : pattern){
                if (block.contains("~")){
                    if (variables.containsKey(block)){
                        description = description.concat(variables.get(block));
                    }
                } else description = description.concat(block);
            }
        return description;
    }

    private void buildSharpestDecreaseTemplate (){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~element_label", 1);
            put("~observational_element", 1);
            put("~preposition-trend", 1);
        }};
        String [] description = new String [] {"~element_label", " shows a significant ", "~observational_element", " ", "~preposition-trend"};
        templates.put(pattern, description);
    }

    private void buildBigStressScenarioTemplate (){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~element_label", 1);
            put("~element_description", 1);
            put("~explanation", 1);
        }};
        String [] description = new String [] {"The selected water supply of ", "~element_label", " Scenario appends ", "~element_description ", "~explanation."};
        templates.put(pattern, description);
    }

    private void buildUrbanPriceTemplate (){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~region", 1);
            put("~adjective_behavior", 1);
            put("~adjective_trend", 1);
            put("~constant_year", 1);
            put("~constant_value", 1);
            put("~minValue", 1);
            put("~minYear", 1);
        }};
        String [] description = new String [] {"Urban Prices in ", "~region", " follow a ", "~adjective_behavior", " ", "~adjective_trend", " trend that peaks and remains constant as of ", "~constant_year", " at ", "~constant_value", " USD/AF, lowest value is in ", "~minValue ", "USD/AF in ", "~minYear"};
        templates.put(pattern, description);
    }

    private void buildAverageInflowsHistoricalTemplate (){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~adjective_trend", 1);
            put("~adjective_behavior", 1);
            put("~average", 1);
            put("~element_unit", 1);
        }};
        String [] description = new String [] {"Water Supply outcome causes a ", "~adjective_trend", " ", "~adjective_behavior", " in average inflows to Elephant Butte in comparison to the historical period (1995-2015). Historical averages were 582.86 KAF/yr and the selected scenario results in ", "~average", " ", "~element_unit", " average annual inflow."};
        templates.put(pattern, description);
    }

    private void buildYearlyComparisonInflowsHistoricalTemplate (){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~var_description", 1);
            put("~adjective_behavior", 1);
            put("~benchmark_label", 1);
            put("~benchmark_value", 1);
            put("~var_unit", 1);
            put("~year_frame", 1);
        }};
        String [] description = new String [] {"~var_description", " appears ", "~adjective_behavior", " the ", "~benchmark_label", "  of ", "~benchmark_value", " ", "~var_unit", " between the years ", "~year_frame", "."};
        templates.put(pattern, description);
    }

    private void buildPeakVolumesTemplate(){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~element_label", 1);
            put("~year", 1);
            put("~max", 1);
        }};
        String [] description = new String [] {"~element_label", " shows ", "~max", " volumes in ", "~year", "."};
        templates.put(pattern, description);
    }

    private void buildMinVolumesTemplate(){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~element_label", 1);
            put("~year", 1);
            put("~min", 1);
        }};
        String [] description = new String [] {"~element_label", " shows ", "~min", " volumes in ", "~year", "."};
        templates.put(pattern, description);
    }

    private void buildYearFrameTemplate(){
        HashMap <String, Integer> pattern = new HashMap<String, Integer>() {{
            put("~year_frame", 1);
            put("~start_year", 1);
            put("~end_year", 1);
        }};
        String [] description = new String [] {"~year_frame", "~start_year", "-", "~end_year"};
        templates.put(pattern, description);
    }

}

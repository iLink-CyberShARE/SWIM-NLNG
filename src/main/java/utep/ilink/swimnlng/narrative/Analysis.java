package utep.ilink.swimnlng.narrative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;


public class Analysis {
    Templates templates;

    public Analysis(){
        templates = new Templates();
    }
    public static void main(String[] args) {
        
    }

    public List<String> analyzeVars(List<SWIMVariable> vars){
        if (vars == null) return null;
        HashSet<String> descriptionSet = new HashSet<>();

        for (SWIMVariable var: vars){
            descriptionSet.addAll(belowOrAboveBenchmark(var));
            descriptionSet.addAll(findMax(var));
            descriptionSet.addAll(findMin(var));
        }
        return new ArrayList<String>(descriptionSet);
        
    }

    public List<String> crossOver(SWIMVariable var){

        //TODO fix and add code
        return null;
    }

    public List<String> belowOrAboveBenchmark(SWIMVariable var){
        List<HashMap <String, String>> benchmarksEnUS = var.benchmarksEnUS;
        int[] times = var.times;
        double[] values = var.values;
        String elementLabel = var.varInfoEnUS.get("varLabel");

        List<String> descriptions = new ArrayList<>();
        if (benchmarksEnUS != null && benchmarksEnUS.size()>0){//I have a benchmark
            for(HashMap<String, String> benchmark: benchmarksEnUS){
                Double benchmarkValue = Double.valueOf(benchmark.get("benchmarkValue"));
                String benchmarkLabel = benchmark.get("benchmarkLabel");
                List<Integer> yearsAboveBenchmark = belowOrAboveThreshold(times, values, benchmarkValue, true, 3);
                List<Integer> yearsBelowBenchmark = belowOrAboveThreshold(times, values, benchmarkValue, false, 3);
                //call description generation
                String [] variables = new String [] {"~var_description", "~adjective_behavior", "~benchmark_label", "~benchmark_value", "~var_unit", "~year_frame"};

                if (yearsAboveBenchmark.size()>0){
                    String yearFrame = getYearDescription(yearsAboveBenchmark);
                    String [] labels = new String [] {var.varInfoEnUS.get("varDescription"), "above", benchmarkLabel, benchmarkValue.toString(), var.varInfoEnUS.get("varUnit"), yearFrame};
                    descriptions = templates.buildNarratives(variables, labels);

                } else if (yearsBelowBenchmark.size()>0){
                    String yearFrame = getYearDescription(yearsBelowBenchmark);
                    String [] labels = new String [] {var.varInfoEnUS.get("varDescription"), "below", benchmarkLabel, benchmarkValue.toString(), var.varInfoEnUS.get("varUnit"), yearFrame};
                    descriptions = templates.buildNarratives(variables, labels);
                }
            }
        }

        return descriptions;
    }

    public String getYearDescription(List<Integer> years){
        if(years.size()<2) return "";
        String retval = "";

        for(int i = 0 ; i < years.size(); i=i+2){
            if (retval.length() != 0) retval = retval.concat(", ");
            if ((years.size()-i) < 3) retval = retval.concat("and ");
            String desc = templates.buildNarratives(new String [] {"~year_frame", "~start_year", "~end_year"}, new String [] {"", years.get(i).toString(), years.get(i+1).toString()}).get(0);
            retval = retval.concat(desc);
            
        }
        return retval;
    }

    public List<Integer> belowOrAboveThreshold(int[] times, double[] values, double threshold, boolean order){
        List<Integer> years = new ArrayList<>();
        boolean recording = false;
        int initialYear = 0;
        for(int i = 0; i < values.length; i++){
            double curr = values[i];
            if(order){//above threshold
                if (curr > threshold){
                    if(!recording){
                        recording = true;
                        years.add(times[i]);
                    }
                } else{
                    if(recording){
                        recording = false;
                        years.add(times[i-1]);
                    }
                }
            }
            else{
                if (curr < threshold){
                    if(!recording){
                        recording = true;
                        years.add(times[i]);
                    }
                } else{
                    if(recording){
                        recording = false;
                        years.add(times[i-1]);
                    }
                }
            }
        }
        // System.out.println((order ? "Above" : "Below") + "Threshold years: " + Arrays.toString(years.toArray()));
        return years;
    }

    public List<Integer> belowOrAboveThreshold(int[] times, double[] values, double threshold, boolean order, int window){
        List<Integer> years = new ArrayList<>();
        boolean recording = false;
        int initialYear = 0;
        for(int i = 0; i < values.length; i++){
            double curr = values[i];
            if(order){//above threshold
                if (curr > threshold){
                    if(initialYear == 0){
                        initialYear = times [i];
                        //years.add(times[i]);
                    }
                } else{
                    if(initialYear != 0){
                        if ((times[i-1]-initialYear) > window){
                            years.add(initialYear);
                            years.add(times[i-1]);
                        } 
                        initialYear = 0;
                    }
                }
            }
            else{
                if (curr < threshold){
                    if(initialYear == 0){
                        initialYear = times [i];
                        //years.add(times[i]);
                    }
                } else{
                    if(initialYear != 0){
                        if ((times[i-1]-initialYear) > window){
                            years.add(initialYear);
                            years.add(times[i-1]);
                        } 
                        initialYear = 0;
                    }
                }
            }
        }
        // System.out.println((order ? "Above" : "Below") + "Threshold2 years: " + Arrays.toString(years.toArray()));
        return years;
    }

    public List<String> findMax(SWIMVariable var){
        // System.out.println("Analysis.findMax called. SWIMVariable: " + var.toString());
        EvictingQueue queue = new EvictingQueue(3);
        for (int i = 0; i<var.values.length; i++){
            Pair<Double, Integer> pair= new Pair<>(var.values[i], i);
            queue.add(pair);
        }

        String [] variables = new String [] {"~element_label", "~year", "~max"};
        int year = var.times[queue.peek().getValue()];
        String [] labels = new String [] {var.varInfoEnUS.get("varLabel"), Integer.toString(year), "peak"};
        List<String> descriptions = templates.buildNarratives(variables, labels);

        return descriptions;
    }

    public List<String> findMin(SWIMVariable var){
        // System.out.println("Analysis.findMax called. SWIMVariable: " + var.toString());
        EvictingQueue queue = new EvictingQueue(3, false);
        for (int i = 0; i<var.values.length; i++){
            Pair<Double, Integer> pair= new Pair<>(var.values[i], i);
            queue.add(pair);
        }

        String [] variables = new String [] {"~element_label", "~year", "~min"};
        String elementLabel = var.varInfoEnUS.toString();
        
        int year = var.times[queue.peek().getValue()];
        String [] labels = new String [] {var.varInfoEnUS.get("varLabel"), Integer.toString(year), "minimum"};
        List<String> descriptions = templates.buildNarratives(variables, labels);
        // System.out.println("Inside Analysis.findMin. Descriptions: " + Arrays.toString(descriptions.toArray()));
        return descriptions;
    }

    //TODO Extend Queue Interface
    static class EvictingQueue{
        List <Double> values = new ArrayList<>();
        List <Integer> indexes = new ArrayList<>();
        boolean order = true;//if 0 => increasing (keep Min), if 1 => decreasing (keep Max)
        int size;

        public EvictingQueue(int size){
            this.size = size;
        }

        public EvictingQueue(int size, boolean order){
            this.size = size;
            this.order = order;
        }

        public void add(Pair<Double, Integer> pair){
            //check place of new pair
            int index = 0;
            if(order){
                for (int i = 0; i< values.size(); i++){
                    double curr = values.get(i);
                    double val = pair.getKey();
                    index = i;
                    if (val > curr){
                        break;
                    }
                }
                if(values.size() > 0 && values.get(index) > pair.getKey()) index++;
            } else{
                for (int i = 0; i< values.size(); i++){
                    double curr = values.get(i);
                    double val = pair.getKey();
                    index = i;
                    if (val < curr){
                        break;
                    }
                }
                if(values.size() > 0 && values.get(index) < pair.getKey()) index++;
            }

            //add value, add index
            values.add(index, pair.getKey());
            indexes.add(index, pair.getValue());

            //remove last pair in both lists
            while(values.size() > this.size){
                values.remove(values.size()-1);
            }

            while(indexes.size() > this.size){
                indexes.remove(indexes.size()-1);
            }
            
        }

        public Pair<Double, Integer> poll(){
            //store first item in both lists as Pair
            if (values.size() == 0) return null;
            Pair<Double, Integer> pair = new Pair<Double,Integer>(values.get(0), indexes.get(0));

            //remove first item in both lists
            values.remove(0);
            indexes.remove(0);

            //return Pair
            return pair;
        }

        public Pair<Double, Integer> peek(){
            //store first item in both lists as Pair
            if (values.size() == 0) return null;
            Pair<Double, Integer> pair = new Pair<Double,Integer>(values.get(0), indexes.get(0));

            //return Pair
            return pair;
        }
    }


}

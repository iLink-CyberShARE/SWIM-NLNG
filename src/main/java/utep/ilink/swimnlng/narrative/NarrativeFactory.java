package utep.ilink.swimnlng.narrative;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mongodb.morphia.Datastore;

import com.fasterxml.jackson.core.JsonProcessingException;

import utep.ilink.swimnlng.models.narratives.NLNGReqPayload;

public class NarrativeFactory {

    private NLNGReqPayload _reqPayload;
    private SWIMVariable _swimVariable;
    private Datastore _swimDatastore;
    private Datastore _narrativeDatastore;

    /** Constructors **/

    /**
     * Constructor only using the payload request
     * @param payload narrative generation request payload
     */
    public NarrativeFactory(NLNGReqPayload payload) { 
        System.out.println("Payload only factory invoked.");
        _reqPayload = payload;
        try {
            this.ParsePayload(_reqPayload.toJSONString());
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Constructor using payload and narrative data store
     * @param payload narrative generation request payload
     * @param narrativeStore morphia datastore to narrative database
     */
    public NarrativeFactory(NLNGReqPayload payload, Datastore narrativeStore) { 
        System.out.println("Payload and narrative db factory invoked.");
        _reqPayload = payload;
        _narrativeDatastore = narrativeStore;
    }

    /**
     * Constructor using payload, narrative and swim modeling stores
     * @param payload narrative generation request payload
     * @param narrativeStore morphia datastore to narrative database
     * @param swimStore morphia datastore to swim database
     */
    public NarrativeFactory(NLNGReqPayload payload, Datastore narrativeStore, Datastore swimStore){
        System.out.println("Payload, narrative and swim db factory invoked.");
        _reqPayload = payload;
        _narrativeDatastore = narrativeStore;
        _swimDatastore = swimStore;
    }

    /** Methods **/

    /**
     * Convert the JSON in string format to a SWIM Variable Object for data analysis
     * @param json_payload
     */
    private void ParsePayload(String json_payload){
        JSONParser parser = new JSONParser();
        JSONObject json_payload_object;
        try {
            json_payload_object = (JSONObject) parser.parse(json_payload);
            SWIMJSONParser swimParser = new SWIMJSONParser(json_payload_object); 
            _swimVariable = swimParser.parseJSON();
            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * Performs output data analysis and assembled a text based narrative.
     */
    public String GenerateNarrative(){
        Analysis analysis = new Analysis();
        List<SWIMVariable> variables = new ArrayList<>();
        variables.add(_swimVariable);
        List<String> descriptions = analysis.analyzeVars(variables);
        return Arrays.toString(descriptions.toArray()).replace("[", "").replace("]", "");
    }

    
}

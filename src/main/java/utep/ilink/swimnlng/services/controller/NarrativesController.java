package utep.ilink.swimnlng.services.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import utep.ilink.swimnlng.auth.Auth;
import utep.ilink.swimnlng.db.MongoHandler;
import utep.ilink.swimnlng.db.RelationalHandler;
import utep.ilink.swimnlng.models.narratives.NLNGReqPayload;
import utep.ilink.swimnlng.narrative.NarrativeFactory;
import utep.ilink.swimnlng.services.api.ServiceConfiguration;

import com.codahale.metrics.annotation.Timed;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.POST;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;


@Path("/output")
@Api(value = "/narratives", description = "Narrative Generation Endpoints")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NarrativesController {

    private ServiceConfiguration configuration = null;
    private Logger _logger;

    /**
     * Endpoints for model-to-model orchestration.
     * 
     * @param configuration mappings to service configuration file
     */
    public NarrativesController(ServiceConfiguration configuration) {
        this.configuration = configuration;
        this._logger = LoggerFactory.getLogger(NarrativesController.class);
    }

    @POST
    @Timed
    @ApiOperation(value = "Request output narrative", notes = "Generates a narrative from a given model output, language and role target")
    public Object run(@HeaderParam("Authorization") String auth, @NotNull @Valid NLNGReqPayload payload) {

        try {

            System.out.println("Initializing Database Connections...");

            // validate authentication
            RelationalHandler authdb = new RelationalHandler(configuration.getAuthDBDriver());
            authdb.setDataSource(configuration.getAuthDBHost(), String.valueOf(configuration.getAuthDBPort()), configuration.getAuthDBName());
            Auth identity = new Auth(authdb, configuration.getAuthDBUser(), configuration.getAuthDBPassword());
            if (!identity.isAuthValid(auth)) {
                throw new Exception("Invalid Authorization Token");
            }            

            // database connectivity (narrative db)
            MongoHandler nmh = new MongoHandler(configuration.getNarrativedbuser(),
                    configuration.getNarrativedbpassword(),
                    configuration.getNarrativedbname(), configuration.getNarrativedbhost(),
                    configuration.getNarrativedbport(), configuration.getNarrativedbauth()); 
                    
            // check connection to narrative db was established        
            if (!nmh.isConnected()) {
                throw new Exception("Connection to SWIM Modeling database could not be established");
            }

            // database connectivity (swim db)
            MongoHandler smh = new MongoHandler(configuration.getSWIMDBUser(), configuration.getSWIMDBPassword(),
                    configuration.getSWIMDBName(), configuration.getSWIMDBHost(), configuration.getSWIMDBPort(),
                    configuration.getSWIMDBAuth());

            // check the connection to swim db was established
            if (!smh.isConnected()) {
                throw new Exception("Connection to SWIM Modeling database could not be established");
            }

            System.out.println("Preparing ORM Mapper to Mongo...");

            // narrative db ORM via morphia
            Morphia narrativeMorphia = new Morphia();

            // manually map model classes if necessary
            // narrativeMorphia.map(template.class) (this is just an  example)

            Datastore narrativeDatastore = narrativeMorphia.createDatastore(nmh.getMongoClient(), configuration.getNarrativedbname());
            
            // swim db ORM via morphia
            Morphia swimMorphia = new Morphia();
            
            // manually map model classes if necessary
            // swimMorphia.map(SWIMOutput.class) (this is just an example)

            Datastore swimDatastore = swimMorphia.createDatastore(smh.getMongoClient(), configuration.getSWIMDBName());

            System.out.println("Initializing Narrative Factory...");

            // narrative factory (payload only option for now)
            NarrativeFactory nf = new NarrativeFactory(payload);

            System.out.println("Generating Narratives...");
            String narrative = nf.GenerateNarrative();

            System.out.println("Closing Mongo Connections...");
            // close database connections
            smh.closeConnection();

            System.out.println("Generation Complete.");
            // return narrative in JSON format
            String message = "{\"result\": \"" + narrative + "\"}";
            return Response.ok().entity(message).build();
        }

        catch (Exception e) {
            String exception = "An internal error ocurred.";
            // e.printStackTrace(); // debug only
            if (e.getMessage() != null) {
                exception = e.getMessage();
            }
            _logger.error(e.toString());
            String message = "{\"message\": \"" + exception + "\"}";
            return Response.serverError().entity(message).build();
        }

    }

}

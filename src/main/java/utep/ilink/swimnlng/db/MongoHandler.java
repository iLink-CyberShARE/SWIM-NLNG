package utep.ilink.swimnlng.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

public class MongoHandler {
    
    private String _mongoUser;
    private String _mongoPassword;
    private String _mongoDB;
    private String _mongoHost;
    private int _mongoPort;
    private String _mongoAuthSource = "";
    
    private MongoClient _mongoClient;

    private Logger _logger;
    
    /**
     * Mongo handler constructor.
     * @param mongoUser database user name
     * @param mongoPassword database password
     * @param mongoDB database instance name
     * @param mongoHost host address
     * @param mongoPort host port
     * @param mongoAuthSource user authentication source database
     */
    public MongoHandler(String mongoUser, String mongoPassword, String mongoDB, String mongoHost, int mongoPort, String mongoAuthSource) {
        super();
        this._mongoUser = mongoUser;
        this._mongoPassword = mongoPassword;
        this._mongoDB = mongoDB;
        this._mongoHost = mongoHost;
        this._mongoPort = mongoPort;
        this._mongoAuthSource = mongoAuthSource;

        this._logger = LoggerFactory.getLogger(MongoHandler.class);
        this.openConnection();
    }
    
    /**
     * Open database connection.
     */
    private void openConnection(){
        try {
            //Mongo Client connection
            MongoClientURI uri = new MongoClientURI("mongodb://"+_mongoUser+":"+_mongoPassword+"@"+_mongoHost+":"+_mongoPort+"/"+_mongoDB+"?authSource="+_mongoAuthSource+"");
            _mongoClient = new MongoClient(uri);

            if(!this.isConnected()){
                throw new Exception("Mongo " + _mongoDB + " connection was not established");
            }

        } catch(MongoException e){
            _logger.error(e.toString());
            // e.printStackTrace();         
        } catch(Exception e){
            _logger.error(e.toString());
            // e.printStackTrace();
        }
        
    }

    /**
     * Get name of connected database instance.
     * @return database name
     */
    public String getDBName(){
        return  _mongoDB;
    }
    
    /**
     * Returns the status of the mongo db connection.
     * @return true if connected.
     */
    public boolean isConnected(){
        try {
            _mongoClient.getAddress();
        } catch (Exception e) {
            System.out.println("Mongo is down");
            return false;
        }
        return true;
    }

    /**
     * Closes currently open mongo connection.
     */
    public void closeConnection(){
        this._mongoClient.close();
    }
    
    /**
     * Get Mongo client object.
     * @return instance of a mongo client
     */
    public MongoClient getMongoClient() {
        return _mongoClient;
    }

}

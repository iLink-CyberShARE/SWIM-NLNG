package utep.ilink.swimnlng.health;

import com.codahale.metrics.health.HealthCheck;

import utep.ilink.swimnlng.db.MongoHandler;

public class MongoHealthCheck extends HealthCheck {
    private final String mongoUser;
    private final String mongoPassword;
    private final String mongoDB;
    private final String mongoHost;
    private final int mongoPort;
    private final String mongoAuthSource;

    public MongoHealthCheck(String user, String password, String dbname, String host, int port, String authSource) {
        this.mongoUser = user;
        this.mongoPassword = password;
        this.mongoDB = dbname;
        this.mongoHost = host;
        this.mongoPort = port;
        this.mongoAuthSource = authSource;
    }

    @Override
    protected Result check() throws Exception {
        MongoHandler mongoH = new MongoHandler(this.mongoUser, this.mongoPassword, this.mongoDB, this.mongoHost,
                this.mongoPort, this.mongoAuthSource);

        if (!mongoH.isConnected())
            return Result.unhealthy("Mongo connection could not be established");
        mongoH.closeConnection();
        return Result.healthy();
    }
}

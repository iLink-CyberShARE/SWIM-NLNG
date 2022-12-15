package utep.ilink.swimnlng.health;

import com.codahale.metrics.health.HealthCheck;

import utep.ilink.swimnlng.db.RelationalHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class RelationalHealthCheck extends HealthCheck {
    private final String dbUser;
    private final String dbPassword;
    private final String dbName;
    private final String dbHost;
    private final int dbPort;
    private final String dbDriver;

    public RelationalHealthCheck(String user, String password, String name, String host, int port, String driver) {
        this.dbUser = user;
        this.dbPassword = password;
        this.dbName = name;
        this.dbHost = host;
        this.dbPort = port;
        this.dbDriver = driver;
    }

    @Override
    protected Result check() throws Exception {
        boolean res = false;

        // connection to relational database
        RelationalHandler selector = new RelationalHandler(dbDriver);
        selector.setDataSource(dbHost, String.valueOf(dbPort), dbName);
        final Connection connection = selector.getConnection(dbUser, dbPassword);

        // make a ping to the database
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("/* ping */ SELECT 1");

        if (rs.next())
            res = true;
        connection.close();

        if (!res)
            return Result.unhealthy("MySQL connection could not be established");
        return Result.healthy();
    }
}
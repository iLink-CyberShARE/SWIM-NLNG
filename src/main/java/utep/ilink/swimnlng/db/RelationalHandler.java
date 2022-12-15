package utep.ilink.swimnlng.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Garnica
 * Only manages mysql connections
 */
public class RelationalHandler {
    
    private String _dbms_name = null;
    private String _datasource = null;
    
    private String _host = "localhost";
    private String _port;
    private String _schema;
    private String _username;
    private String _password;
    
    public RelationalHandler(String dbms_name) {
        _dbms_name = dbms_name;
    }
    
    public void setDataSource(String host, String port, String schema){
        this._host = host;
        this._port = port;
        this._schema = schema;

        if (_dbms_name.equalsIgnoreCase("mysql"))
            mysqlDataSource();
    }
    
    public Connection getConnection (String username, String password){
        try {
            this._username = username;
            this._password = password;
            Connection connection = DriverManager.getConnection(_datasource, _username, _password);
            return connection;
        } catch (SQLException ex) {
            Logger.getLogger(RelationalHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /* Data source Setters */
    private void mysqlDataSource(){
        try {
            _datasource =  "jdbc:mysql://"+_host+":"+_port+"/"+_schema;
        } catch (Exception ex) {
            Logger.getLogger(RelationalHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /* Getters and Setters */
    public String getDbms_name() {
        return _dbms_name;
    }

    public void setDbms_name(String dbms_name) {
        this._dbms_name = dbms_name;
    }
    
    public String getSchema() {
        return _schema;
    }
    
}

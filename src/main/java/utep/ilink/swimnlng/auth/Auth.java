package utep.ilink.swimnlng.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import utep.ilink.swimnlng.db.RelationalHandler;

public class Auth {

    private RelationalHandler _authdb; 
    private String _userdb;
    private String _dbpassword;
    private Logger _logger;

    /**
     * Auth constructor.
     * @param authdb auth database handler
     * @param dbuser auth database user
     * @param dbpassword auth database password
     */
    public Auth(RelationalHandler authdb, String dbuser, String dbpassword){
        this._authdb = authdb;
        this._userdb = dbuser;
        this._dbpassword = dbpassword;

        this._logger = LoggerFactory.getLogger(RelationalHandler.class);
    }

    /**
     * Validate authentication string (JWT token)
     * @param auth authentication string
     * @return true if valid
     */
    public boolean isAuthValid(String auth){
        try{
            if (auth == null || auth == ""){
                throw new Exception("Empty or null token string");
            }

            if (!auth.contains("Bearer") ){
                throw new Exception("Missing Bearer on auth token");
            }
    
            String hush = this.RetrieveHush();
            auth = auth.split(" ", 2)[1];
            Claims claims = Jwts.parserBuilder().setSigningKey(hush.getBytes("UTF-8")).build()
                    .parseClaimsJws(auth).getBody();
            String userId = claims.get("id").toString();
    
            System.out.println("User connected: " + userId);

            /* if extraction success, return true */
            return userId != null;

        } catch(Exception e) {
            _logger.error(e.toString());
            return false;
        }

    }

    /**
     * Retrieve the secret key form the database
     * @return secret key string
     */
    private String RetrieveHush(){
        final Connection con = _authdb.getConnection(_userdb, _dbpassword);

        String hush = "";

        /* Get the hush key */
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from hush");

            /* Get the last key available */
            while (rs.next()) {
                hush = rs.getString("value");
            }

            /* close connection to secret db */
            con.close();

            // assign to hush attribute
            return hush;

        } catch (SQLException e) {
            _logger.error(e.toString());
            return null;
        }
    }


}

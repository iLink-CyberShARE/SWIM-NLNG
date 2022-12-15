package utep.ilink.swimnlng.services.api;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import io.federecio.dropwizard.swagger.*;

public class ServiceConfiguration extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    /* SWIM-DB Credentials */
    @NotEmpty
    private String swimdbuser;
    @NotEmpty
    private String swimdbpassword;
    @NotEmpty
    private String swimdbhost;
    @NotEmpty
    private String swimdbname;

    private String swimdbauth;
    @Min(1)
    @Max(65535)
    private int swimdbport;

    /* Narratives-DB Credentials */

    @NotEmpty
    private String narrativedbuser;
    @NotEmpty
    private String narrativedbpassword;
    @NotEmpty
    private String narrativedbhost;
    @NotEmpty
    private String narrativedbname;
    @Min(1)
    @Max(65535)
    private int narrativedbport;
    private String narrativedbauth;

    /* Auth-DB Credentials */
    @NotEmpty
    private String authDBDriver;
    @NotEmpty
    private String authDBHost;
    @Min(1)
    @Max(65535)
    private int authDBPort;
    @NotEmpty
    private String authDBName;
    @NotEmpty
    private String authDBUser;
    @NotEmpty
    private String authDBPassword;

    /* User-DB Credentials */
    @NotEmpty
    private String userDBDriver;
    @NotEmpty
    private String userDBHost;
    @Min(1)
    @Max(65535)
    private int userDBPort;
    @NotEmpty
    private String userDBName;
    @NotEmpty
    private String userDBUser;
    @NotEmpty
    private String userDBPassword;

    /* Getters and Setters */

    /* SWIM-DB */

    @JsonProperty
    public String getSWIMDBUser() {
        return swimdbuser;
    }

    public void setSWIMDBUser(String swimdbuser) {
        this.swimdbuser = swimdbuser;
    }

    @JsonProperty
    public String getSWIMDBPassword() {
        return swimdbpassword;
    }

    public void setSWIMDBPassword(String swimpassword) {
        this.swimdbpassword = swimpassword;
    }

    @JsonProperty
    public String getSWIMDBHost() {
        return swimdbhost;
    }

    public void setSWIMDBHost(String swimdbhost) {
        this.swimdbhost = swimdbhost;
    }

    @JsonProperty
    public String getSWIMDBName() {
        return swimdbname;
    }

    public void setSWIMDBName(String swimdbname) {
        this.swimdbname = swimdbname;
    }

    @JsonProperty
    public int getSWIMDBPort() {
        return swimdbport;
    }

    public void setSWIMDBPort(int swimdbport) {
        this.swimdbport = swimdbport;
    }

    @JsonProperty
    public String getSWIMDBAuth() {
        return swimdbauth;
    }

    public void setSWIMAuth(String swimdbauth) {
        this.swimdbauth = swimdbauth;
    }

    /* NARRATIVE-DB */
    @JsonProperty
    public String getNarrativedbuser() {
        return narrativedbuser;
    }

    public void setNarrativedbuser(String narrativedbuser) {
        this.narrativedbuser = narrativedbuser;
    }

    @JsonProperty
    public String getNarrativedbpassword() {
        return narrativedbpassword;
    }

    public void setNarrativedbpassword(String narrativedbpassword) {
        this.narrativedbpassword = narrativedbpassword;
    }

    @JsonProperty
    public String getNarrativedbhost() {
        return narrativedbhost;
    }

    public void setNarrativedbhost(String narrativedbhost) {
        this.narrativedbhost = narrativedbhost;
    }

    @JsonProperty
    public String getNarrativedbname() {
        return narrativedbname;
    }

    public void setNarrativedbname(String narrativedbname) {
        this.narrativedbname = narrativedbname;
    }

    @JsonProperty
    public int getNarrativedbport() {
        return narrativedbport;
    }

    public void setNarrativedbport(int narrativedbport) {
        this.narrativedbport = narrativedbport;
    }

    @JsonProperty
    public String getNarrativedbauth() {
        return narrativedbauth;
    }

    public void setNarrativedbauth(String narrativedbauth) {
        this.narrativedbauth = narrativedbauth;
    }

    /* Auth-DB */

    @JsonProperty
    public String getAuthDBDriver() {
        return this.authDBDriver;
    }

    @JsonProperty
    public String getAuthDBHost() {
        return this.authDBHost;
    }

    @JsonProperty
    public int getAuthDBPort() {
        return this.authDBPort;
    }

    @JsonProperty
    public String getAuthDBName() {
        return this.authDBName;
    }

    @JsonProperty
    public String getAuthDBUser() {
        return this.authDBUser;
    }

    @JsonProperty
    public String getAuthDBPassword() {
        return this.authDBPassword;
    }

    /* User-DB */

    @JsonProperty
    public String getUserDBDriver() {
        return this.userDBDriver;
    }

    @JsonProperty
    public String getUserDBHost() {
        return this.userDBHost;
    }

    @JsonProperty
    public int getUserDBPort() {
        return this.userDBPort;
    }

    @JsonProperty
    public String getUserDBName() {
        return this.userDBName;
    }

    @JsonProperty
    public String getUserDBUser() {
        return this.userDBUser;
    }

    @JsonProperty
    public String getUserDBPassword() {
        return this.userDBPassword;
    }

}

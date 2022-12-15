package utep.ilink.swimnlng.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationToken {

    String idToken;
    String email;
    String expiresIn;
    String id;
    String role;
    String success;

    public AuthenticationToken(String idToken, String email, String expiresIn, String id, String role, String success) {
        this.idToken = idToken;
        this.email = email;
        this.expiresIn = expiresIn;
        this.id = id;
        this.role = role;
        this.success = success;
    }

    public AuthenticationToken() {

    }

    public String getIdToken() {
        return idToken;
    }
}
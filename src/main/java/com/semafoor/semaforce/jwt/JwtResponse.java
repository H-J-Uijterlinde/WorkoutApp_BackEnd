package com.semafoor.semaforce.jwt;

import java.io.Serializable;

/**
 * This class represents the JSON response sent by the authentication mechanism and contains the jwt token.
 */
public class JwtResponse implements Serializable {

    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}

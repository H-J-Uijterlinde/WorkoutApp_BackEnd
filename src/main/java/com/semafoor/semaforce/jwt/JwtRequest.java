package com.semafoor.semaforce.jwt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This class containing user credentials. Objects of this class are accepted by the /authenticate endpoint.
 */
@Getter
@Setter
public class JwtRequest implements Serializable {

    private String username;
    private String password;

    public JwtRequest() {
    }

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

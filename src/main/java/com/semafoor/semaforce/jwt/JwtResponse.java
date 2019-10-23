package com.semafoor.semaforce.jwt;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * This class represents the response send after successful authentication. It contains the Jwt token
 */
@Data
public class JwtResponse implements Serializable {

    private final String jwttoken;

}

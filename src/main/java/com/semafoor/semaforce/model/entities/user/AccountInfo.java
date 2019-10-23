package com.semafoor.semaforce.model.entities.user;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Embeddable class which defines a logical composition of columns/ properties of the User table/ entity.
 */
@Data
@Embeddable
public class AccountInfo {

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "First name is required. Minimum is 2, maximum is 255 characters"
    )
    private String firstName;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Last name is required. Minimum is 2, maximum is 255 characters"
    )
    private String lastName;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Email is required. Minimum is 2, maximum is 255 characters"
    )
    @Column(nullable = false, unique = true)
    private String email;

    AccountInfo() {
    }

    public AccountInfo(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}

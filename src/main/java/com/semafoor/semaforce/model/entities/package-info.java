/**
 * Package-info class used to define the identity generator for database persistence. It defines an enhanced sequence
 * generator. The advantage of using this strategy is that the Id will be available before the entity is really stored
 * in the database, so before the transactions needs to commit.
 */
@org.hibernate.annotations.GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "enhanced-sequence",
        parameters = {
                @org.hibernate.annotations.Parameter(
                        name = "sequence_name",
                        value = "SEMAFORCE_WORKOUT_APP_SEQUENCE"
                ),
                @org.hibernate.annotations.Parameter(
                        name = "initial_value",
                        value = "1000"
                )
        }
)
package com.semafoor.semaforce.model.entities;
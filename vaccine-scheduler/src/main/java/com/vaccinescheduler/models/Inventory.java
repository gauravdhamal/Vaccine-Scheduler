package com.vaccinescheduler.models;

import javax.persistence.OneToOne;

public class Inventory {

    /**
     * This is the owner field of relationship for user class "inventory" variable.
     */
    @OneToOne
    private User admin;
}

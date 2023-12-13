package com.vaccinescheduler.models;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class Hospital {

    /**
     * "One" hospital can have "Many" doctors present.
     * Mapped by "hospital" variable present in User(doctor) class.
     */
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL)
    private List<User> doctors = new ArrayList<>();
}

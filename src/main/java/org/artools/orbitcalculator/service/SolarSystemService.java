package org.artools.orbitcalculator.service;

import org.artools.orbitcalculator.entity.SolarSystemState;

import java.util.List;

public interface SolarSystemService {
    SolarSystemState saveSolarSystemState(SolarSystemState systemState);

    List<SolarSystemState> fetchSolarSystemStates();

    SolarSystemState getSolarSystemStateByID(String id);

    SolarSystemState updateSolarSystemState(SolarSystemState solarSystemState, String id);

    void deleteSolarSystemState(String id);
}

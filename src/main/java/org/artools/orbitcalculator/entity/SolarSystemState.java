package org.artools.orbitcalculator.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.artools.orbitcalculator.orbitcalculation.application.vector.Orrery;

import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolarSystemState {
    private String id;
    private Instant epoch;
    private Orrery orrery;
}

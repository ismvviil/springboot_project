package com.example.potager_v1.dto;

import com.example.potager_v1.model.Simulation;
import lombok.Data;

import java.util.Map;

@Data
public class SimulationStateDTO {
    private Simulation simulation;
    private Map<String, Object> statistiques;
}
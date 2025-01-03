package ies.grupo202.ATLAS_api.kafka.Entities;

import java.time.LocalDateTime;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VitalSignsMsg {
    private int astronaut_id;
    private double heartRate;
    private double bloodPressure;
    private double bodyTemperature;
    private double oxygenLevel;
    private LocalDateTime lastUpdate;

    public List<Double> getDoubles(){
        List<Double> double_values = new ArrayList<>();
        double_values.add(heartRate);
        double_values.add(bloodPressure);
        double_values.add(bodyTemperature);
        double_values.add(oxygenLevel);
        return double_values;
    }
}


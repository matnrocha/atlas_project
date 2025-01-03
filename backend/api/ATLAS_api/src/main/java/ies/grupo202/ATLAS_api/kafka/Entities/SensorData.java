package ies.grupo202.ATLAS_api.kafka.Entities;

import java.time.LocalDateTime;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SensorData {
    private int spaceship_id;
    private double cabinTemperature;
    private double cabinPressure;
    private double co2Level;
    private double ppo2Level;
    private double intTemperature;
    private double extTemperature;
    private double humidity;
    private double battery;
    private double velocity;
    private double altitude;
    private double apogee;
    private double perigee;
    private double inclination;
    private double range;
    private LocalDateTime timestamp;

    public List<Double> getDoubles(){
        List<Double> double_values = new ArrayList<>();
        double_values.add(cabinTemperature);
        double_values.add(cabinPressure);
        double_values.add(co2Level);
        double_values.add(ppo2Level);
        double_values.add(intTemperature);
        double_values.add(extTemperature);
        double_values.add(humidity);
        double_values.add(battery);
        double_values.add(velocity);
        double_values.add(altitude);
        double_values.add(apogee);
        double_values.add(perigee);
        double_values.add(inclination);
        double_values.add(range);
        return double_values;
    }
}


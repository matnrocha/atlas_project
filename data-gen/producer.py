from kafka import KafkaProducer
import json
import random
import time
import os
from datetime import datetime, timezone

sensor_topic = 'sensor-data'
vital_topic = 'vital-signs'
message_topic = 'messages'
bootstrap_servers = os.getenv('KAFKA_BOOTSTRAP_SERVERS')
# bootstrap_servers = "kafka:9092"

last_sensor_data = {
    "spaceship_id": 1,
    "battery": 80.0,
    "cabinTemperature": 22.0,
    "cabinPressure": 1.0,
    "co2Level": 400.0,
    "ppo2Level": 21.0,
    "intTemperature": 25.0,
    "extTemperature": -60.0,
    "humidity": 50.0,
    "velocity": 4000.0,
    "altitude": 15000.0,
    "apogee": 350000.0,
    "perigee": 250000.0,
    "inclination": 90.0,
    "range": 2000.0,
    "timestamp": datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')
}

def generate_spaceship_data():
    global last_sensor_data
    # gradual changes
    
    if "battery" not in last_sensor_data or last_sensor_data["battery"] > 100:
        last_sensor_data["battery"] = random.uniform(90, 100)
    drain_rate = random.uniform(0.05, 0.5)  # Decrease by a small random amount
    if random.random() < 0.3:
        last_sensor_data["battery"] += 0.5

    last_sensor_data["battery"] -= drain_rate
    last_sensor_data["cabinTemperature"] += random.uniform(-0.95, 0.95)
    last_sensor_data["intTemperature"] += random.uniform(-0.5, 0.5)
    last_sensor_data["cabinPressure"] += random.uniform(-0.30, 0.30)
    last_sensor_data["co2Level"] += random.uniform(-5.0, 5.0)
    last_sensor_data["ppo2Level"] += random.uniform(-1.3, 1.3)
    last_sensor_data["extTemperature"] += random.uniform(-1.2, 1.2)
    last_sensor_data["humidity"] += random.uniform(-1.1, 1.1)
    last_sensor_data["velocity"] += random.uniform(-100.0, 100.0)
    last_sensor_data["altitude"] += random.uniform(-500.0, 500.0)
    last_sensor_data["apogee"] += random.uniform(-700.0, 700.0)
    last_sensor_data["perigee"] += random.uniform(-500.0, 500.0)
    last_sensor_data["inclination"] += random.uniform(-25, 25)
    last_sensor_data["range"] += random.uniform(-400.0, 400.0)
    last_sensor_data["timestamp"] = datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')

    # limiting values and rounding
    last_sensor_data["battery"] = round(max(0, min(100, last_sensor_data["battery"])), 2)
    last_sensor_data["cabinTemperature"] = round(max(15.0, min(32.0, last_sensor_data["cabinTemperature"])), 2)
    last_sensor_data["intTemperature"] = round(max(8.0, min(35.0, last_sensor_data["intTemperature"])), 2)
    last_sensor_data["ppo2Level"] = round(max(19.5, min(23.5, last_sensor_data["ppo2Level"])), 2)
    last_sensor_data["extTemperature"] = round(max(-130.0, min(70.0, last_sensor_data["extTemperature"])), 2)
    last_sensor_data["humidity"] = round(max(20.0, min(100.0, last_sensor_data["humidity"])), 2)
    last_sensor_data["inclination"] = round(max(0.0, min(180.0, last_sensor_data["inclination"])), 2)
    last_sensor_data["range"] = round(max(0,min(2500,last_sensor_data["range"])))
    last_sensor_data["velocity"] = round(max(0,min(7000,last_sensor_data["velocity"])), 2)
    last_sensor_data["altitude"] = round(max(0,min(35000,last_sensor_data["altitude"])),2)
    last_sensor_data["apogee"] = round(max(0,min(350000,last_sensor_data["apogee"])),2)
    last_sensor_data["perigee"] = round(max(0,min(350000,last_sensor_data["perigee"])),2)
    last_sensor_data["cabinPressure"] = round(max(0.7,min(2.0,last_sensor_data["cabinPressure"])), 2)
    last_sensor_data["co2Level"] = round(last_sensor_data["co2Level"], 2)

    return last_sensor_data

last_astronaut_data = {
    "astronaut_id": 1,
    "heartRate": 65.0,
    "bloodPressure": 95.0,
    "bodyTemperature": 37.0,
    "oxygenLevel": 95.0,
    "lastUpdate": datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')
}

def generate_astronaut_data():
    global last_astronaut_data
    data = []
    for id in range(1,5):
        last_astronaut_data["astronaut_id"] = id
        last_astronaut_data["heartRate"] += random.uniform(-1.0, 1.0)
        last_astronaut_data["bloodPressure"] += random.uniform(-0.5, 0.5)
        last_astronaut_data["bodyTemperature"] += random.uniform(-0.3, 0.3)
        last_astronaut_data["oxygenLevel"] += random.uniform(-0.5, 0.5)
        last_astronaut_data["lastUpdate"] = datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')

        last_astronaut_data["heartRate"] = round(max(50.0, min(120.0, last_astronaut_data["heartRate"])), 2) 
        last_astronaut_data["bloodPressure"] = round(max(90.0, min(105.0, last_astronaut_data["bloodPressure"])), 2) 
        last_astronaut_data["bodyTemperature"] = round(max(36.0, min(38.5, last_astronaut_data["bodyTemperature"])), 2)
        last_astronaut_data["oxygenLevel"] = round(max(90.0, min(100.0, last_astronaut_data["oxygenLevel"])), 2) 
        data.append(last_astronaut_data.copy())
    return data

def generate_astronaut_message():
    message = {}
    message["messageLog_id"] = 1
    message["sender_id"] = random.choice([1, 2, 3, 4, 5])
    message["message"] = random.choice([
        "Hi, i am in space",
        "Wow, the view from here is amazing!",
        "Houston, we have a problem.",
        "Mission control, the view from here is breathtaking.",
        "Preparing for EVA, suit checks complete.",
        "Docking sequence initiated.",
        "All systems are nominal.",
        "We\'ve completed the repairs on the module.",
        "Requesting status update on re-entry trajectory.",
        "The solar array is fully deployed and functional.",
        "I spotted something unusual near the satellite.",
        "Life support systems are stable and running smoothly.",
        "Can someone confirm the coordinates for the next burn?",
        "The oxygen levels are holding steady.",
        "Executing experiment protocol as scheduled.",
        "This is one small step for a man, one giant leap for mankind.",
        "Fuel reserves are at optimal levels.",
        "Crew morale is high after the successful docking.",
        "Adjusting course, new heading 120 degrees.",
        "The food supplies are sufficient for the remainder of the mission.",
        "Standby, we're recalibrating the sensors.",
        "The thermal shielding is holding up as expected.",
        "We've entered the planned orbit.",
        "Requesting a weather update for the landing zone.",
        "Initiating power-down procedures for non-essential systems.",
        "This silence out here is unlike anything on Earth.",
        "Crew reporting slight turbulence during maneuver.",
        "Experiment samples have been safely stored.",
        "Commencing re-entry, wish us luck.",
        "The stars look so close, it\'s surreal.",
        "Deploying the rover, all systems go.",
        "Sending data logs for mission day 12.",
        "How are you?",
        "I am fine.",
        "I am hungry.",
        "I am thirsty.",
        "I am sleepy.",
        "I am tired.",
        "I am excited.",
        "I am scared.",
        "When is lunch?",
        "When is dinner?",
        "When do we get to go home?",
        "I miss my family.",
        "I miss my bed.",
        "I miss my dog.",
        "I miss my cat.",
        "I'll be glad when this mission is over.",
        "I can't wait to get back to Earth.",
        "I will never forget this experience.",
        "I am grateful for this opportunity.",
        "I am proud to be part of this mission.",
        "Can someone check the airlock?",
        "I think I saw something outside the window.",
        "Can someone check the solar panels?",
        "Could you repeat that?",
        "The toilet needs cleaning...",
        "I think I left my book in the lab.",
        "I can't find my pen.",
        "The toilet needs fixing... again.",
        "Please stop leaving the windows open during spacewalks. It gets chilly inside :(",
        "I love IES <3",
    ])
    message["timestamp"] = datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')

    return message

def generate_system_message():
    message = {}
    message["messageLog_id"] = 1
    message["message"] = random.randint(200, 1750)
    message["timestamp"] = datetime.now(timezone.utc).strftime('%Y-%m-%dT%H:%M:%S')

    return message


producer = KafkaProducer(
    bootstrap_servers=bootstrap_servers,
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

try:
    while True:
        sensor_data_message = generate_spaceship_data()
        producer.send(sensor_topic, value=sensor_data_message)
        print(f"Sent: {sensor_data_message}")

        vital_signs = generate_astronaut_data()
        for msg in vital_signs:
            producer.send(vital_topic, value=msg)
            print(f"Sent: {msg}")

        message = generate_astronaut_message()
        producer.send(message_topic, value=message)
        print(f"Sent: {message}")

        system_message = generate_system_message()
        producer.send(message_topic, value=system_message)
        print(f"Sent: {system_message}")
        
        time.sleep(3)
finally:
    producer.close()

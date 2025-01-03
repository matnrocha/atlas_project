const rolePermissions = {
  "/home": ["FLIGHT_DIRECTOR", "ASTRONAUT"],
  "/crew": ["FLIGHT_DIRECTOR", "ASTRONAUT"],
  "/communication": ["FLIGHT_DIRECTOR", "ASTRONAUT"],
  "/alerts": ["FLIGHT_DIRECTOR", "ASTRONAUT"],
  "/reports": ["CEO", "FLIGHT_DIRECTOR"],
  "/data": ["CEO", "FLIGHT_DIRECTOR"]
};

export default rolePermissions;

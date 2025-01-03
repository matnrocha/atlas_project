import apiClient from "./api";

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`; 
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Function to fetch alerts for a specific spaceship
export const getAlerts = async (id) => {
  try {
    const response = await apiClient.get(`/spaceship/${id}/alerts/`); // Fetch alerts data
    return response.data; // Return the data from the API
  } catch (error) {
    console.error("Error fetching alerts:", error);
    return []; // Return an empty array in case of an error
  }
};

// export const postAlert = async (alerts) => {
//   try {
//     const response = await apiClient.post("/spaceship/1/alerts/", { alerts });
//     console.log("Alerts posted successfully:", response.data);
//   } catch (error) {
//     console.error("Error posting alerts:", error);
//   }
// };

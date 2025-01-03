import apiClient from "./api";

apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token'); 
    if (token) {
        config.headers.Authorization = `Bearer ${token}`; 
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export const getLatestData = async (id) => {
    try {
        const response = await apiClient.get(`/spaceship/${id}/latest_sensor_data/`);
        return response.data;
    } catch (error) {
        console.error(error);
    }
}

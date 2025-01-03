import apiClient from "./api";


export const getVitalSigns = async (id) => {
    try {
        const response = await apiClient.get(`/astronaut/vitalSigns/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
    }
}

export const postVitalSigns = async (id) => {
    try {
        const response = await apiClient.post(`/astronaut/vitalSigns/${id}`);
        return response.data;
    } catch (error) {
        console.error(error);
    }
}


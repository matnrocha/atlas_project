import apiClient from "./api";

export const getReport = async (id, startTime, endTime) => {
    try {
        const response = await apiClient.get(`/spaceship/${id}/report/${startTime}/${endTime}`);
        return response.data;
    } catch (error) {
        console.error(error);
    }
}




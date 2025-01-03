import apiClient from "./api";


export const getAllAstronauts = async () => {
    try {
      const response = await apiClient.get(`/astronaut/astronauts`);
      return response.data;
    } catch (error) {
      console.error("Error fetching astronauts:", error);
      return [];
    }
  };
  

// export const getAstronaut = async (id) => {
//     try {
//         const response = await client.get(`/astronaut/astronaut/${id}`);
//         return response.data;
//     } catch (error) {
//         console.error(error);
//     }
// }

// export const postAstronauts = async () => {
//     try {
//         const response = await client.post(`/astronaut/astronaut`);
//         return response.data;
//     } catch (error) {
//         console.error(error);
//     }
// }
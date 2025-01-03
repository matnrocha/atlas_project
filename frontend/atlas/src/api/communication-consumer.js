import apiClient from "./api";


// export const getMessages = async (id) => {
//     try {
//         const response = await apiClient.get(`/messages/${id}/messages`);
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching messages:", error);
//         return [];
//     }
// };

// export const getMessagesBySenderName = async (id, sender) => {
//     try {
//         const response = await apiClient.get(`/messages/messages/${id}/sender`, {
//             params: { sender }, 
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching messages:", error);
//         return [];
//     }
// };

// export const getMessagesByMatchingSubstring = async (id, substring) => {
//   try {
//       const response = await apiClient.get(`/messages/messages/${id}/search`, {
//           params: { substring },
//       });
//       return response.data;
//   } catch (error) {
//       console.error("Error fetching messages:", error);
//       return [];
//   }
// };


// export const getMessagesByMatchingSubstringAndSender = async (id, sender, substring) => {
//     try {
//         const response = await apiClient.get(`/messages/messages/${id}/sender/search`, {
//             params: { sender, substring },
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error fetching messages:", error);
//         return [];
//     }
// }

export const getNLatestMessages = async (id, n) => {
    try {
        const response = await apiClient.get(`/messages/${id}/messages/latest`, {
            params: { n },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching messages:", error);
        return [];
    }
};

export const getNLatestMessagesBySenderName = async (id, n, sender) => {
    try {
        const response = await apiClient.get(`/messages/${id}/messages/latest/sender`, {
            params: { n, sender }, 
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching messages:", error);
        return [];
    }
};

export const getNLatestMessagesByMatchingSubstring = async (id, n, substring) => {
  try {
      const response = await apiClient.get(`/messages/${id}/messages/latest/search`, {
          params: { n, substring },
      });
      return response.data;
  } catch (error) {
      console.error("Error fetching messages:", error);
      return [];
  }
};


export const getNLatestMessagesByMatchingSubstringAndSender = async (id, n, sender, substring) => {
    try {
        const response = await apiClient.get(`/messages/${id}/messages/latest/sender/search`, {
            params: { n, sender, substring },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching messages:", error);
        return [];
    }
}

export const getNLatestSystemMessages = async (id, n) => {
    try {
        const response = await apiClient.get(`/messages/${id}/messages/system/latest`, {
            params: { n },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching system messages:", error);
        return [];
    }
}

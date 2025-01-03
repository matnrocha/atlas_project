import axios from 'axios';

const url = import.meta.env.VITE_API_URL;
const apiClient = axios.create({
  baseURL: `${url}/api/v1`,
});

apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      window.location.href = '/';
    }
    return Promise.reject(error);
  }
);

export default apiClient;

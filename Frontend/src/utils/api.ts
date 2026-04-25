import axios from "axios";

const api = axios.create({
  baseURL: "https://ecommerce-web-app-1-hhn4.onrender.com/api",
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken"); // ✅ FIXED

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export default api;

import axios from "axios";


const api = axios.create({
  baseURL: "https://ecommerce-web-app-1-hhn4.onrender.com/api",
  headers: {
    "Content-Type": "application/json",
  },
});

// 🔐 Attach token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

// 🚨 Handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    if (status === 401) {
      console.log("🔒 Unauthorized - logging out");

      // remove token
      localStorage.removeItem("accessToken");

      // redirect to login
      window.location.href = "/";
    }

    if (status === 403) {
      console.log("⛔ Forbidden - no permission");
      alert("You are not allowed to access this page");
    }

    return Promise.reject(error);
  }
);

export default api;

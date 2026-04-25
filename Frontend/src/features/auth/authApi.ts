import api from "../../api/axios";

interface LoginRequest {
  email: string;
  password: string;
}

interface LoginResponse {
  success: boolean;
  data: {
    accessToken: string;
    refreshToken: string;
  };
}

export const loginRequest = async (
  payload: LoginRequest
): Promise<LoginResponse> => {
  const response = await api.post("/auth/login", payload);
  return response.data;


  
};



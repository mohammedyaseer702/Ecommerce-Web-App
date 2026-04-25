import api from "../../api/axios";

export const createStripeSession = async (amount: number) => {
  const response = await api.post("/payment/create-session", {
    amount,
  });

  return response.data.sessionId;
};
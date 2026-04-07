import api from "../../api/axios";

interface CreateOrderRequest {
  paymentMethod: string;
}

// ✅ Create Order
export const createOrder = async (
  orderData: CreateOrderRequest
) => {
  const response = await api.post("/orders/place", orderData);
  return response.data;
};

// ✅ Get My Orders
export const getMyOrders = async () => {
  const response = await api.get("/orders/my-orders");
  return response.data.data;
};
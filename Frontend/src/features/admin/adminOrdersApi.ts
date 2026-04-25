import api from "../../api/axios";

export const getAllOrders = async () => {
  const response = await api.get("/orders/admin/all");
  return response.data.data;
};

export const updateOrderStatus = async (
  orderId: number,
  status: string
) => {
  await api.put("/orders/admin/update-status", null, {
    params: { orderId, status },
  });
};
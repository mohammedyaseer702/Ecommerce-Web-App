import api from "../../api/axios";
import type { Product } from "../products/types";

export const getAdminProducts = async (): Promise<Product[]> => {
  const res = await api.get("/products/public");
  return res.data.data.content;
};

export const addProduct = async (data: any) => {
  return api.post("/products/admin", data);
};

export const updateProduct = async (id: number, data: any) => {
  return api.put(`/products/admin/${id}`, data);
};

export const deleteProduct = async (id: number) => {
  return api.delete(`/products/admin/${id}`);
};
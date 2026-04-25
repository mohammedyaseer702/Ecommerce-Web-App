import api from "../../api/axios";
import type { Product } from "./types";


export const getAllProducts = async (): Promise<Product[]> => {
  const response = await api.get("/products/public");

  console.log("Products API response:", response.data);

  return response.data.data.content; // 👈 use this
};
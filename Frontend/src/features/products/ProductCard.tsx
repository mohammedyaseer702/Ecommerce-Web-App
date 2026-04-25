import { useState } from "react";
import { useCart } from "../cart/CartContext";
import { useNavigate } from "react-router-dom";

export default function ProductCard({ product }: any) {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const { addToCart } = useCart();

  const handleAddToCart = async () => {
    try {
      setLoading(true);
      await addToCart(product);
      alert("Added to cart");
    } catch (error: any) {
      alert(error?.response?.data?.message || "Failed to add to cart");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white shadow rounded-xl p-4">
      

  {/* ✅ CLICKABLE AREA (IMAGE + NAME ONLY) */}
  <div
    className="cursor-pointer"
   onClick={() => {
  const id = product.id || product.productId;
  console.log("CLICK ID:", id);

  if (!id) {
    alert("Product ID missing");
    return;
  }

  navigate(`/product/${id}`);
}}
  >
    <img
      src={product.imageUrl}
      alt={product.name}
      className="w-full h-40 object-cover rounded mb-3"
    />

    <h2 className="font-semibold">{product.name}</h2>
  </div>

  <p>₹{product.price}</p>

  {/* STOCK */}
  <p className={`text-sm ${
    product.stock > 0 ? "text-green-600" : "text-red-500"
  }`}>
    {product.stock > 0 ? "In Stock" : "Out of Stock"}
  </p>

  {/* ✅ BUTTON (NO REDIRECT NOW) */}
  <button
    onClick={handleAddToCart}
    disabled={loading || product.stock === 0}
    className="bg-black text-white px-4 py-2 rounded mt-3 disabled:bg-gray-400"
  >
    {product.stock === 0
      ? "Out of Stock"
      : loading
      ? "Adding..."
      : "Add to Cart"}
  </button>

</div>
  );
  
}

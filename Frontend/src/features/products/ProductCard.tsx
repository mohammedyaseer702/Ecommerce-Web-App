import { useState } from "react";
import { useCart } from "../cart/CartContext";

export default function ProductCard({ product }: any) {
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
      <h2 className="font-semibold">{product.name}</h2>
      <p>₹{product.price}</p>

      <button
        onClick={handleAddToCart}
        disabled={loading}
        className="bg-black text-white px-4 py-2 rounded mt-3"
      >
        {loading ? "Adding..." : "Add to Cart"}
      </button>
    </div>
  );
}

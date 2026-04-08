import { useCart } from "./CartContext";
import { useState } from "react";
import { createStripeSession } from "../payment/paymentApi";


export default function CartPage() {
  const {
    cart,
    removeFromCart,
    increaseQty,
    decreaseQty,
    getTotal,
  } = useCart();


  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  // ✅ Checkout Function
  const handleCheckout = async () => {
  try {
    setLoading(true);

    const amount = getTotal() * 100;

    const checkoutUrl = await createStripeSession(amount);

    // 🔥 THIS IS THE CORRECT WAY NOW
    window.location.href = checkoutUrl;

  } catch (error) {
    console.error(error);
    setError("Payment failed");
  } finally {
    setLoading(false);
  }
};

  if (cart.length === 0) {
    return (
      <div className="text-center text-gray-500">
        Your cart is empty.
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Shopping Cart</h1>

      {cart.map((item) => (
        <div
          key={item.id}
          className="bg-white shadow rounded-xl p-4 flex justify-between items-center"
        >
          <div className="flex items-center gap-4">
            <img
              src={item.imageUrl}
              alt={item.name}
              className="w-20 h-20 object-cover rounded"
            />

            <div>
              <h2 className="font-semibold">{item.name}</h2>
              <p className="text-gray-500">₹{item.price}</p>
            </div>
          </div>

          <div className="flex items-center gap-3">
            <button
              onClick={() => decreaseQty(item.id)}
              className="px-3 py-1 bg-gray-200 rounded"
            >
              -
            </button>

            <span>{item.quantity}</span>

            <button
              onClick={() => increaseQty(item.id)}
              className="px-3 py-1 bg-gray-200 rounded"
            >
              +
            </button>

            <button
              onClick={() => removeFromCart(item.id)}
              className="text-red-500 ml-4"
            >
              Remove
            </button>
          </div>
        </div>
      ))}

      {/* ✅ Checkout Section */}
      <div className="text-right space-y-3">
        <div className="font-bold text-xl">
          Total: ₹{getTotal()}
        </div>

        {error && (
          <div className="text-red-500">{error}</div>
        )}

        <button
          onClick={handleCheckout}
          disabled={loading}
          className="bg-black text-white px-6 py-3 rounded-lg hover:bg-gray-800 transition disabled:opacity-50"
        >
          {loading ? "Processing..." : "Checkout"}
        </button>
      </div>
    </div>
  );
}
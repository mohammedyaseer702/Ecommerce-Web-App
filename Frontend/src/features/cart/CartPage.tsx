import { useCart } from "./CartContext";
import { useState } from "react";
import api from "../../utils/api";

export default function CartPage() {
  const { cart, removeFromCart, increaseQty, decreaseQty, getTotal } = useCart();
  const [loading, setLoading] = useState(false);

  const handleCheckout = async () => {
  try {
    setLoading(true);

    // 🔥 STEP 1: Sync cart to backend
    await api.post("/cart/sync", {
      items: cart.map(item => ({
        productId: item.id,
        quantity: item.quantity
      }))
    });

    // 🔥 STEP 2: Place order
    const res = await api.post("/orders/place", {
      paymentMethod: "ONLINE"
    });

    console.log("ORDER RESPONSE:", res.data);

    const orderId = res.data?.data?.orderId;

    if (!orderId) {
      throw new Error("Order ID missing");
    }

    localStorage.setItem("orderId", orderId.toString());

    // 🔥 STEP 3: Go to payment success (temporary)
    const paymentUrl = res.data?.data?.paymentUrl;

if (!paymentUrl) {
  throw new Error("Payment URL missing");
}

window.location.href = paymentUrl;

  } catch (err: any) {
    console.error("Checkout error:", err.response?.data || err);
    alert(err.response?.data?.message || "Checkout failed");
  } finally {
    setLoading(false);
  }
};
  if (!cart || cart.length === 0) {
    return <div className="text-center mt-10">Cart is empty</div>;
  }

  return (
    <div className="p-6 space-y-4">
      <h1 className="text-2xl font-bold">Cart</h1>

      {cart.map((item) => (
        <div
          key={item.id}
          className="flex justify-between bg-white p-4 rounded shadow"
        >
          <img
      src={
        item.imageUrl?.startsWith("http")
          ? item.imageUrl
          : `http://localhost:8080${item.imageUrl}`
      }
      alt={item.productName}
      className="w-16 h-16 object-cover rounded"
    />
          <div>
            <h2 className="font-semibold">{item.name}</h2>
            <p>₹ {item.price}</p>
          </div>

          <div className="flex items-center gap-2">
            <button onClick={() => decreaseQty(item.id)}>-</button>
            <span>{item.quantity}</span>
            <button onClick={() => increaseQty(item.id)}>+</button>

            <button
              onClick={() => removeFromCart(item.id)}
              className="text-red-500 ml-4"
            >
              Remove
            </button>
          </div>
        </div>
      ))}

      <div className="text-right">
        <h2 className="text-xl font-bold">Total: ₹{getTotal()}</h2>

        <button
          onClick={handleCheckout}
          disabled={loading}
          className="bg-black text-white px-6 py-2 rounded mt-3"
        >
          {loading ? "Processing..." : "Checkout"}
        </button>
      </div>
    </div>
  );
}
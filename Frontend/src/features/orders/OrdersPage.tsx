import { useEffect, useState } from "react";
import api from "../../utils/api";

export default function OrdersPage() {
  const [orders, setOrders] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const res = await api.get("/orders/my-orders");

        const data = res?.data?.data;

        if (Array.isArray(data)) {
          setOrders(data);
        } else {
          setOrders([]);
        }
      } catch (err: any) {
        console.error("Orders error:", err?.response?.data || err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  if (loading) return <p className="text-center mt-10">Loading orders...</p>;

  if (!orders.length)
    return <p className="text-center mt-10">No orders yet</p>;

  return (
    <div className="p-8 bg-gray-50 min-h-screen">

  <h1 className="text-3xl font-bold mb-6">My Orders</h1>

  <div className="space-y-6">

    {orders.map((order:any) => (

      <div key={order.orderId} className="bg-white rounded-xl shadow p-6">

        {/* HEADER */}
        <div className="flex justify-between items-center mb-4">

          <div>
            <p className="font-semibold">Order #{order.orderId}</p>
            <p className="text-gray-500 text-sm">
  {order.orderDate
    ? new Date(order.orderDate).toLocaleDateString()
    : "N/A"}
</p>
          </div>

          <div className="text-right">
            <p className="font-bold text-lg">₹{order.totalAmount}</p>
            <p className={`text-sm font-semibold ${
              order.status === "DELIVERED"
                ? "text-green-600"
                : order.status === "CANCELLED"
                ? "text-red-500"
                : "text-yellow-500"
            }`}>
              {order.status}
            </p>
          </div>

        </div>

        {/* PRODUCTS */}
        <div className="space-y-3 border-t pt-4">

          {order.items?.map((item:any, i:number) => (
            <div key={i} className="flex justify-between items-center">

              <div>
                <p className="font-medium">{item.productName}</p>
                <p className="text-sm text-gray-500">
                  Qty: {item.quantity}
                </p>
              </div>

              <p className="font-semibold">₹{item.price}</p>

            </div>
          ))}

        </div>

        {/* ACTIONS */}
        <div className="flex gap-3 mt-4">

          <button
  onClick={() => window.location.href = `/orders/${order.orderId}`}
  className="bg-black text-white px-4 py-2 rounded"
>
  View Details
</button>

          <button
  onClick={() => alert(`Tracking Order #${order.orderId}\nStatus: ${order.status}`)}
  className="px-4 py-2 border rounded"
>
  Track Order
</button>

          {order.status !== "CANCELLED" && (
           <button
  onClick={async () => {
    try {
      await api.post(`/orders/cancel?orderId=${order.orderId}`);
      alert("Order cancelled");
      window.location.reload();
    } catch (err) {
      alert("Cancel failed");
    }
  }}
  className="px-4 py-2 border text-red-500 rounded"
>
  Cancel
</button>
          )}

          <button
  onClick={async () => {
    try {
      await api.post("/orders/place", { paymentMethod: "COD" });
      alert("Reorder placed");
    } catch {
      alert("Reorder failed");
    }
  }}
  className="px-4 py-2 bg-green-600 text-white rounded"
>
  Reorder
</button>

        </div>

      </div>

    ))}

  </div>
</div>
  );
}
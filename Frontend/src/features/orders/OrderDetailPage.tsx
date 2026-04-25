import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/axios";

export default function OrderDetailsPage() {
  const { id } = useParams();
  const [order, setOrder] = useState<any>(null);

  useEffect(() => {
    if (!id) return;

    api.get(`/orders/${id}`)
      .then(res => setOrder(res.data.data || res.data))
      .catch(() => alert("Failed to load order"));
  }, [id]);

  if (!order) return <p className="text-center mt-10">Loading...</p>;

  // ✅ TRACKING STATUS LOGIC
  const isPlaced = true;
  const isShipped =
    order.status === "SHIPPED" || order.status === "DELIVERED";
  const isDelivered = order.status === "DELIVERED";

  return (
    <div className="p-8 bg-gray-100 min-h-screen">

      <div className="max-w-4xl mx-auto space-y-6">

        {/* 🧾 ORDER HEADER */}
        <div className="bg-white p-6 rounded-xl shadow">
          <h1 className="text-2xl font-bold mb-2">
            Order #{order.orderId}
          </h1>

          <p className="text-gray-500">
            {order.orderDate
              ? new Date(order.orderDate).toLocaleString()
              : "N/A"}
          </p>

          <p className="mt-2">
            Status:
            <span className={`ml-2 font-semibold ${
              order.status === "DELIVERED"
                ? "text-green-600"
                : order.status === "CANCELLED"
                ? "text-red-500"
                : "text-yellow-500"
            }`}>
              {order.status}
            </span>
          </p>
        </div>

        {/* 🚚 TRACKING TIMELINE (AMAZON STYLE) */}
        <div className="bg-white p-6 rounded-xl shadow">
          <h2 className="text-lg font-semibold mb-4">
            Track Order
          </h2>

          <div className="flex items-center justify-between">

            {/* STEP 1 */}
            <div className="flex flex-col items-center">
              <div className={`h-4 w-4 rounded-full ${
                isPlaced ? "bg-green-500" : "bg-gray-300"
              }`} />
              <p className="text-xs mt-1">Placed</p>
            </div>

            <div className="flex-1 h-1 bg-gray-300 mx-2">
              <div className={`h-1 ${
                isShipped ? "bg-green-500" : ""
              }`} />
            </div>

            {/* STEP 2 */}
            <div className="flex flex-col items-center">
              <div className={`h-4 w-4 rounded-full ${
                isShipped ? "bg-green-500" : "bg-gray-300"
              }`} />
              <p className="text-xs mt-1">Shipped</p>
            </div>

            <div className="flex-1 h-1 bg-gray-300 mx-2">
              <div className={`h-1 ${
                isDelivered ? "bg-green-500" : ""
              }`} />
            </div>

            {/* STEP 3 */}
            <div className="flex flex-col items-center">
              <div className={`h-4 w-4 rounded-full ${
                isDelivered ? "bg-green-500" : "bg-gray-300"
              }`} />
              <p className="text-xs mt-1">Delivered</p>
            </div>

          </div>
        </div>

        {/* 📦 ITEMS */}
        <div className="bg-white p-6 rounded-xl shadow">
          <h2 className="text-lg font-semibold mb-4">
            Items
          </h2>

          <div className="space-y-4">

            {order.items?.map((item: any, i: number) => (
              <div key={i}
                className="flex justify-between items-center border p-4 rounded-lg">

                <div>
                  <p className="font-medium">
                    {item.productName}
                  </p>
                  <p className="text-sm text-gray-500">
                    Qty: {item.quantity}
                  </p>
                </div>

                <p className="font-semibold">
                  ₹{item.price}
                </p>

              </div>
            ))}

          </div>
        </div>

        {/* 💰 TOTAL */}
        <div className="bg-white p-6 rounded-xl shadow text-right">
          <p className="text-xl font-bold text-green-600">
            Total: ₹{order.totalAmount}
          </p>
        </div>

      </div>
    </div>
  );
}
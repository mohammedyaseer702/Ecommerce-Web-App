import { useEffect, useState } from "react";
import { getMyOrders } from "./ordersApi";
import type { Order } from "./types";

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const data = await getMyOrders();
        setOrders(data);
      } catch (err) {
        setError("Failed to load orders");
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  if (loading) {
    return <div className="text-center">Loading orders...</div>;
  }

  if (error) {
    return <div className="text-red-500">{error}</div>;
  }

  if (orders.length === 0) {
    return (
      <div className="text-center text-gray-500">
        You have no orders yet.
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {orders.map((order) => (
        <div
          key={order.id}
          className="bg-white shadow rounded-xl p-6"
        >
          <div className="flex justify-between mb-4">
            <div>
              <h2 className="font-semibold">
                Order #{order.id}
              </h2>
              <p className="text-sm text-gray-500">
                {new Date(order.createdAt).toLocaleString()}
              </p>
            </div>

            <div className="text-right">
              <p className="font-bold text-lg">
                ₹{order.totalAmount}
              </p>
              <p className="text-sm text-gray-500">
                {order.status}
              </p>
            </div>
          </div>

          <div className="border-t pt-4 space-y-2">
            {order.items.map((item) => (
              <div
                key={item.productId}
                className="flex justify-between text-sm"
              >
                <span>
                  {item.productName} × {item.quantity}
                </span>
                <span>₹{item.price}</span>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

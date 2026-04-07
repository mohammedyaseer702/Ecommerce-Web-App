import { useEffect, useState } from "react";
import { getAllOrders, updateOrderStatus } from "./adminOrdersApi";

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<any[]>([]);

  const fetchOrders = async () => {
    const data = await getAllOrders();
    setOrders(data);
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleStatusChange = async (
    orderId: number,
    status: string
  ) => {
    await updateOrderStatus(orderId, status);
    fetchOrders();
  };

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">
        Admin Orders
      </h1>

      <div className="space-y-4">
        {orders.map((order) => (
          <div
            key={order.id}
            className="bg-white shadow rounded-xl p-4"
          >
            <div className="flex justify-between">
              <div>
                <p className="font-semibold">
                  Order #{order.id}
                </p>
                <p className="text-sm text-gray-500">
                  ₹{order.totalAmount}
                </p>
              </div>

              <select
                value={order.status}
                onChange={(e) =>
                  handleStatusChange(
                    order.id,
                    e.target.value
                  )
                }
                className="border rounded px-3 py-1"
              >
                <option>PENDING</option>
                <option>PAID</option>
                <option>SHIPPED</option>
                <option>DELIVERED</option>
                <option>CANCELLED</option>
              </select>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
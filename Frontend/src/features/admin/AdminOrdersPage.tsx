import { useEffect, useState } from "react";
import api from "../../api/axios";
import toast from "react-hot-toast";

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchOrders = async () => {
    try {
      const res = await api.get("/admin/orders");
      setOrders(res.data.data);
    } catch {
      toast.error("Failed to load orders");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  // ================= UPDATE STATUS =================
  const updateStatus = async (id: number, status: string) => {
    try {
      await api.put(`/admin/orders/${id}/status?status=${status}`);
      toast.success("Status updated");
      fetchOrders();
    } catch {
      toast.error("Failed to update status");
    }
  };

  // ================= DELETE CONFIRM =================
  const handleDelete = async(id: number) => {
    const confirmDelete = window.confirm("Delete this order?");
    if (!confirmDelete) return;

    // OPTIONAL: if you add delete API later
    try {
  await api.delete(`/admin/orders/${id}`);
  toast.success("Order deleted");
  fetchOrders();
} catch {
  toast.error("Failed to delete");
}
  };

  if (loading) return <p>Loading orders...</p>;

  return (
    <div className="p-6 space-y-6">

      <h1 className="text-3xl font-bold">Orders Management</h1>

      {/* TABLE */}
      <div className="bg-white rounded shadow overflow-hidden">

        <table className="w-full text-sm">
          <thead className="bg-gray-200">
            <tr>
              <th className="p-3">Order ID</th>
              <th>User</th>
              <th>Total</th>
              <th>Status</th>
              <th>Payment</th>
              <th>Items</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {orders.map((o) => (
              <tr key={o.orderId} className="border-t text-center">

                <td className="p-3 font-semibold">{o.orderId}</td>

                <td>{o.userEmail}</td>

                <td>₹ {o.totalAmount}</td>

                {/* STATUS BADGE */}
                <td>
                  <span className={`px-2 py-1 rounded text-white text-xs
                    ${o.status === "CONFIRMED" && "bg-green-500"}
                    ${o.status === "PENDING_PAYMENT" && "bg-yellow-500"}
                    ${o.status === "CANCELLED" && "bg-red-500"}
                    ${o.status === "SHIPPED" && "bg-blue-500"}        // ✅ ADD
  ${o.status === "DELIVERED" && "bg-purple-500"}    // ✅ ADD
                  `}>
                    {o.status}
                  </span>
                </td>

                <td>{o.paymentMethod}</td>

                {/* ITEMS */}
                <td>
                  {o.items?.length || 0}
                </td>

                {/* ACTION */}
                <td className="space-x-2">

                  <select
                    className="border p-1 rounded"
                    onChange={(e) => updateStatus(o.orderId, e.target.value)}
                    defaultValue=""
                  >
                    <option value="" disabled>Update</option>
                    <option value="CONFIRMED">Confirm</option>
                    <option value="SHIPPED">Ship</option>
                    <option value="DELIVERED">Deliver</option>
                    <option value="CANCELLED">Cancel</option>
                  </select>

                  <button
                    onClick={() => handleDelete(o.orderId)}
                    className="bg-red-500 text-white px-2 py-1 rounded"
                  >
                    Delete
                  </button>

                </td>

              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </div>
  );
}
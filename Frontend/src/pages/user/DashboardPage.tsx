import { useEffect, useState } from "react";
import api from "../../api/axios";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function DashboardPage() {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [orders, setOrders] = useState<any[]>([]);
  const [cartCount, setCartCount] = useState(0);
  const [products, setProducts] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  // ================= LOAD DATA =================
  useEffect(() => {
    const loadData = async () => {
      try {
        const ordersRes = await api.get("/orders/my-orders"); 

// FIX THIS LINE
setOrders(ordersRes.data.data || []);

        const cartRes = await api.get("/cart");
setCartCount(cartRes.data.data?.items?.length || 0);

        const productRes = await api.get("/products/public");
        setProducts(productRes.data.data.content.slice(0, 4)); // recommended
      } catch (err) {
        console.log(err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  // ================= CALCULATIONS =================
  const totalOrders = orders.length;

  const totalSpent = orders.reduce(
    (sum, o) => sum + (o.totalAmount || 0),
    0
  );

  const delivered = orders.filter(o => o.status === "DELIVERED").length;
const pending = orders.filter(o => o.status === "PENDING_PAYMENT").length;  const cancelled = orders.filter(o => o.status === "CANCELLED").length;

  const recentOrders = orders.slice(0, 5);

  if (loading) return <p className="text-center">Loading dashboard...</p>;

  return (
    <div className="space-y-8">

      {/* 👤 USER CARD */}
      <div className="bg-gradient-to-r from-black to-gray-800 text-white p-6 rounded-2xl shadow flex justify-between">
        <div>
          <h2 className="text-2xl font-bold">
            Hello, {user?.email || "User"} 👋
          </h2>
          <p className="text-gray-300">{user?.email}</p>
        </div>
        <div className="text-sm text-gray-400">
          Premium Member ⭐
        </div>
      </div>

      {/* ⚡ QUICK ACTIONS */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">

        <button onClick={() => navigate("/products")}
          className="bg-white p-4 rounded-xl shadow hover:shadow-md">
          🛍 Browse Products
        </button>

        <button onClick={() => navigate("/cart")}
          className="bg-white p-4 rounded-xl shadow hover:shadow-md">
          🛒 View Cart
        </button>

        <button onClick={() => navigate("/orders")}
          className="bg-white p-4 rounded-xl shadow hover:shadow-md">
          📦 My Orders
        </button>

        <button className="bg-white p-4 rounded-xl shadow hover:shadow-md">
          ❤️ Wishlist (Soon)
        </button>

      </div>

      {/* 📊 STATS */}
      <div className="grid md:grid-cols-3 gap-6">

        <div className="bg-white p-6 rounded-xl shadow">
          <h3 className="text-gray-500 text-sm">Orders</h3>
          <p className="text-3xl font-bold">{totalOrders}</p>
        </div>

        <div className="bg-white p-6 rounded-xl shadow">
          <h3 className="text-gray-500 text-sm">Cart Items</h3>
          <p className="text-3xl font-bold">{cartCount}</p>
        </div>

        <div className="bg-white p-6 rounded-xl shadow">
          <h3 className="text-gray-500 text-sm">Total Spent</h3>
          <p className="text-3xl font-bold text-green-600">₹{totalSpent}</p>
        </div>

      </div>

      {/* 📦 ORDER STATUS */}
      <div className="grid md:grid-cols-3 gap-6">

        <div className="bg-green-100 p-5 rounded-xl text-center">
          <p className="text-green-700 font-semibold">Delivered</p>
          <p className="text-2xl font-bold">{delivered}</p>
        </div>

        <div className="bg-yellow-100 p-5 rounded-xl text-center">
          <p className="text-yellow-700 font-semibold">Pending</p>
          <p className="text-2xl font-bold">{pending}</p>
        </div>

        <div className="bg-red-100 p-5 rounded-xl text-center">
          <p className="text-red-700 font-semibold">Cancelled</p>
          <p className="text-2xl font-bold">{cancelled}</p>
        </div>

      </div>

      {/* 📦 RECENT ORDERS */}
      <div className="bg-white p-6 rounded-2xl shadow">

        <div className="flex justify-between mb-4">
          <h2 className="text-xl font-semibold">Recent Orders</h2>
          <button
            onClick={() => window.location.href = "/orders"}
            className="text-blue-600 text-sm hover:underline"
          >
            View All
          </button>
        </div>

        {recentOrders.length === 0 ? (
          <p className="text-gray-500">No orders yet</p>
        ) : (
          <div className="space-y-3">
            {recentOrders.map(order => (
              <div key={order.orderId}
                className="border p-4 rounded-xl flex justify-between items-center">

                <div>
                  <p className="font-medium">Order #{order.orderId}</p>
                  <p className="text-gray-500 text-sm">
  {order.orderDate
    ? new Date(order.orderDate).toLocaleDateString()
    : "N/A"}
</p>
                </div>

                <div className="text-right">
                  <p className="font-semibold text-green-600">
                    ₹{order.totalAmount}
                  </p>
                  <p className="text-sm text-gray-500">
                    {order.status}
                  </p>
                </div>

              </div>
            ))}
          </div>
        )}
      </div>

      {/* 🛍 RECOMMENDED PRODUCTS */}
      <div className="bg-white p-6 rounded-2xl shadow">

        <h2 className="text-xl font-semibold mb-4">
          Recommended for you
        </h2>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">

          {products.map(p => (
            <div key={p.id}
              onClick={() => window.location.href=`/product/${p.id}`}
              className="cursor-pointer border p-3 rounded-xl hover:shadow-md">

              <img src={p.imageUrl}
                className="h-32 w-full object-cover rounded mb-2" />

              <p className="text-sm font-medium line-clamp-2">
                {p.name}
              </p>

              <p className="text-green-600 font-bold">
                ₹{p.price}
              </p>

            </div>
          ))}

        </div>

      </div>

    </div>
  );
}
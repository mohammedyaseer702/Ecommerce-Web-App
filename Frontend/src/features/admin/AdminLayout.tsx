import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

export default function AdminLayout({ children }: any) {
  const navigate = useNavigate();
  const { logout } = useAuth();

  return (
    <div className="flex min-h-screen bg-gray-100">

      {/* SIDEBAR */}
      <div className="w-64 bg-black text-white p-6 flex flex-col justify-between">

        <div>
          <h2 className="text-2xl font-bold mb-8">🛒 Admin</h2>

          <div className="space-y-4">
            <p onClick={() => navigate("/admin")} className="cursor-pointer hover:text-gray-300">
              Dashboard
            </p>
            <p onClick={() => navigate("/admin/products")} className="cursor-pointer hover:text-gray-300">
              Products
            </p>
            <p onClick={() => navigate("/admin/orders")} className="cursor-pointer hover:text-gray-300">
              Orders
            </p>
          </div>
        </div>

        <button
          onClick={() => {
            logout();
            navigate("/");
          }}
          className="bg-red-500 p-2 rounded hover:bg-red-600"
        >
          Logout
        </button>
      </div>

      {/* MAIN */}
      <div className="flex-1">

        {/* TOPBAR */}
        <div className="bg-white shadow p-4 flex justify-between">
          <input
            placeholder="Search anything..."
            className="border p-2 w-1/3 rounded"
          />

          <div className="font-semibold">
            Admin
          </div>
        </div>

        {/* CONTENT */}
        <div className="p-6">
          {children}
        </div>

      </div>
    </div>
  );
}
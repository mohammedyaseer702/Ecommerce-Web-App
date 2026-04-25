import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { NavLink } from "react-router-dom";

export default function Sidebar() {
  const { user } = useAuth();

  return (
    <div className="w-64 bg-black text-white min-h-screen p-6">
      <h2 className="text-2xl font-bold mb-8">MyStore</h2>

      <nav className="space-y-4">

    

<NavLink
  to="/dashboard"
  className={({ isActive }) =>
    `block px-3 py-2 rounded ${
      isActive ? "bg-gray-800" : "hover:text-gray-300"
    }`
  }
>
  Dashboard
</NavLink>

        <Link to="/products" className="block hover:text-gray-300">
          Products
        </Link>

        <Link to="/orders" className="block hover:text-gray-300">
          My Orders
        </Link>

        <Link to="/cart" className="block hover:text-gray-300">
          Cart
        </Link>

        {/* ✅ ADMIN LINKS */}
        {user?.role === "ADMIN" && (
          <>
            <Link to="/admin" className="block hover:text-gray-300">
              Admin Panel
            </Link>

            <Link to="/admin/orders" className="block hover:text-gray-300">
              Admin Orders
            </Link>
          </>
        )}

      </nav>
    </div>
  );
}
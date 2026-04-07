import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div className="flex justify-between items-center bg-white shadow px-6 py-4">
      <div className="font-semibold">
        Welcome, {user?.email}
      </div>

      <button
        onClick={handleLogout}
        className="bg-black text-white px-4 py-2 rounded-lg hover:bg-gray-800"
      >
        Logout
      </button>
    </div>
  );
}

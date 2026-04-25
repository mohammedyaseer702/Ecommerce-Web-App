import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

interface Props {
  children: React.ReactNode;
  role?: string;
}

export default function ProtectedRoute({ children, role }: Props) {
  const { isAuthenticated, user } = useAuth();

  // 🔐 Not logged in
  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  // 🔒 Role check
  if (role && user?.role !== role) {
    console.log("ACCESS DENIED:", user?.role, "required:", role);
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
}
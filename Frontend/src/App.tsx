import './App.css'
import AppRoutes from './app/routes'
import { Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";
import { Toaster } from "react-hot-toast";

function App() {
  return (
    <>
      <Toaster position="top-right" />
      <AppRoutes />
    </>
  )
}

export default App;

export const AdminRoute = ({ children }: any) => {
  const { user } = useAuth();

  if (!user) return <Navigate to="/" />;

  if (user.role !== "ROLE_ADMIN") {
    return <div>Access Denied</div>;
  }

  return children;
};


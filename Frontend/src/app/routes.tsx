import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";
import LoginPage from "../features/auth/LoginPage";
import DashboardLayout from "../layouts/DashBoardLayout";
import OrdersPage from "../features/orders/OrdersPage";
import ProductsPage from "../features/products/ProductsPage";
import CartPage from "../features/cart/CartPage";
import PaymentSuccess from "../features/payment/PaymentSuccess";
import AdminOrdersPage from "../features/admin/AdminOrdersPage";


function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <DashboardLayout>
                <h1 className="text-2xl font-bold">
                  Dashboard Overview
                </h1>
              </DashboardLayout>
            </ProtectedRoute>
          }
        />
<Route path="/payment-success" element={<PaymentSuccess />} />

        <Route
  path="/cart"
  element={
    <ProtectedRoute>
      <DashboardLayout>
        <CartPage />
      </DashboardLayout>
    </ProtectedRoute>
  }
/>

<Route
  path="/admin/orders"
  element={
    <ProtectedRoute role="ADMIN">
      <DashboardLayout>
        <AdminOrdersPage />
      </DashboardLayout>
    </ProtectedRoute>
  }
/>






        <Route
  path="/orders"
  element={
    <ProtectedRoute>
      <DashboardLayout>
        <OrdersPage />
      </DashboardLayout>
    </ProtectedRoute>
  }
/>

<Route
  path="/products"
  element={
    <ProtectedRoute>
      <DashboardLayout>
        <ProductsPage />
      </DashboardLayout>
    </ProtectedRoute>
  }
/>



        <Route
          path="/admin"
          element={
            <ProtectedRoute role="ADMIN">
              <DashboardLayout>
                <h1 className="text-2xl font-bold">
                  Admin Panel
                </h1>
              </DashboardLayout>
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;

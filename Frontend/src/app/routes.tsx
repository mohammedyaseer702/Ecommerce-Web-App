import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";

import LoginPage from "../features/auth/LoginPage";
import RegisterPage from "../features/auth/RegisterPage";

import DashboardLayout from "../layouts/DashBoardLayout";
import ProductsPage from "../features/products/ProductsPage";
import OrdersPage from "../features/orders/OrdersPage";
import OrderDetailsPage from "../features/orders/OrderDetailPage";
import CartPage from "../features/cart/CartPage";
import PaymentSuccess from "../features/payment/PaymentSuccess";

import AdminDashboard from "../features/admin/AdminDashboard";
import AdminOrdersPage from "../features/admin/AdminOrdersPage";
import AdminProductsPage from "../features/admin/AdminProductsPage";
import ProductDetailsPage from "../features/products/ProductDetailsPage";
import DashboardPage from "../pages/user/DashBoardPage";


function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>

        {/* AUTH */}
        <Route path="/" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* USER */}
        <Route
  path="/dashboard"
  element={
    <ProtectedRoute>
      <DashboardLayout>
        <DashboardPage />
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
  path="/orders/:id"
  element={
    <ProtectedRoute>
      <DashboardLayout>
        <OrderDetailsPage />
      </DashboardLayout>
    </ProtectedRoute>
  }
/>

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

        <Route path="/payment-success" element={<PaymentSuccess />} />

        <Route path="/product/:id" element={<ProductDetailsPage />} />

        {/* ADMIN */}
        <Route
  path="/admin"
  element={
    <ProtectedRoute role="ADMIN">
      <AdminDashboard />
    </ProtectedRoute>
  }
/>




<Route
  path="/admin/products"
  element={
    <ProtectedRoute role="ADMIN">
      <AdminProductsPage />
    </ProtectedRoute>
  }
/>




        <Route
          path="/admin/orders"
          element={
            <ProtectedRoute role="ADMIN">
              <AdminOrdersPage />
            </ProtectedRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}

export default AppRoutes;
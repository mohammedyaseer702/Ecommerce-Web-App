import { useEffect } from "react";
import { useNavigate , useSearchParams } from "react-router-dom"; // ✅ ADD THIS
import api from "../../utils/api";


export default function PaymentSuccess() {
  useSearchParams();
  const navigate = useNavigate(); // ✅ ADD THIS

  useEffect(() => {
    const orderId = localStorage.getItem("orderId");

    if (!orderId) {
      console.error("Order ID missing");
      navigate("/orders"); // ✅ fallback
      return;
    }

    api.post("/orders/confirm-payment", {
      orderId: Number(orderId),
      transactionId: "stripe_success",
      success: true
    })
    .then(() => {
      localStorage.removeItem("orderId");

      // ✅ FIX: navigate properly
      navigate("/orders");
    })
    .catch((err) => {
      console.error("Payment confirm error:", err);
      navigate("/orders"); // still go to orders
    });

  }, []);

  return <h1>Payment Successful</h1>;
}

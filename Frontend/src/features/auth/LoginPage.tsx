import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginRequest } from "./authApi";
import { useAuth } from "../../context/AuthContext";
import { jwtDecode } from "jwt-decode";

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const res = await loginRequest({ email, password });

      const token = res.data.accessToken;

      // ✅ save token
      login(token);

      // ✅ decode token
      const decoded: any = jwtDecode(token);

      console.log("DECODED:", decoded); // 🔥 debug

      // ✅ FIXED ROLE CHECK
      if (decoded.role === "ADMIN") {
        navigate("/admin");
      } else {
        navigate("/dashboard");
      }
      

    } catch (err: any) {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
    
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white shadow-xl rounded-2xl p-10 w-full max-w-md">
        <h2 className="text-3xl font-bold text-center mb-8">
          Welcome Back
        </h2>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block mb-1 text-sm font-medium">
              Email
            </label>
            <input
              type="email"
              required
              className="w-full border rounded-lg p-3"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium">
              Password
            </label>
            <input
              type="password"
              required
              className="w-full border rounded-lg p-3"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>

          {error && (
            <div className="text-red-500 text-sm">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-black text-white p-3 rounded-lg"
          >
            {loading ? "Signing in..." : "Sign In"}
          </button>

          <p className="text-sm text-center mt-4">
            Don't have an account?{" "}
            <span
              className="text-blue-500 cursor-pointer"
              onClick={() => navigate("/register")}
            >
              Register
            </span>
          </p>

        </form>
      </div>
    </div>
  );
}
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/axios";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    password: "",
  });

  const [error, setError] = useState("");

  // ✅ handle input change
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  // ✅ handle submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      await api.post("/auth/register", form);

      // ✅ redirect after success
      navigate("/");
    } catch (err: any) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white shadow-xl rounded-2xl p-10 w-full max-w-md">
        <h2 className="text-3xl font-bold text-center mb-8">
          Register
        </h2>

        <form onSubmit={handleSubmit} className="space-y-5">

          <div>
            <label className="block mb-1 text-sm font-medium">Name</label>
            <input
              type="text"
              name="name"
              required
              className="w-full border rounded-lg p-3"
              value={form.name}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium">Email</label>
            <input
              type="email"
              name="email"
              required
              className="w-full border rounded-lg p-3"
              value={form.email}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium">Phone</label>
            <input
              type="text"
              name="phone"
              required
              className="w-full border rounded-lg p-3"
              value={form.phone}
              onChange={handleChange}
            />
          </div>

          <div>
            <label className="block mb-1 text-sm font-medium">Password</label>
            <input
              type="password"
              name="password"
              required
              className="w-full border rounded-lg p-3"
              value={form.password}
              onChange={handleChange}
            />
          </div>

          {error && (
            <div className="text-red-500 text-sm">
              {error}
            </div>
          )}

          <button
            type="submit"
            className="w-full bg-black text-white p-3 rounded-lg hover:bg-gray-800"
          >
            Register
          </button>
        </form>
      </div>
    </div>
  );
}
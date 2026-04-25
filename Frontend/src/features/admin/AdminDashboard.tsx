import AdminLayout from "./AdminLayout";
import {
  LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer
} from "recharts";

export default function AdminDashboard() {

  const data = [
    { name: "Jan", sales: 400 },
    { name: "Feb", sales: 800 },
    { name: "Mar", sales: 600 },
    { name: "Apr", sales: 1200 },
  ];

  return (
    <AdminLayout>

      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>

      {/* STATS */}
      <div className="grid grid-cols-4 gap-4 mb-6">
        <div className="bg-blue-500 text-white p-4 rounded shadow">
          Revenue ₹50,000
        </div>
        <div className="bg-green-500 text-white p-4 rounded shadow">
          Orders 120
        </div>
        <div className="bg-yellow-500 text-white p-4 rounded shadow">
          Users 80
        </div>
        <div className="bg-red-500 text-white p-4 rounded shadow">
          Pending 12
        </div>
      </div>

      {/* CHART */}
      <div className="bg-white p-6 rounded shadow">
        <h2 className="mb-4 font-bold">Sales Overview</h2>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={data}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="sales" />
          </LineChart>
        </ResponsiveContainer>
      </div>

    </AdminLayout>
  );
}
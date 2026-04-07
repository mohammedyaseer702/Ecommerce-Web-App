import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";

interface Props {
  children: React.ReactNode;
}

export default function DashboardLayout({ children }: Props) {
  return (
    <div className="flex">
      <Sidebar />

      <div className="flex-1 bg-gray-100 min-h-screen">
        <Navbar />
        <div className="p-6">{children}</div>
      </div>
    </div>
  );
}

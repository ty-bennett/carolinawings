import { useState, useEffect } from "react";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import Sidebar from "../components/Sidebar";
import { useNavigate } from "react-router-dom";
import MenuDetails from "./MenuDetails";

function RestaurantDashboard() {
  const [activeTab, setActiveTab] = useState("home");
  const [orders, setOrders] = useState([]);
  const [hours, setHours] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchOrders = async () => {
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const res = await axios.get("http://localhost:8080/admin/restaurants/1/orders", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders(res.data.content || []);
    } catch (err) {
      console.error(err);
      setError("Failed to load orders.");
    }
  };

  const getHours = () => {
    const day = new Date().getDay();
    return day === 5 || day === 6 ? "11:00 - 11:00" : "11:00 - 10:00";
  };

  

  const handleDeleteMenu = async (menuId)  => {
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const res = await axios.delete(`http://localhost:8080/admin/restaurants/1/menus/${menuId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      console.log(res.data);
      console.log(res.status);
      navigate("/admin/restaurants/menus");
    } catch (err) {
      console.error(err);
      setError("Failed to load menus.");
    }
  }

  return (
    <>
      <NavBar />
      <div className="flex min-h-screen">
        <Sidebar onSelect={setActiveTab} />
        <div className="w-5/6 p-6 bg-gray-200">
          {/* Welcome + Error */}
          <div className="flex justify-between items-end mb-4">
            <h1 className="text-2xl font-bold capitalize">{activeTab}</h1>
          </div>
          {error && <p className="text-red-500 mb-4">{error}</p>}

          {/* Home Dashboard Tiles */}
          {activeTab === "home" && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div
                onClick={() => navigate("/admin/restaurants/dashboard/menus")}
                className="cursor-pointer bg-white p-6 rounded-xl shadow-md hover:shadow-lg border hover:scale-101 transition-transform"
              >
                <h2 className="text-xl font-semibold text-center">View Menus</h2>
              </div>

              <div
                onClick={() => setActiveTab("orders")}
                className="cursor-pointer bg-white p-6 rounded-xl shadow-md hover:shadow-lg border hover:scale-101 transition-transform"
              >
                <h2 className="text-xl font-semibold text-center">View Orders</h2>
              </div>

              <div
                onClick={() => navigate("/admin/users")}
                className="cursor-pointer bg-white p-6 rounded-xl shadow-md hover:shadow-lg border hover:scale-101 transition-transform"
              >
                <h2 className="text-xl font-semibold text-center">Manage Users</h2>
              </div>

              <div
                onClick={() => navigate("/admin/sales")}
                className="cursor-pointer bg-white p-6 rounded-xl shadow-md hover:shadow-lg border hover:scale-101 transition-transform"
              >
                <h2 className="text-xl font-semibold text-center">View Sales</h2>
              </div>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default RestaurantDashboard;

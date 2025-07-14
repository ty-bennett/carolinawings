import { useState, useEffect } from "react";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import Sidebar from "../components/Sidebar";
import { useNavigate } from "react-router-dom";
import MenuDetails from "./MenuDetails";

function RestaurantDashboard() {
  const [activeTab, setActiveTab] = useState("home");
  const [menus, setMenus] = useState([]);
  const [orders, setOrders] = useState([]);
  const [hours, setHours] = useState("");
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchMenus = async () => {
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const restaurant = localStorage.getItem("restaurants");
      const res = await axios.get(`http://localhost:8080/admin/restaurants/${restaurant}/menus`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMenus(res.data.content || []);
    } catch (err) {
      console.error(err);
      setError("Failed to load menus.");
    }
  };

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

  useEffect(() => {
    if (activeTab === "menus") fetchMenus();
    if (activeTab === "orders") fetchOrders();
    if (activeTab === "hours") setHours(getHours());
  }, [activeTab]);

  const handleMenuClick = (restaurantId) => {
    navigate("/admin/restaurants/menus");
    fetchMenus();
  };

  return (
    <>
      <NavBar />
      <div className="flex min-h-screen">
        <Sidebar onSelect={setActiveTab} />
        <div className="w-5/6 p-6 bg-gray-200">
          {/* Welcome + Error */}
          <div className="flex justify-between items-center mb-4">
            <h1 className="text-2xl font-bold capitalize">{activeTab}</h1>
          </div>
          {error && <p className="text-red-500 mb-4">{error}</p>}

          {/* Home Dashboard Tiles */}
          {activeTab === "home" && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div
                onClick={() => setActiveTab("menus")}
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

          {/* Menus Section */}
          {activeTab === "menus" && (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {menus.map((menu) => (
                <div
                  key={menu.id}
                  className="bg-white p-4 rounded-lg shadow-md border hover:scale-101 transition-transform cursor-pointer"
                  onClick={handleMenuClick(menu.id)}
                >
                  <h2 className="text-xl font-semibold">{menu.name}</h2>
                  <p className="text-gray-600">{menu.description}</p>
                </div>
              ))}
            </div>
          )}

          {/* Orders Section */}
          {activeTab === "orders" && (
            <div>
              {orders.map((order, index) => (
                <div key={index} className="bg-white p-4 rounded shadow mb-4">
                  <p>Order #{order.id}</p>
                </div>
              ))}
            </div>
          )}

          {/* Hours Section */}
          {activeTab === "hours" && (
            <div className="bg-white p-6 rounded shadow">
              <p className="text-xl font-medium">Today's Hours: {hours}</p>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default RestaurantDashboard;

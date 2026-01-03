import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import { adminAPI, Order } from "../services/api";
import { useAuth } from "../context/AuthContext";

function AdminDashboard() {
  const [activeTab, setActiveTab] = useState("orders");
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [selectedRestaurant] = useState<number>(1);
  const navigate = useNavigate();
  const { user } = useAuth();

  // Fetch orders when tab changes to orders
  useEffect(() => {
    if (activeTab === "orders") {
      fetchOrders();
    }
  }, [activeTab, selectedRestaurant]);

  const fetchOrders = async () => {
    setLoading(true);
    setError("");
    try {
      const { data } = await adminAPI.getOrders(selectedRestaurant, 0, 50);
      setOrders(data.content || []);
    } catch (err) {
      console.error(err);
      setError("Failed to load orders.");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateStatus = async (orderId: string, newStatus: string) => {
    try {
      await adminAPI.updateOrderStatus(orderId, newStatus);
      // Update local state
      setOrders(orders.map(order =>
        order.orderId === orderId
          ? { ...order, status: newStatus }
          : order
      ));
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || "Failed to update order status");
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800 border-yellow-300';
      case 'PREPARING': return 'bg-blue-100 text-blue-800 border-blue-300';
      case 'READY': return 'bg-green-100 text-green-800 border-green-300';
      case 'COMPLETED': return 'bg-gray-100 text-gray-800 border-gray-300';
      case 'CANCELLED': return 'bg-red-100 text-red-800 border-red-300';
      default: return 'bg-gray-100 text-gray-800 border-gray-300';
    }
  };

  const getNextStatus = (currentStatus: string): string | null => {
    switch (currentStatus) {
      case 'PENDING': return 'PREPARING';
      case 'PREPARING': return 'READY';
      case 'READY': return 'COMPLETED';
      default: return null;
    }
  };

  const formatTime = (isoString: string) => {
    return new Date(isoString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  return (
    <>
      <NavBar />
      <div className="flex min-h-screen">
        {/* Sidebar */}
        <div className="w-64 bg-darkred text-white">
          <div className="p-4 border-b border-red-800">
            <h2 className="text-xl font-bold">Admin Panel</h2>
            <p className="text-sm text-gray-300 mt-1">{user?.name}</p>
          </div>
          <nav className="p-4">
            <button
              onClick={() => setActiveTab("orders")}
              className={`w-full text-left px-4 py-3 rounded-lg mb-2 transition ${activeTab === "orders"
                ? "bg-white text-darkred font-semibold"
                : "hover:bg-red-800"
                }`}
            >
              📋 Orders
            </button>
            <button
              onClick={() => navigate("/admin/restaurants/dashboard/menus")}
              className="w-full text-left px-4 py-3 rounded-lg mb-2 hover:bg-red-800 transition"
            >
              🍔 Menus
            </button>
            <button
              onClick={() => setActiveTab("settings")}
              className={`w-full text-left px-4 py-3 rounded-lg mb-2 transition ${activeTab === "settings"
                ? "bg-white text-darkred font-semibold"
                : "hover:bg-red-800"
                }`}
            >
              ⚙️ Settings
            </button>
          </nav>
        </div>

        {/* Main Content */}
        <div className="flex-1 bg-gray-100 p-6">
          {error && (
            <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
              {error}
            </div>
          )}

          {/* Orders Tab */}
          {activeTab === "orders" && (
            <div>
              <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Order Management</h1>
                <button
                  onClick={fetchOrders}
                  className="bg-darkred text-white px-4 py-2 rounded-lg hover:bg-red-800 transition"
                >
                  🔄 Refresh
                </button>
              </div>

              {/* Order Status Filters */}
              <div className="flex gap-2 mb-6">
                {['ALL', 'PENDING', 'PREPARING', 'READY', 'COMPLETED'].map(status => (
                  <button
                    key={status}
                    className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50 transition"
                  >
                    {status}
                  </button>
                ))}
              </div>

              {loading ? (
                <p className="text-center py-8">Loading orders...</p>
              ) : orders.length === 0 ? (
                <div className="bg-white rounded-lg shadow p-8 text-center">
                  <p className="text-gray-500">No orders found</p>
                </div>
              ) : (
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {orders.map(order => (
                    <div
                      key={order.orderId}
                      className={`bg-white rounded-lg shadow-md border-l-4 ${getStatusColor(order.status)}`}
                    >
                      {/* Order Header */}
                      <div className="p-4 border-b">
                        <div className="flex justify-between items-start">
                          <div>
                            <h3 className="font-bold text-lg">#{order.orderId?.slice(0, 8)}</h3>
                            <p className="text-sm text-gray-500">{order.customerName}</p>
                          </div>
                          <span className={`px-2 py-1 rounded text-xs font-medium ${getStatusColor(order.status)}`}>
                            {order.status}
                          </span>
                        </div>
                        <div className="mt-2 flex justify-between text-sm text-gray-600">
                          <span>📞 {order.customerPhone}</span>
                          <span>🕐 Pickup: {formatTime(order.pickupTime)}</span>
                        </div>
                      </div>

                      {/* Order Items */}
                      <div className="p-4 border-b max-h-40 overflow-y-auto">
                        {order.items?.map((item, idx) => (
                          <div key={idx} className="mb-2">
                            <p className="font-medium">{item.quantity}x {item.menuItemName}</p>
                            {item.options && item.options.length > 0 && (
                              <p className="text-xs text-gray-500 ml-4">
                                → {item.options.map(opt => opt.optionName).join(', ')}
                              </p>
                            )}
                          </div>
                        ))}
                        {order.customerNotes && (
                          <p className="text-sm text-orange-600 mt-2">
                            📝 {order.customerNotes}
                          </p>
                        )}
                      </div>

                      {/* Order Footer */}
                      <div className="p-4">
                        <div className="flex justify-between items-center mb-3">
                          <span className="font-bold text-lg">${order.totalPrice?.toFixed(2)}</span>
                          <span className="text-xs text-gray-500">
                            {new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                          </span>
                        </div>

                        {/* Action Buttons */}
                        <div className="flex gap-2">
                          {getNextStatus(order.status) && (
                            <button
                              onClick={() => handleUpdateStatus(order.orderId, getNextStatus(order.status)!)}
                              className="flex-1 bg-darkred text-white py-2 rounded-lg hover:bg-red-800 transition text-sm font-medium"
                            >
                              Mark {getNextStatus(order.status)}
                            </button>
                          )}
                          {order.status === 'PENDING' && (
                            <button
                              onClick={() => handleUpdateStatus(order.orderId, 'CANCELLED')}
                              className="px-3 py-2 border border-red-500 text-red-500 rounded-lg hover:bg-red-50 transition text-sm"
                            >
                              Cancel
                            </button>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          )}

          {/* Settings Tab */}
          {activeTab === "settings" && (
            <div>
              <h1 className="text-2xl font-bold mb-6">Restaurant Settings</h1>
              <div className="bg-white rounded-lg shadow p-6">
                <p className="text-gray-500">Settings coming soon...</p>
              </div>
            </div>
          )}
        </div>
      </div>
      <Footer />
    </>
  );
}

export default AdminDashboard;

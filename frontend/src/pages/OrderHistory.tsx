import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { orderAPI, Order } from '../services/api';

function OrderHistory() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const { data } = await orderAPI.getUserOrderHistory();
        setOrders(data.content || []);
      } catch (err) {
        console.error(err);
        setError('Failed to load order history');
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-100 text-yellow-800';
      case 'PREPARING': return 'bg-blue-100 text-blue-800';
      case 'READY': return 'bg-green-100 text-green-800';
      case 'COMPLETED': return 'bg-gray-100 text-gray-800';
      case 'CANCELLED': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  if (loading) {
    return (
      <>
        <NavBar />
        <main className="min-h-screen bg-gray-100 flex items-center justify-center">
          <p className="text-xl">Loading your orders...</p>
        </main>
        <Footer />
      </>
    );
  }

  const handleCancelOrder = async (orderId: string) => {
    if (!window.confirm('Are you sure you want to cancel this order?')) {
      return;
    }

    try {
      await orderAPI.cancelOrder(orderId);
      // Refresh orders
      setOrders(orders.map(order =>
        order.orderId === orderId
          ? { ...order, status: 'CANCELLED' }
          : order
      ));
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || 'Failed to cancel order');
    }
  };

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100 py-8 px-4">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold mb-8">Order History</h1>

          {error && (
            <p className="text-red-500 mb-4">{error}</p>
          )}

          {orders.length === 0 ? (
            <div className="bg-white rounded-lg shadow p-8 text-center">
              <p className="text-gray-500 mb-4">You haven't placed any orders yet.</p>
              <button
                onClick={() => navigate('/order')}
                className="bg-darkred text-white px-6 py-2 rounded-lg hover:bg-red-800 transition cursor-pointer border-none"
              >
                Start Ordering
              </button>
            </div>
          ) : (
            <div className="space-y-4">
              {orders.map((order) => (
                <div
                  key={order.orderId}
                  className="bg-white rounded-lg shadow p-6 hover:shadow-md transition cursor-pointer"
                  onClick={() => navigate(`/orders/${order.orderId}`)}
                >
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h2 className="text-lg font-bold">Order #{order.orderId?.slice(0, 8)}</h2>
                      <p className="text-gray-500">{order.restaurantName}</p>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                      {order.status}
                    </span>
                  </div>

                  <div className="flex justify-between items-center text-sm text-gray-600 mb-4">
                    <span>
                      {new Date(order.createdAt).toLocaleDateString()} at{' '}
                      {new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </span>
                    <span className="font-semibold text-darkred">${order.totalPrice?.toFixed(2)}</span>
                  </div>

                  <div className="border-t pt-3">
                    <p className="text-sm text-gray-500">
                      {order.items?.map(item => `${item.quantity}x ${item.menuItemName}`).join(', ')}
                    </p>
                  </div>

                  {order.status === 'PENDING' && (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleCancelOrder(order.orderId);
                      }}
                      className="mt-4 text-red-500 text-sm hover:underline bg-transparent border-none cursor-pointer"
                    >
                      Cancel Order
                    </button>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
      <Footer />
    </>
  );
}

export default OrderHistory;

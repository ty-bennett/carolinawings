import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { orderAPI, Order } from '../services/api';

function OrderDetails() {
  const { orderId } = useParams<{ orderId: string }>();
  const navigate = useNavigate();
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchOrder = async () => {
      if (!orderId) return;

      try {
        const { data } = await orderAPI.getOrder(orderId);
        setOrder(data);
      } catch (err) {
        console.error(err);
        setError('Failed to load order details');
      } finally {
        setLoading(false);
      }
    };

    fetchOrder();
  }, [orderId]);

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

  const handleCancelOrder = async () => {
    if (!order || !window.confirm('Are you sure you want to cancel this order?')) {
      return;
    }

    try {
      await orderAPI.cancelOrder(order.orderId);
      setOrder({ ...order, status: 'CANCELLED' });
    } catch (err: any) {
      console.error(err);
      alert(err.response?.data?.message || 'Failed to cancel order');
    }
  };

  if (loading) {
    return (
      <>
        <NavBar />
        <main className="min-h-screen bg-gray-100 flex items-center justify-center">
          <p className="text-xl">Loading order details...</p>
        </main>
        <Footer />
      </>
    );
  }

  if (error || !order) {
    return (
      <>
        <NavBar />
        <main className="min-h-screen bg-gray-100 py-12 px-4">
          <div className="max-w-2xl mx-auto text-center">
            <h1 className="text-2xl font-bold mb-4 text-red-500">{error || 'Order not found'}</h1>
            <button
              onClick={() => navigate('/orders')}
              className="bg-darkred text-white px-6 py-2 rounded-lg hover:bg-red-800 transition cursor-pointer border-none"
            >
              Back to Orders
            </button>
          </div>
        </main>
        <Footer />
      </>
    );
  }

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100 py-8 px-4">
        <div className="max-w-3xl mx-auto">
          {/* Back Button */}
          <button
            onClick={() => navigate('/orders')}
            className="mb-6 text-darkred hover:underline bg-transparent border-none cursor-pointer flex items-center gap-2"
          >
            ← Back to Order History
          </button>

          {/* Order Header */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <div className="flex justify-between items-start mb-4">
              <div>
                <h1 className="text-2xl font-bold">Order #{order.orderId?.slice(0, 8)}</h1>
                <p className="text-gray-500 mt-1">{order.restaurantName}</p>
              </div>
              <span className={`px-4 py-2 rounded-full text-sm font-medium ${getStatusColor(order.status)}`}>
                {order.status}
              </span>
            </div>

            <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
              <div>
                <p className="text-gray-500">Order Date</p>
                <p className="font-medium">
                  {new Date(order.createdAt).toLocaleDateString()}
                </p>
              </div>
              <div>
                <p className="text-gray-500">Order Time</p>
                <p className="font-medium">
                  {new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
              </div>
              <div>
                <p className="text-gray-500">Pickup Time</p>
                <p className="font-medium">
                  {new Date(order.pickupTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                </p>
              </div>
              <div>
                <p className="text-gray-500">Order Type</p>
                <p className="font-medium">{order.orderType}</p>
              </div>
            </div>
          </div>

          {/* Customer Info */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h2 className="text-lg font-bold mb-4">Customer Information</h2>
            <div className="grid grid-cols-2 gap-4 text-sm">
              <div>
                <p className="text-gray-500">Name</p>
                <p className="font-medium">{order.customerName}</p>
              </div>
              <div>
                <p className="text-gray-500">Phone</p>
                <p className="font-medium">{order.customerPhone}</p>
              </div>
            </div>
            {order.customerNotes && (
              <div className="mt-4">
                <p className="text-gray-500">Special Instructions</p>
                <p className="font-medium">{order.customerNotes}</p>
              </div>
            )}
          </div>

          {/* Order Items */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h2 className="text-lg font-bold mb-4">Order Items</h2>
            <div className="space-y-4">
              {order.items?.map((item) => (
                <div key={item.id} className="flex justify-between items-start pb-4 border-b last:border-b-0 last:pb-0">
                  <div>
                    <p className="font-medium">{item.quantity}x {item.menuItemName}</p>
                    {item.options && item.options.length > 0 && (
                      <p className="text-sm text-gray-500 mt-1">
                        {item.options.map(opt => opt.optionName).join(', ')}
                      </p>
                    )}
                  </div>
                  <p className="font-medium">${item.lineTotal?.toFixed(2)}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Order Summary */}
          <div className="bg-white rounded-lg shadow p-6 mb-6">
            <h2 className="text-lg font-bold mb-4">Order Summary</h2>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-500">Subtotal</span>
                <span>${order.subtotal?.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-500">Tax</span>
                <span>${order.totalTax?.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-lg font-bold pt-2 border-t mt-2">
                <span>Total</span>
                <span className="text-darkred">${order.totalPrice?.toFixed(2)}</span>
              </div>
            </div>
          </div>

          {/* Actions */}
          {order.status === 'PENDING' && (
            <div className="text-center">
              <button
                onClick={handleCancelOrder}
                className="bg-red-500 text-white px-6 py-3 rounded-lg hover:bg-red-600 transition cursor-pointer border-none"
              >
                Cancel Order
              </button>
            </div>
          )}
        </div>
      </main>
      <Footer />
    </>
  );
}

export default OrderDetails;

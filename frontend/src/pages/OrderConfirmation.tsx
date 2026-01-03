import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { orderAPI, Order } from '../services/api';

function OrderConfirmation() {
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
              onClick={() => navigate('/')}
              className="bg-darkred text-white px-6 py-2 rounded-lg hover:bg-red-800 transition cursor-pointer"
            >
              Go Home
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
      <main className="min-h-screen bg-gray-100 py-12 px-4">
        <div className="max-w-2xl mx-auto">
          {/* Success Header */}
          <div className="bg-green-100 border border-green-400 rounded-lg p-6 mb-8 text-center">
            <div className="text-green-600 text-5xl mb-4">✓</div>
            <h1 className="text-2xl font-bold text-green-800 mb-2">Order Placed Successfully!</h1>
            <p className="text-green-700">Your order has been sent to the kitchen.</p>
          </div>

          {/* Order Details */}
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex justify-between items-start mb-6">
              <div>
                <h2 className="text-xl font-bold">Order #{order?.orderId?.slice(0, 8)}</h2>
                <p className="text-gray-500">{order.restaurantName}</p>
              </div>
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${order.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                order.status === 'PREPARING' ? 'bg-blue-100 text-blue-800' :
                  order.status === 'READY' ? 'bg-green-100 text-green-800' :
                    order.status === 'COMPLETED' ? 'bg-gray-100 text-gray-800' :
                      'bg-red-100 text-red-800'
                }`}>
                {order.status}
              </span>
            </div>

            <div className="grid grid-cols-2 gap-4 mb-6 pb-6 border-b">
              <div>
                <p className="text-sm text-gray-500">Customer</p>
                <p className="font-medium">{order.customerName}</p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Phone</p>
                <p className="font-medium">{order.customerPhone}</p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Pickup Time</p>
                <p className="font-medium">
                  {new Date(order.pickupTime).toLocaleTimeString([], {
                    hour: '2-digit',
                    minute: '2-digit'
                  })}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-500">Order Type</p>
                <p className="font-medium">{order.orderType}</p>
              </div>
            </div>

            {/* Order Items */}
            <h3 className="font-bold mb-4">Items</h3>
            <div className="space-y-3 mb-6">
              {order.items.map((item) => (
                <div key={item.id} className="flex justify-between">
                  <div>
                    <p className="font-medium">{item.quantity}x {item.menuItemName}</p>
                    {item.options && item.options.length > 0 && (
                      <p className="text-sm text-gray-500">
                        {item.options.map(opt => opt.optionName).join(', ')}
                      </p>
                    )}
                  </div>
                  <p className="font-medium">${item.lineTotal.toFixed(2)}</p>
                </div>
              ))}
            </div>

            {/* Totals */}
            <div className="border-t pt-4 space-y-2">
              <div className="flex justify-between">
                <span>Subtotal</span>
                <span>${order.subtotal.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span>Tax</span>
                <span>${order.totalTax.toFixed(2)}</span>
              </div>
              <div className="flex justify-between text-xl font-bold pt-2 border-t">
                <span>Total</span>
                <span className="text-darkred">${order.totalPrice.toFixed(2)}</span>
              </div>
            </div>

            {order.customerNotes && (
              <div className="mt-6 p-3 bg-gray-50 rounded">
                <p className="text-sm text-gray-500">Special Instructions</p>
                <p>{order.customerNotes}</p>
              </div>
            )}
          </div>

          <div className="mt-8 text-center">
            <button
              onClick={() => navigate('/order')}
              className="bg-darkred text-white px-8 py-3 rounded-lg font-semibold hover:bg-red-800 transition cursor-pointer border-none"
            >
              Place Another Order
            </button>
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
}

export default OrderConfirmation;

import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { orderAPI, CreateOrderRequest } from '../services/api';

function Checkout() {
  const navigate = useNavigate();
  const { cart } = useCart();
  const { user } = useAuth();

  const [customerName, setCustomerName] = useState(user?.name || '');
  const [customerPhone, setCustomerPhone] = useState('');
  const [customerNotes, setCustomerNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Get restaurant ID from first cart item
  const restaurantId = Number(localStorage.getItem('selectedRestaurantId'));
  const restaurantName = localStorage.getItem('selectedRestaurantName');

  const handlePlaceOrder = async () => {

    if (!restaurantId) {
      setError('Restaurant not selected. Please start a new order.');
      return;
    }

    if (!cart || cart.cartItems.length === 0) {
      setError('Your cart is empty');
      return;
    }

    if (!customerName.trim()) {
      setError('Please enter your name');
      return;
    }

    if (!customerPhone.trim()) {
      setError('Please enter your phone number');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const request: CreateOrderRequest = {
        cartId: cart.cartId,
        restaurantId: restaurantId!,
        customerName: customerName.trim(),
        customerPhone: customerPhone.trim(),
        customerNotes: customerNotes.trim() || undefined,
      };

      const { data: order } = await orderAPI.createOrder(request);
      console.log('Order response:', order);
      // Clear cart and navigate to confirmation
      navigate(`/order-confirmation/${order.orderId}`);
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to place order');
    } finally {
      setLoading(false);
    }
  };

  if (!cart || cart.cartItems.length === 0) {
    return (
      <>
        <NavBar />
        <main className="min-h-screen bg-gray-100 py-12 px-4">
          <div className="max-w-2xl mx-auto text-center">
            <h1 className="text-2xl font-bold mb-4">Your cart is empty</h1>
            <button
              onClick={() => navigate('/order')}
              className="bg-darkred text-white px-6 py-2 rounded-lg hover:bg-red-800 transition cursor-pointer"
            >
              Start Ordering
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
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold mb-8">Checkout</h1>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
            {/* Left Column - Customer Info */}
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-xl font-bold mb-4">Pickup Information</h2>

              {restaurantName && (
                <div className="mb-4 p-3 bg-gray-50 rounded">
                  <p className="text-sm text-gray-500">Pickup Location</p>
                  <p className="font-semibold">{restaurantName}</p>
                </div>
              )}

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Name *</label>
                  <input
                    type="text"
                    value={customerName}
                    onChange={(e) => setCustomerName(e.target.value)}
                    className="w-full border rounded-lg p-3"
                    placeholder="Your name"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">Phone Number *</label>
                  <input
                    type="tel"
                    value={customerPhone}
                    onChange={(e) => setCustomerPhone(e.target.value)}
                    className="w-full border rounded-lg p-3"
                    placeholder="123-456-7890"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">Special Instructions</label>
                  <textarea
                    value={customerNotes}
                    onChange={(e) => setCustomerNotes(e.target.value)}
                    className="w-full border rounded-lg p-3 h-24 resize-none"
                    placeholder="Any special requests for your order?"
                  />
                </div>
              </div>
            </div>

            {/* Right Column - Order Summary */}
            <div className="bg-white rounded-lg shadow p-6">
              <h2 className="text-xl font-bold mb-4">Order Summary</h2>

              <div className="space-y-4 mb-6">
                {cart.cartItems.map((item) => (
                  <div key={item.cartItemId} className="flex justify-between pb-3 border-b">
                    <div>
                      <p className="font-medium">
                        {item.quantity}x {item.menuItem.name}
                      </p>
                      {item.options && item.options.length > 0 && (
                        <p className="text-sm text-gray-500">
                          {item.options.map(opt => opt.optionName).join(', ')}
                        </p>
                      )}
                      {item.memos && (
                        <p className="text-sm text-gray-400 italic">{item.memos}</p>
                      )}
                    </div>
                    <p className="font-medium">${item.price.toFixed(2)}</p>
                  </div>
                ))}
              </div>

              <div className="space-y-2 border-t pt-4">
                <div className="flex justify-between">
                  <span>Subtotal</span>
                  <span>${cart.totalPrice.toFixed(2)}</span>
                </div>
                <div className="flex justify-between">
                  <span>Tax</span>
                  <span>$0.00</span>
                </div>
                <div className="flex justify-between text-xl font-bold pt-2 border-t">
                  <span>Total</span>
                  <span className="text-darkred">${cart.totalPrice.toFixed(2)}</span>
                </div>
              </div>

              {error && (
                <p className="text-red-500 mt-4">{error}</p>
              )}

              <button
                onClick={handlePlaceOrder}
                disabled={loading}
                className="w-full mt-6 bg-darkred text-white py-4 rounded-lg font-semibold hover:bg-red-800 transition disabled:opacity-50 cursor-pointer border-none text-lg"
              >
                {loading ? 'Placing Order...' : 'Place Order'}
              </button>

              <p className="text-center text-sm text-gray-500 mt-4">
                Payment will be collected at pickup
              </p>
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
}

export default Checkout;

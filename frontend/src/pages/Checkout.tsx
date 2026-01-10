import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { orderAPI, CreateOrderRequest } from '../services/api';

function Checkout() {
  const navigate = useNavigate();
  const { cart, resetCart } = useCart();
  const { user } = useAuth();

  const [customerName, setCustomerName] = useState(user?.name || '');
  const [customerPhone, setCustomerPhone] = useState('');
  const [customerNotes, setCustomerNotes] = useState('');
  const [pickupTime, setPickupTime] = useState('');
  const [pickupOptions, setPickupOptions] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showOrderSummary, setShowOrderSummary] = useState(false);

  const restaurantId = Number(localStorage.getItem('selectedRestaurantId'));
  const restaurantName = localStorage.getItem('selectedRestaurantName');
  const estimatedMinutes = Number(localStorage.getItem('selectedRestaurantEstimate')) || 15;

  useEffect(() => {
    const options: string[] = [];
    const now = new Date();

    const firstPickup = new Date(now.getTime() + estimatedMinutes * 60000);

    const minutes = firstPickup.getMinutes();
    const roundedMinutes = Math.ceil(minutes / 5) * 5;
    firstPickup.setMinutes(roundedMinutes, 0, 0);

    const endOfDay = new Date(now);
    endOfDay.setHours(21, 0, 0, 0);

    if (firstPickup >= endOfDay) {
      setPickupOptions([]);
      return;
    }

    let currentTime = new Date(firstPickup);
    while (currentTime <= endOfDay) {
      options.push(currentTime.toISOString());
      currentTime = new Date(currentTime.getTime() + 5 * 60000);
    }

    setPickupOptions(options);
    if (options.length > 0) {
      setPickupTime(options[0]);
    }
  }, [estimatedMinutes]);

  const formatPickupTime = (isoString: string) => {
    const date = new Date(isoString);
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  };

  const handlePlaceOrder = async () => {
    if (!cart || cart.cartItems.length === 0) {
      setError('Your cart is empty');
      return;
    }

    if (!restaurantId) {
      setError('Restaurant not selected. Please start a new order.');
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

    if (!pickupTime) {
      setError('Please select a pickup time');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const request: CreateOrderRequest = {
        cartId: cart.cartId,
        restaurantId: restaurantId,
        customerName: customerName.trim(),
        customerPhone: customerPhone.trim(),
        customerNotes: customerNotes.trim() || undefined,
        requestedPickupTime: pickupTime,
      };

      const { data: order } = await orderAPI.createOrder(request);

      resetCart();
      navigate(`/order-confirmation/${order.orderId}`);
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to place order');
    } finally {
      setLoading(false);
    }
  };

  const formatPhoneNumber = (value: string) => {
    const digits = value.replace(/\D/g, '');

    if (digits.length <= 3) {
      return digits;
    } else if (digits.length <= 6) {
      return `${digits.slice(0, 3)}-${digits.slice(3)}`;
    } else {
      return `${digits.slice(0, 3)}-${digits.slice(3, 6)}-${digits.slice(6, 10)}`;
    }
  };

  if (!cart || cart.cartItems.length === 0) {
    return (
      <>
        <NavBar />
        <main className="min-h-screen bg-gray-100 py-12 px-4">
          <div className="max-w-2xl mx-auto text-center">
            <h1 className="text-xl md:text-2xl font-bold mb-4">Your cart is empty</h1>
            <button
              onClick={() => navigate('/order')}
              className="bg-darkred text-white px-6 py-3 rounded-lg hover:bg-red-800 active:scale-95 transition cursor-pointer"
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
      <main className="min-h-screen bg-gray-100 py-6 px-4 md:py-8 pb-32 md:pb-8">
        <div className="max-w-4xl mx-auto">
          <button
            onClick={() => navigate(-1)}
            className="text-darkred hover:underline mb-4 text-sm flex items-center gap-1"
          >
            ← Back to Menu
          </button>

          <h1 className="text-2xl md:text-3xl font-bold mb-6 md:mb-8">Checkout</h1>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6 md:gap-8">
            {/* Customer Info */}
            <div className="bg-white rounded-lg shadow p-5 md:p-6">
              <h2 className="text-lg md:text-xl font-bold mb-4">Pickup Information</h2>

              {restaurantName && (
                <div className="mb-4 p-3 bg-gray-50 rounded-lg">
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
                    className="w-full border rounded-lg p-3 text-base"
                    placeholder="Your name"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">Phone Number *</label>
                  <input
                    type="tel"
                    value={customerPhone}
                    onChange={(e) => setCustomerPhone(formatPhoneNumber(e.target.value))}
                    className="w-full border rounded-lg p-3 text-base"
                    placeholder="123-456-7890"
                    maxLength={12}
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">Pickup Time *</label>
                  {pickupOptions.length === 0 ? (
                    <div className="w-full border rounded-lg p-3 bg-gray-100 text-gray-500">
                      No pickup times available today. We close at 9pm.
                    </div>
                  ) : (
                    <select
                      value={pickupTime}
                      onChange={(e) => setPickupTime(e.target.value)}
                      className="w-full border rounded-lg p-3 text-base bg-white"
                    >
                      {pickupOptions.map((option) => (
                        <option key={option} value={option}>
                          {formatPickupTime(option)}
                        </option>
                      ))}
                    </select>
                  )}
                  <p className="text-sm text-gray-500 mt-1">
                    Estimated prep time: {estimatedMinutes} minutes
                  </p>
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">Special Instructions</label>
                  <textarea
                    value={customerNotes}
                    onChange={(e) => setCustomerNotes(e.target.value)}
                    className="w-full border rounded-lg p-3 h-24 resize-none text-base"
                    placeholder="Any special requests for your order?"
                  />
                </div>
              </div>
            </div>

            {/* Order Summary */}
            <div className="bg-white rounded-lg shadow p-5 md:p-6">
              <button
                onClick={() => setShowOrderSummary(!showOrderSummary)}
                className="w-full flex justify-between items-center md:cursor-default"
              >
                <h2 className="text-lg md:text-xl font-bold">Order Summary</h2>
                <span className="md:hidden text-gray-500">
                  {showOrderSummary ? '▲' : '▼'}
                </span>
              </button>

              {/* Collapsible section on mobile, always visible on desktop */}
              <div className={`${showOrderSummary ? 'block' : 'hidden'} md:block mt-4`}>
                <div className="space-y-3 mb-6 max-h-60 md:max-h-none overflow-y-auto">
                  {cart.cartItems.map((item) => (
                    <div key={item.cartItemId} className="flex justify-between pb-3 border-b">
                      <div className="flex-1 min-w-0 pr-2">
                        <p className="font-medium text-sm md:text-base">
                          {item.quantity}x {item.menuItem.name}
                        </p>
                        {item.options && item.options.length > 0 && (
                          <p className="text-xs md:text-sm text-gray-500 truncate">
                            {item.options.map(opt => opt.optionName).join(', ')}
                          </p>
                        )}
                        {item.memos && (
                          <p className="text-xs md:text-sm text-gray-400 italic truncate">{item.memos}</p>
                        )}
                      </div>
                      <p className="font-medium text-sm md:text-base">${item.price.toFixed(2)}</p>
                    </div>
                  ))}
                </div>

                <div className="space-y-2 border-t pt-4">
                  <div className="flex justify-between text-sm md:text-base">
                    <span>Subtotal</span>
                    <span>${cart.totalPrice.toFixed(2)}</span>
                  </div>
                  <div className="flex justify-between text-sm md:text-base">
                    <span>Tax</span>
                    <span>$0.00</span>
                  </div>
                  <div className="flex justify-between text-lg md:text-xl font-bold pt-2 border-t">
                    <span>Total</span>
                    <span className="text-darkred">${cart.totalPrice.toFixed(2)}</span>
                  </div>
                </div>
              </div>

              {/* Mobile only - show total when collapsed */}
              {!showOrderSummary && (
                <div className="md:hidden mt-2 flex justify-between text-lg font-bold">
                  <span>Total</span>
                  <span className="text-darkred">${cart.totalPrice.toFixed(2)}</span>
                </div>
              )}

              {error && (
                <p className="text-red-500 mt-4 text-sm md:text-base">{error}</p>
              )}
              {/* Desktop Place Order Button */}
              <button
                onClick={handlePlaceOrder}
                disabled={loading || pickupOptions.length === 0}
                className="md:block w-full mt-6 bg-darkred text-white py-4 rounded-lg font-semibold hover:bg-red-800 transition disabled:opacity-50 cursor-pointer border-none text-lg"
              >
                {loading ? 'Placing Order...' : 'Place Order'}
              </button>
            </div>
          </div>
        </div>
      </main>

      {/* Fixed Bottom Button - Mobile Only */}
      <div className="md:hidden fixed bottom-0 left-0 right-0 bg-white border-t shadow-lg p-4">
        <button
          onClick={handlePlaceOrder}
          disabled={loading || pickupOptions.length === 0}
          className="w-full bg-darkred text-white py-4 rounded-lg font-semibold active:scale-95 transition disabled:opacity-50 cursor-pointer border-none text-lg"
        >
          {loading ? 'Placing Order...' : `Place Order • $${cart.totalPrice.toFixed(2)}`}
        </button>
      </div>

      <Footer />
    </>
  );
}

export default Checkout;

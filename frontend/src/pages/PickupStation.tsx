import { useState, useEffect, useRef } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import { adminAPI, Order } from '../services/api';

function PickupStation() {
  const [searchParams] = useSearchParams();
  const restaurantId = Number(searchParams.get('restaurant')) || 1;

  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [connected, setConnected] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState<'PREPARING' | 'READY'>('PREPARING');

  const clientRef = useRef<Client | null>(null);
  const audioRef = useRef<HTMLAudioElement | null>(null);

  // Fetch initial orders
  useEffect(() => {
    fetchOrders();
  }, [restaurantId]);

  // Setup WebSocket connection
  useEffect(() => {
    const token = localStorage.getItem('token');

    if (!token) {
      console.error('No auth token found');
      return;
    }

    const client = new Client({
      brokerURL: `ws://localhost:8080/ws?token=${token}`,
      reconnectDelay: 5000,
      onConnect: () => {
        setConnected(true);

        client.subscribe(`/topic/orders/${restaurantId}`, (message) => {
          const newOrder = JSON.parse(message.body);
          setOrders(prev => [newOrder, ...prev]);
          playNotificationSound();
        });

        client.subscribe(`/topic/orders/${restaurantId}/status`, (message) => {
          const updatedOrder = JSON.parse(message.body);
          if (updatedOrder.status === 'COMPLETED' || updatedOrder.status === 'CANCELLED') {
            setOrders(prev => prev.filter(o => o.orderId !== updatedOrder.orderId));
          } else {
            setOrders(prev => prev.map(o =>
              o.orderId === updatedOrder.orderId ? updatedOrder : o
            ));
          }
        });
      },
      onDisconnect: () => {
        setConnected(false);
      },
    });

    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
    };
  }, [restaurantId]);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const { data } = await adminAPI.getOrders(restaurantId, 0, 100);
      const activeOrders = (data.content || []).filter(o =>
        ['PREPARING', 'READY'].includes(o.status)
      );
      setOrders(activeOrders);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const playNotificationSound = () => {
    if (audioRef.current) {
      audioRef.current.play().catch(console.error);
    }
  };

  const handleUpdateStatus = async (orderId: string, newStatus: string) => {
    try {
      await adminAPI.updateOrderStatus(orderId, newStatus);
      if (newStatus === 'COMPLETED') {
        setOrders(prev => prev.filter(o => o.orderId !== orderId));
      } else {
        setOrders(prev => prev.map(o =>
          o.orderId === orderId ? { ...o, status: newStatus } : o
        ));
      }
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update order');
    }
  };

  const preparingOrders = orders.filter(o => o.status === 'PREPARING');
  const readyOrders = orders.filter(o => o.status === 'READY');

  const filteredOrders = orders
    .filter(o => o.status === activeTab)
    .filter(o =>
      searchQuery === '' ||
      o.customerName?.toLowerCase().includes(searchQuery.toLowerCase()) ||
      o.customerPhone?.includes(searchQuery) ||
      o.orderId?.slice(0, 8).toLowerCase().includes(searchQuery.toLowerCase())
    );

  return (
    <div className="min-h-screen bg-gray-900 text-white touch-manipulation">
      <audio ref={audioRef} src="/notification.mp3" preload="auto" />

      {/* Header - larger touch targets for iPad */}
      <div className="bg-darkred p-4 md:p-6 flex flex-col md:flex-row justify-between items-center gap-4">
        <div className="text-center md:text-left">
          <h1 className="text-2xl md:text-3xl font-bold">Pickup Station</h1>
          <p className="text-sm mt-1">
            {connected ? '🟢 Live' : '🔴 Disconnected'}
          </p>
        </div>
        <div className="flex items-center gap-3 w-full md:w-auto">
          <input
            type="text"
            placeholder="Search..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="px-4 py-4 rounded-xl bg-gray-800 border border-gray-700 flex-1 md:w-72 text-white text-lg"
          />
          <button
            onClick={fetchOrders}
            className="bg-gray-800 p-4 rounded-xl hover:bg-gray-700 active:bg-gray-600 cursor-pointer text-2xl min-w-[60px]"
          >
            🔄
          </button>
        </div>
      </div>

      {/* Tabs - large touch targets */}
      <div className="bg-gray-800 p-3 flex justify-center gap-3">
        <button
          onClick={() => setActiveTab('PREPARING')}
          className={`flex-1 md:flex-none px-6 md:px-12 py-5 rounded-xl font-bold text-xl md:text-2xl transition active:scale-95 cursor-pointer ${activeTab === 'PREPARING'
              ? 'bg-blue-600 text-white'
              : 'bg-gray-700 text-gray-300'
            }`}
        >
          🍳 Preparing ({preparingOrders.length})
        </button>
        <button
          onClick={() => setActiveTab('READY')}
          className={`flex-1 md:flex-none px-6 md:px-12 py-5 rounded-xl font-bold text-xl md:text-2xl transition active:scale-95 cursor-pointer ${activeTab === 'READY'
              ? 'bg-green-600 text-white'
              : 'bg-gray-700 text-gray-300'
            }`}
        >
          ✓ Ready ({readyOrders.length})
        </button>
      </div>

      {/* Orders Grid - optimized for iPad */}
      <div className="p-4 md:p-6">
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-16 w-16 border-b-4 border-white"></div>
          </div>
        ) : filteredOrders.length === 0 ? (
          <div className="text-center py-16 md:py-24">
            <p className="text-3xl md:text-4xl text-gray-500">
              {activeTab === 'PREPARING' ? 'No orders being prepared' : 'No orders ready for pickup'}
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 md:gap-6">
            {filteredOrders.map(order => (
              <div
                key={order.orderId}
                className={`bg-gray-800 rounded-2xl overflow-hidden border-4 ${order.status === 'READY' ? 'border-green-500' : 'border-blue-500'
                  }`}
              >
                {/* Order Header */}
                <div className={`p-4 md:p-5 ${order.status === 'READY' ? 'bg-green-600' : 'bg-blue-600'}`}>
                  <p className="font-bold text-2xl md:text-3xl">#{order.orderId?.slice(0, 8)}</p>
                  <p className="text-base md:text-lg opacity-90">{order.status}</p>
                </div>

                {/* Customer Info */}
                <div className="p-4 md:p-5">
                  <p className="font-bold text-2xl md:text-3xl mb-2">{order.customerName}</p>
                  <p className="text-gray-400 text-lg md:text-xl mb-2">{order.customerPhone}</p>

                  <p className="text-gray-500 text-base mb-4">
                    Pickup: {new Date(order.pickupTime).toLocaleTimeString([], {
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </p>

                  {/* Items */}
                  <div className="text-base md:text-lg text-gray-400 mb-4 max-h-40 overflow-y-auto">
                    {order.items?.map((item, idx) => (
                      <div key={idx} className="mb-2">
                        <p className="font-medium">{item.quantity}x {item.menuItemName}</p>
                        {item.options && item.options.length > 0 && (
                          <p className="text-sm text-gray-500 ml-4">
                            → {item.options.map(opt => opt.optionName).join(', ')}
                          </p>
                        )}
                      </div>
                    ))}
                  </div>

                  {/* Notes */}
                  {order.customerNotes && (
                    <p className="text-base text-orange-400 bg-orange-900/30 p-3 rounded-lg mb-4">
                      📝 {order.customerNotes}
                    </p>
                  )}

                  {/* Total */}
                  <p className="font-bold text-2xl">${order.totalPrice?.toFixed(2)}</p>
                </div>

                {/* Action Button - extra large for touch */}
                <div className="p-4">
                  {order.status === 'PREPARING' && (
                    <button
                      onClick={() => handleUpdateStatus(order.orderId, 'READY')}
                      className="w-full bg-green-600 hover:bg-green-700 active:bg-green-800 text-white py-5 md:py-6 rounded-xl font-bold text-xl md:text-2xl transition active:scale-95 cursor-pointer"
                    >
                      ✓ Mark Ready
                    </button>
                  )}
                  {order.status === 'READY' && (
                    <button
                      onClick={() => handleUpdateStatus(order.orderId, 'COMPLETED')}
                      className="w-full bg-gray-600 hover:bg-gray-700 active:bg-gray-800 text-white py-5 md:py-6 rounded-xl font-bold text-xl md:text-2xl transition active:scale-95 cursor-pointer"
                    >
                      🎉 Picked Up
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default PickupStation;

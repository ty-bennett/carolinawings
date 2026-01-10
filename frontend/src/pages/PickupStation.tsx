import { useState, useEffect, useRef } from 'react';
import { useSearchParams } from 'react-router-dom';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { adminAPI, Order } from '../services/api';

function PickupStation() {
  const [searchParams] = useSearchParams();
  const restaurantId = Number(searchParams.get('restaurant')) || 1;

  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [connected, setConnected] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState<'READY' | 'ALL'>('READY');

  const clientRef = useRef<Client | null>(null);
  const audioRef = useRef<HTMLAudioElement | null>(null);

  // Fetch initial orders
  useEffect(() => {
    fetchOrders();
  }, [restaurantId]);

  // Setup WebSocket connection
  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      onConnect: () => {
        setConnected(true);
        console.log('WebSocket connected');

        // Subscribe to new orders
        client.subscribe(`/topic/orders/${restaurantId}`, (message) => {
          const newOrder = JSON.parse(message.body);
          setOrders(prev => [newOrder, ...prev]);
          playNotificationSound();
        });

        // Subscribe to status changes
        client.subscribe(`/topic/orders/${restaurantId}/status`, (message) => {
          const updatedOrder = JSON.parse(message.body);
          setOrders(prev => prev.map(o =>
            o.orderId === updatedOrder.orderId ? updatedOrder : o
          ));
        });
      },
      onDisconnect: () => {
        setConnected(false);
        console.log('WebSocket disconnected');
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
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
      // Only show today's active orders
      const todayOrders = (data.content || []).filter(o =>
        ['PENDING', 'PREPARING', 'READY'].includes(o.status)
      );
      setOrders(todayOrders);
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
      // WebSocket will handle the update, but update locally for immediate feedback
      setOrders(prev => prev.map(o =>
        o.orderId === orderId ? { ...o, status: newStatus } : o
      ));
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update status');
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PENDING': return 'bg-yellow-500';
      case 'PREPARING': return 'bg-blue-500';
      case 'READY': return 'bg-green-500';
      default: return 'bg-gray-500';
    }
  };

  const filteredOrders = orders
    .filter(o => activeTab === 'ALL' || o.status === 'READY')
    .filter(o =>
      searchQuery === '' ||
      o.customerName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      o.customerPhone.includes(searchQuery) ||
      o.orderId.slice(0, 8).includes(searchQuery)
    );

  const readyOrders = orders.filter(o => o.status === 'READY');
  const preparingOrders = orders.filter(o => o.status === 'PREPARING');
  const pendingOrders = orders.filter(o => o.status === 'PENDING');

  return (
    <div className="min-h-screen bg-gray-900 text-white">
      {/* Audio for notifications */}
      <audio ref={audioRef} src="/notification.mp3" preload="auto" />

      {/* Header */}
      <div className="bg-darkred p-4 flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Pickup Station</h1>
          <p className="text-sm text-gray-300">
            {connected ? '🟢 Live' : '🔴 Disconnected'}
          </p>
        </div>
        <div className="flex items-center gap-4">
          <input
            type="text"
            placeholder="Search name, phone, or order #..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="px-4 py-2 rounded-lg bg-gray-800 border border-gray-700 w-80 text-white"
          />
          <button
            onClick={fetchOrders}
            className="bg-gray-800 px-4 py-2 rounded-lg hover:bg-gray-700"
          >
            🔄 Refresh
          </button>
        </div>
      </div>

      {/* Stats Bar */}
      <div className="bg-gray-800 p-4 flex gap-6 justify-center">
        <div className="text-center">
          <p className="text-3xl font-bold text-yellow-400">{pendingOrders.length}</p>
          <p className="text-sm text-gray-400">Pending</p>
        </div>
        <div className="text-center">
          <p className="text-3xl font-bold text-blue-400">{preparingOrders.length}</p>
          <p className="text-sm text-gray-400">Preparing</p>
        </div>
        <div className="text-center">
          <p className="text-3xl font-bold text-green-400">{readyOrders.length}</p>
          <p className="text-sm text-gray-400">Ready for Pickup</p>
        </div>
      </div>

      {/* Tab Selector */}
      <div className="bg-gray-800 border-t border-gray-700 p-2 flex gap-2 justify-center">
        <button
          onClick={() => setActiveTab('READY')}
          className={`px-6 py-2 rounded-lg font-semibold transition ${activeTab === 'READY'
            ? 'bg-green-500 text-white'
            : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
            }`}
        >
          Ready for Pickup ({readyOrders.length})
        </button>
        <button
          onClick={() => setActiveTab('ALL')}
          className={`px-6 py-2 rounded-lg font-semibold transition ${activeTab === 'ALL'
            ? 'bg-gray-500 text-white'
            : 'bg-gray-700 text-gray-300 hover:bg-gray-600'
            }`}
        >
          All Active Orders ({orders.length})
        </button>
      </div>

      {/* Orders Grid */}
      <div className="p-6">
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-white"></div>
          </div>
        ) : filteredOrders.length === 0 ? (
          <div className="text-center py-12 text-gray-500">
            <p className="text-2xl">No orders to display</p>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
            {filteredOrders.map(order => (
              <div
                key={order.orderId}
                className={`bg-gray-800 rounded-xl overflow-hidden border-t-4 ${order.status === 'READY' ? 'border-green-500' :
                  order.status === 'PREPARING' ? 'border-blue-500' :
                    'border-yellow-500'
                  }`}
              >
                {/* Order Header */}
                <div className={`p-3 ${getStatusColor(order.status)}`}>
                  <p className="font-bold text-lg">#{order.orderId?.slice(0, 8)}</p>
                  <p className="text-sm opacity-90">{order.status}</p>
                </div>

                {/* Customer Info */}
                <div className="p-4">
                  <p className="font-bold text-xl mb-1">{order.customerName}</p>
                  <p className="text-gray-400 text-sm mb-3">{order.customerPhone}</p>

                  {/* Pickup Time */}
                  <p className="text-sm text-gray-400">
                    Pickup: {new Date(order.pickupTime).toLocaleTimeString([], {
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </p>

                  {/* Items Preview */}
                  <div className="mt-3 text-sm text-gray-400 max-h-20 overflow-hidden">
                    {order.items?.slice(0, 3).map((item, idx) => (
                      <p key={idx}>{item.quantity}x {item.menuItemName}</p>
                    ))}
                    {order.items && order.items.length > 3 && (
                      <p className="text-gray-500">+{order.items.length - 3} more...</p>
                    )}
                  </div>

                  {/* Total */}
                  <p className="mt-3 font-bold text-lg">${order.totalPrice?.toFixed(2)}</p>
                </div>

                {/* Actions */}
                <div className="p-3 bg-gray-900">
                  {order.status === 'READY' && (
                    <button
                      onClick={() => handleUpdateStatus(order.orderId, 'COMPLETED')}
                      className="w-full bg-green-600 hover:bg-green-700 text-white py-3 rounded-lg font-bold text-lg transition"
                    >
                      ✓ Mark Picked Up
                    </button>
                  )}
                  {order.status === 'PREPARING' && (
                    <button
                      onClick={() => handleUpdateStatus(order.orderId, 'READY')}
                      className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg font-bold text-lg transition"
                    >
                      Mark Ready
                    </button>
                  )}
                  {order.status === 'PENDING' && (
                    <button
                      onClick={() => handleUpdateStatus(order.orderId, 'PREPARING')}
                      className="w-full bg-yellow-600 hover:bg-yellow-700 text-white py-3 rounded-lg font-bold text-lg transition"
                    >
                      Start Preparing
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

import { useState, useEffect } from 'react';
import { adminAPI, Restaurant, RestaurantHours } from '../../services/api';

interface RestaurantSettingsProps {
  restaurantId: number;
  restaurantName: string;
}

const DAYS_OF_WEEK = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

function RestaurantSettings({ restaurantId, restaurantName }: RestaurantSettingsProps) {
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Editable fields
  const [acceptingOrders, setAcceptingOrders] = useState(true);
  const [estimatedPickupMinutes, setEstimatedPickupMinutes] = useState(15);
  const [editedHours, setEditedHours] = useState<RestaurantHours[]>([]);

  useEffect(() => {
    fetchRestaurantData();
  }, [restaurantId]);

  const fetchRestaurantData = async () => {
    setLoading(true);
    setError('');
    try {
      const { data: restaurantData } = await adminAPI.getRestaurant(restaurantId);
      setRestaurant(restaurantData);
      setAcceptingOrders(restaurantData.acceptingOrders);
      setEstimatedPickupMinutes(restaurantData.estimatedPickupMinutes || 15);

      try {
        const { data: hoursData } = await adminAPI.getRestaurantHours(restaurantId);
        setEditedHours(hoursData || getDefaultHours());
      } catch {
        // If no hours exist, use defaults
        setEditedHours(getDefaultHours());
      }
    } catch (err) {
      console.error(err);
      setError('Failed to load restaurant settings');
    } finally {
      setLoading(false);
    }
  };

  const getDefaultHours = (): RestaurantHours[] => {
    return DAYS_OF_WEEK.map((_, index) => ({
      dayOfWeek: index,
      openTime: '11:00',
      closeTime: index === 5 || index === 6 ? '23:00' : '22:00', // Fri/Sat close later
      closed: false,
    }));
  };

  const handleToggleAcceptingOrders = async () => {
    setSaving(true);
    setError('');
    setSuccess('');
    try {
      await adminAPI.toggleAcceptingOrders(restaurantId, !acceptingOrders);
      setAcceptingOrders(!acceptingOrders);
      setSuccess('Updated successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update');
    } finally {
      setSaving(false);
    }
  };

  const handleUpdatePickupTime = async () => {
    setSaving(true);
    setError('');
    setSuccess('');
    try {
      await adminAPI.updatePickupTime(restaurantId, estimatedPickupMinutes);
      setSuccess('Pickup time updated!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update pickup time');
    } finally {
      setSaving(false);
    }
  };

  const handleHoursChange = (dayIndex: number, field: keyof RestaurantHours, value: string | boolean) => {
    setEditedHours(prev => prev.map(h =>
      h.dayOfWeek === dayIndex ? { ...h, [field]: value } : h
    ));
  };

  const handleSaveHours = async () => {
    setSaving(true);
    setError('');
    setSuccess('');
    try {
      await adminAPI.updateRestaurantHours(restaurantId, editedHours);
      // setHours(editedHours);
      setSuccess('Hours updated successfully!');
      setTimeout(() => setSuccess(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update hours');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center py-12">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-darkred"></div>
      </div>
    );
  }

  return (
    <div>
      <div className="mb-6">
        <h1 className="text-2xl font-bold">Restaurant Settings</h1>
        <p className="text-gray-500">{restaurantName}</p>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {success && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
          {success}
        </div>
      )}

      {/* Quick Toggles */}
      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <h2 className="text-lg font-semibold mb-4">Quick Settings</h2>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          {/* Accepting Orders Toggle */}
          <div className="border rounded-lg p-4">
            <div className="flex justify-between items-center">
              <div>
                <h3 className="font-medium">Accepting Orders</h3>
                <p className="text-sm text-gray-500">
                  {acceptingOrders
                    ? 'Restaurant is currently accepting orders'
                    : 'Restaurant is NOT accepting orders'}
                </p>
              </div>
              <button
                onClick={handleToggleAcceptingOrders}
                disabled={saving}
                className={`relative w-14 h-8 rounded-full transition-colors ${acceptingOrders ? 'bg-green-500' : 'bg-gray-300'
                  }`}
              >
                <span
                  className={`absolute top-1 w-6 h-6 bg-white rounded-full shadow transition-transform ${acceptingOrders ? 'left-7' : 'left-1'
                    }`}
                />
              </button>
            </div>
          </div>

          {/* Estimated Pickup Time */}
          <div className="border rounded-lg p-4">
            <h3 className="font-medium mb-2">Estimated Pickup Time</h3>
            <p className="text-sm text-gray-500 mb-3">How long until orders are ready</p>
            <div className="flex items-center gap-3">
              <select
                value={estimatedPickupMinutes}
                onChange={(e) => setEstimatedPickupMinutes(Number(e.target.value))}
                className="border rounded-lg px-3 py-2 flex-1"
              >
                <option value={10}>10 minutes</option>
                <option value={15}>15 minutes</option>
                <option value={20}>20 minutes</option>
                <option value={25}>25 minutes</option>
                <option value={30}>30 minutes</option>
                <option value={45}>45 minutes</option>
                <option value={60}>60 minutes</option>
              </select>
              <button
                onClick={handleUpdatePickupTime}
                disabled={saving}
                className="bg-darkred text-white px-4 py-2 rounded-lg hover:bg-red-800 transition disabled:opacity-50"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Restaurant Info */}
      <div className="bg-white rounded-lg shadow p-6 mb-6">
        <h2 className="text-lg font-semibold mb-4">Restaurant Information</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm text-gray-500 mb-1">Name</label>
            <p className="font-medium">{restaurant?.name}</p>
          </div>
          <div>
            <label className="block text-sm text-gray-500 mb-1">Address</label>
            <p className="font-medium">{restaurant?.address || 'Not set'}</p>
          </div>
          <div>
            <label className="block text-sm text-gray-500 mb-1">Phone</label>
            <p className="font-medium">{restaurant?.phone || 'Not set'}</p>
          </div>
          <div>
            <label className="block text-sm text-gray-500 mb-1">Status</label>
            <span className={`px-2 py-1 rounded text-sm ${restaurant?.status === 'OPEN'
              ? 'bg-green-100 text-green-800'
              : 'bg-red-100 text-red-800'
              }`}>
              {restaurant?.status}
            </span>
          </div>
        </div>
      </div>

      {/* Operating Hours */}
      <div className="bg-white rounded-lg shadow p-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold">Operating Hours</h2>
          <button
            onClick={handleSaveHours}
            disabled={saving}
            className="bg-darkred text-white px-4 py-2 rounded-lg hover:bg-red-800 transition disabled:opacity-50"
          >
            {saving ? 'Saving...' : 'Save Hours'}
          </button>
        </div>

        <div className="space-y-3">
          {editedHours.sort((a, b) => a.dayOfWeek - b.dayOfWeek).map((dayHours) => (
            <div
              key={dayHours.dayOfWeek}
              className={`flex items-center gap-4 p-3 rounded-lg ${dayHours.closed ? 'bg-gray-100' : 'bg-gray-50'
                }`}
            >
              <div className="w-28 font-medium">
                {DAYS_OF_WEEK[dayHours.dayOfWeek]}
              </div>

              <label className="flex items-center gap-2 cursor-pointer">
                <input
                  type="checkbox"
                  checked={dayHours.closed}
                  onChange={(e) => handleHoursChange(dayHours.dayOfWeek, 'closed', e.target.checked)}
                  className="w-4 h-4"
                />
                <span className="text-sm text-gray-600">Closed</span>
              </label>

              {!dayHours.closed && (
                <>
                  <div className="flex items-center gap-2">
                    <label className="text-sm text-gray-500">Open:</label>
                    <input
                      type="time"
                      value={dayHours.openTime}
                      onChange={(e) => handleHoursChange(dayHours.dayOfWeek, 'openTime', e.target.value)}
                      className="border rounded px-2 py-1"
                    />
                  </div>
                  <div className="flex items-center gap-2">
                    <label className="text-sm text-gray-500">Close:</label>
                    <input
                      type="time"
                      value={dayHours.closeTime}
                      onChange={(e) => handleHoursChange(dayHours.dayOfWeek, 'closeTime', e.target.value)}
                      className="border rounded px-2 py-1"
                    />
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default RestaurantSettings;

import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { publicAPI, Restaurant } from '../services/api';

export function Order() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchRestaurants = async () => {
      try {
        const { data } = await publicAPI.getRestaurants();
        setRestaurants(data.content || []);
      } catch (err) {
        console.error(err);
        setError('Failed to load restaurants');
      } finally {
        setLoading(false);
      }
    };

    fetchRestaurants();
  }, []);

  const handleSelectRestaurant = (restaurant: Restaurant) => {
    localStorage.setItem('selectedRestaurantId', restaurant.id.toString());
    localStorage.setItem('selectedRestaurantName', restaurant.name);
    navigate(`/order/${restaurant.id}`);
  };

  if (loading) {
    return (
      <>
        <NavBar />
        <div className="flex justify-center items-center h-screen">
          <p className="text-xl">Loading restaurants...</p>
        </div>
      </>
    );
  }

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100 py-12 px-4">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-3xl font-bold text-center mb-8">Select a Location</h1>

          {error && <p className="text-red-500 text-center mb-4">{error}</p>}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {restaurants.map((restaurant) => (
              <div
                key={restaurant.id}
                onClick={() => handleSelectRestaurant(restaurant)}
                className="bg-white rounded-lg shadow-md p-6 cursor-pointer hover:shadow-lg transition-shadow"
              >
                <h2 className="text-xl font-semibold mb-2">{restaurant.name}</h2>
                <p className="text-gray-600 mb-2">{restaurant.address}</p>
                {restaurant.phone && (
                  <p className="text-gray-500 text-sm">{restaurant.phone}</p>
                )}
                <div className="mt-4 flex items-center justify-between">
                  <span className={`px-2 py-1 rounded text-sm ${restaurant.acceptingOrders
                    ? 'bg-green-100 text-green-800'
                    : 'bg-red-100 text-red-800'
                    }`}>
                    {restaurant.acceptingOrders ? 'Open for Orders' : 'Currently Closed'}
                  </span>
                  <span className="text-gray-500 text-sm">
                    ~{restaurant.estimatedPickupMinutes} min pickup
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
      <Footer />
    </>
  );
}

export default Order;

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
    localStorage.setItem('selectedRestaurantEstimate', restaurant.estimatedPickupMinutes?.toString() || '15');
    navigate(`/order/${restaurant.id}`);
  };

  if (loading) {
    return (
      <>
        <NavBar />
        <div className="flex justify-center items-center h-screen">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-darkred"></div>
        </div>
      </>
    );
  }

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100 py-8 px-4 md:py-12">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-2xl md:text-3xl font-bold text-center mb-6 md:mb-8">Select a Location</h1>

          {error && <p className="text-red-500 text-center mb-4">{error}</p>}

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 md:gap-6">
            {restaurants.map((restaurant) => (
              <div
                key={restaurant.id}
                onClick={() => handleSelectRestaurant(restaurant)}
                className="bg-white rounded-lg shadow-md p-5 md:p-6 cursor-pointer hover:shadow-lg active:scale-[0.98] transition-all"
              >
                <h2 className="text-lg md:text-xl font-semibold mb-2">{restaurant.name}</h2>
                <p className="text-gray-600 text-sm md:text-base mb-2">{restaurant.address}</p>
                {restaurant.phone && (
                  <a
                    href={`tel:${restaurant.phone}`}
                    onClick={(e) => e.stopPropagation()}
                    className="text-darkred text-sm hover:underline"
                  >
                    {restaurant.phone}
                  </a>
                )}
                <div className="mt-4 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-2">
                  <span className={`px-3 py-1 rounded-full text-sm font-medium ${restaurant.acceptingOrders
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

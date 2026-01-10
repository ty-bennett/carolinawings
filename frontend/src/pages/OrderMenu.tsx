import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { publicAPI, Restaurant, Menu, MenuItem } from '../services/api';
import MenuItemModal from '../components/MenuItemModal';

function OrderMenu() {
  const { restaurantId } = useParams<{ restaurantId: string }>();
  const navigate = useNavigate();
  const [restaurant, setRestaurant] = useState<Restaurant | null>(null);
  const [menus, setMenus] = useState<Menu[]>([]);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [selectedMenu, setSelectedMenu] = useState<Menu | null>(null);
  const [selectedItem, setSelectedItem] = useState<MenuItem | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Fetch restaurant and menus
  useEffect(() => {
    const fetchData = async () => {
      if (!restaurantId) return;

      try {
        setLoading(true);

        const { data: restaurantData } = await publicAPI.getRestaurant(Number(restaurantId));
        setRestaurant(restaurantData);

        const { data: menusData } = await publicAPI.getMenus(Number(restaurantId));
        const menus = menusData.content || [];
        setMenus(menus);

        const primaryMenu = menus.find(m => m.isPrimary) || menus[0];
        if (primaryMenu) {
          setSelectedMenu(primaryMenu);
        }
      } catch (err) {
        console.error(err);
        setError('Failed to load menu');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [restaurantId]);

  // Fetch menu items when selected menu changes
  useEffect(() => {
    const fetchMenuItems = async () => {
      if (!selectedMenu) return;

      try {
        const { data } = await publicAPI.getMenuItems(selectedMenu.id);
        setMenuItems(data.content || []);
      } catch (err) {
        console.error(err);
        setError('Failed to load menu items');
      }
    };

    fetchMenuItems();
  }, [selectedMenu]);

  // Group items by category
  const itemsByCategory = menuItems.reduce((acc, item) => {
    const category = item.category || 'Other';
    if (!acc[category]) {
      acc[category] = [];
    }
    acc[category].push(item);
    return acc;
  }, {} as Record<string, MenuItem[]>);

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

  if (error) {
    return (
      <>
        <NavBar />
        <div className="flex flex-col justify-center items-center h-screen px-4">
          <p className="text-red-500 text-xl text-center">{error}</p>
          <button
            onClick={() => navigate('/order')}
            className="mt-4 bg-darkred text-white px-6 py-2 rounded-lg"
          >
            Back to Locations
          </button>
        </div>
      </>
    );
  }

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100">
        {/* Restaurant Header */}
        <div className="bg-darkred text-white py-6 px-4 md:py-8">
          <div className="max-w-6xl mx-auto">
            <button
              onClick={() => navigate('/order')}
              className="text-white/80 hover:text-white text-sm mb-2 flex items-center gap-1"
            >
              ← Change Location
            </button>
            <h1 className="text-2xl md:text-3xl font-bold">{restaurant?.name}</h1>
            <p className="text-gray-200 text-sm md:text-base mt-1">{restaurant?.address}</p>
            <div className="mt-3 flex flex-wrap items-center gap-2 md:gap-4">
              <span className={`px-3 py-1 rounded-full text-sm font-medium ${restaurant?.acceptingOrders
                ? 'bg-green-600'
                : 'bg-red-600'
                }`}>
                {restaurant?.acceptingOrders ? 'Open' : 'Closed'}
              </span>
              <span className="text-sm">
                ~{restaurant?.estimatedPickupMinutes} min pickup
              </span>
            </div>
          </div>
        </div>

        {/* Menu Tabs (if multiple menus) */}
        {menus.length > 1 && (
          <div className="bg-white shadow sticky top-0 z-10">
            <div className="max-w-6xl mx-auto flex gap-2 px-4 py-3 overflow-x-auto">
              {menus.map((menu) => (
                <button
                  key={menu.id}
                  onClick={() => setSelectedMenu(menu)}
                  className={`px-4 py-2 rounded-full font-medium transition whitespace-nowrap text-sm md:text-base ${selectedMenu?.id === menu.id
                    ? 'bg-darkred text-white'
                    : 'bg-gray-200 hover:bg-gray-300'
                    }`}
                >
                  {menu.name}
                </button>
              ))}
            </div>
          </div>
        )}

        {/* Category Quick Links - Mobile Only */}
        <div className="bg-white border-b md:hidden sticky top-0 z-10">
          <div className="flex gap-2 px-4 py-2 overflow-x-auto">
            {Object.keys(itemsByCategory).map((category) => (
              <button
                key={category}
                onClick={() => {
                  const element = document.getElementById(category.replace(/\s+/g, '-'));
                  element?.scrollIntoView({ behavior: 'smooth' });
                }}
                className="px-3 py-1 bg-gray-100 rounded-full text-sm whitespace-nowrap text-gray-700"
              >
                {category}
              </button>
            ))}
          </div>
        </div>
        {/* Menu Items */}
        <div className="max-w-6xl mx-auto py-6 px-4 md:py-8">
          {Object.entries(itemsByCategory).map(([category, items]) => (
            <div key={category} id={category.replace(/\s+/g, '-')} className="mb-8 md:mb-10 scroll-mt-20">
              <h2 className="text-xl md:text-2xl font-bold mb-3 md:mb-4 text-darkred border-b-2 border-darkred pb-2">
                {category}
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3 md:gap-4">
                {items.map((item) => (
                  <div
                    key={item.id}
                    onClick={() => setSelectedItem(item)}
                    className="bg-white rounded-lg shadow p-4 hover:shadow-md active:scale-[0.98] transition cursor-pointer"
                  >
                    <div className="flex justify-between items-start gap-3">
                      <div className="flex-1 min-w-0">
                        <h3 className="font-semibold text-base md:text-lg">{item.name}</h3>
                        <p className="text-gray-600 text-sm mt-1 line-clamp-2">{item.description}</p>
                        <p className="font-bold text-darkred mt-2">${item.price.toFixed(2)}</p>
                      </div>
                      {item.imageUrl && (
                        <img
                          src={item.imageUrl}
                          alt={item.name}
                          className="w-20 h-20 md:w-24 md:h-24 object-cover rounded-lg flex-shrink-0"
                        />
                      )}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </main >

      {selectedItem && (
        <MenuItemModal
          item={selectedItem}
          onClose={() => setSelectedItem(null)}
        />
      )
      }
      <Footer />
    </>
  );
}

export default OrderMenu;

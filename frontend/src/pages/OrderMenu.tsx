import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavBar from '../components/NavBar';
import Footer from '../components/Footer';
import { publicAPI, Restaurant, Menu, MenuItem } from '../services/api';
import MenuItemModal from '../components/MenuItemModal';

function OrderMenu() {
  const { restaurantId } = useParams<{ restaurantId: string }>();
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
        
        // Get restaurant details
        const { data: restaurantData } = await publicAPI.getRestaurant(Number(restaurantId));
        setRestaurant(restaurantData);
        
        // Get menus for this restaurant
        const { data: menusData } = await publicAPI.getMenus(Number(restaurantId));
        const menus = menusData.content || [];
        setMenus(menus);
        
        // Select the primary menu by default, or first menu
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
          <p className="text-xl">Loading menu...</p>
        </div>
      </>
    );
  }

  if (error) {
    return (
      <>
        <NavBar />
        <div className="flex justify-center items-center h-screen">
          <p className="text-red-500 text-xl">{error}</p>
        </div>
      </>
    );
  }

  return (
    <>
      <NavBar />
      <main className="min-h-screen bg-gray-100">
        {/* Restaurant Header */}
        <div className="bg-darkred text-white py-8 px-4">
          <div className="max-w-6xl mx-auto">
            <h1 className="text-3xl font-bold">{restaurant?.name}</h1>
            <p className="text-gray-200 mt-1">{restaurant?.address}</p>
            <div className="mt-2 flex items-center gap-4">
              <span className={`px-2 py-1 rounded text-sm ${
                restaurant?.acceptingOrders 
                  ? 'bg-green-600' 
                  : 'bg-red-600'
              }`}>
                {restaurant?.acceptingOrders ? 'Open for Orders' : 'Currently Closed'}
              </span>
              <span className="text-sm">
                ~{restaurant?.estimatedPickupMinutes} min pickup
              </span>
            </div>
          </div>
        </div>

        {/* Menu Tabs (if multiple menus) */}
        {menus.length > 1 && (
          <div className="bg-white shadow">
            <div className="max-w-6xl mx-auto flex gap-4 px-4 py-2">
              {menus.map((menu) => (
                <button
                  key={menu.id}
                  onClick={() => setSelectedMenu(menu)}
                  className={`px-4 py-2 rounded font-medium transition ${
                    selectedMenu?.id === menu.id
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

        {/* Menu Items */}
        <div className="max-w-6xl mx-auto py-8 px-4">
          {Object.entries(itemsByCategory).map(([category, items]) => (
            <div key={category} className="mb-10">
              <h2 className="text-2xl font-bold mb-4 text-darkred border-b-2 border-darkred pb-2">
                {category}
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {items.map((item) => (
                <div
                  key={item.id}
                  onClick={() => setSelectedItem(item)}
                  className="bg-white rounded-lg shadow p-4 hover:shadow-md transition cursor-pointer"
                  >
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="font-semibold text-lg">{item.name}</h3>
                        <p className="text-gray-600 text-sm mt-1">{item.description}</p>
                      </div>
                      <div className="ml-4 text-right">
                        <span className="font-bold text-darkred">${item.price.toFixed(2)}</span>
                        {item.imageUrl && (
                          <img 
                            src={item.imageUrl} 
                            alt={item.name}
                            className="w-20 h-20 object-cover rounded mt-2"
                          />
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </main>
      {selectedItem && (
      <MenuItemModal 
        item={selectedItem} 
        onClose={() => setSelectedItem(null)} 
      />
      )}
      <Footer />
    </>
  );
}

export default OrderMenu;
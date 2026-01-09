import navbar from './NavBar.module.css';
import wood from '/backgroundImages/woodtexture.jpg';
import logo from '/carolinawingslogo.png';
import { NavLink } from 'react-router-dom';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import { CartItem } from '../services/api.ts';
import MenuItemModal from './MenuItemModal';

const NavBar = () => {
  const [showUserDropdown, setShowUserDropdown] = useState(false);
  const [showCart, setShowCart] = useState(false);
  const navigate = useNavigate();
  const toggleDropdown = () => setShowUserDropdown(!showUserDropdown);
  const [editingItem, setEditingItem] = useState<CartItem | null>(null);

  const { user, logout } = useAuth();
  const { cart, itemCount, removeItem, isLoading } = useCart();

  const name = user?.name ?? "";
  const roles = user?.roles ?? ["CUSTOMER"];
  const id = user?.id ?? "";

  const handleLogout = () => {
    logout();
    navigate("/");
  };



  return (
    <>
      <div className={navbar.gridparent}>
        <div className={navbar.gridchildlogo}>
          <img src={logo} className={navbar.logo}></img>
        </div>
        <div className={navbar.gridchildimg}>
          <img src={wood} className={navbar.backgroundimg}></img>
        </div>
        <div className={navbar.gridchildbutton}>
          <NavLink to="/order" className={navbar.ordernow}>Order Now</NavLink>
        </div>
        <div className={navbar.gridchildnav}>
          <div className={navbar.navbarposition}>
            <div className={navbar.left}></div>
            <div className={navbar.center}>
              <NavLink to="/" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Home</NavLink>
              <NavLink to="/menu" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Menu</NavLink>
              <NavLink to="/specials" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Specials</NavLink>
              <NavLink to="/catering" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Catering</NavLink>
              <NavLink to="/locations" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Locations</NavLink>
              <NavLink to="/contact" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Contact</NavLink>
              {!id && (
                <NavLink to="/login" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Login</NavLink>
              )}
              {((roles.includes("MANAGER") || roles.includes("RESTAURANT_ADMIN") || roles.includes("ADMIN")) &&
                <NavLink to="/admin/restaurants/dashboard" className={({ isActive }) => isActive ? navbar.navbaritemactive : navbar.navbaritem}>Admin Panel</NavLink>
              )}
            </div>
            <div className={navbar.right}>
              {/* Cart Icon */}
              {id && (
                <button
                  onClick={() => setShowCart(true)}
                  className="relative mr-4 text-white hover:text-yellow-400 cursor-pointer bg-transparent border-none"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                  {itemCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-600 text-white text-xs rounded-full h-5 w-5 flex items-center justify-center">
                      {itemCount}
                    </span>
                  )}
                </button>
              )}

              {/* User Dropdown */}
              {id && (
                <div
                  className={navbar.userDropdownContainer}
                  onClick={toggleDropdown}
                >
                  <span className={navbar.welcomeMessage}>Welcome, {name}</span>
                  {showUserDropdown && (
                    <div className={navbar.dropdownMenu}>
                      <button
                        onClick={() => navigate('/orders')}
                        className={navbar.dropdownItem}
                      >
                        Order History
                      </button>
                      <button onClick={handleLogout} className={navbar.dropdownItem}>
                        Logout
                      </button>
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </div >

      {/* Cart Sidebar */}
      < div
        className={`fixed inset-0 z-50 transition-opacity duration-300 ${showCart ? 'opacity-100 pointer-events-auto' : 'opacity-0 pointer-events-none'
          }`
        }
      >
        {/* Overlay - semi-transparent so you can still see the page */}
        < div
          className={`absolute inset-0 bg-black transition-opacity duration-300 ${showCart ? 'opacity-30' : 'bg-opacity-0'
            }`}
          onClick={() => setShowCart(false)}
        />

        {/* Sidebar - slides in from right */}
        <div
          className={`absolute right-0 top-0 h-full w-96 bg-white shadow-2xl flex flex-col transform transition-transform duration-300 ease-out ${showCart ? 'translate-x-0' : 'translate-x-full'
            }`}
        >
          {/* Header */}
          <div className="p-4 bg-darkred text-white flex justify-between items-center">
            <h2 className="text-xl font-bold">Your Cart ({itemCount})</h2>
            <button
              onClick={() => setShowCart(false)}
              className="text-2xl hover:text-gray-300 bg-transparent border-none cursor-pointer"
            >
              &times;
            </button>
          </div>

          {/* Cart Items */}
          <div className="flex-1 overflow-y-auto p-4">
            {isLoading ? (
              <p className="text-center text-gray-500">Loading...</p>
            ) : !cart || cart.cartItems.length === 0 ? (
              <p className="text-center text-gray-500 mt-8">Your cart is empty</p>
            ) : (
              <div className="space-y-4">
                {cart.cartItems.map((item) => (
                  <div key={item.cartItemId} className="border-b pb-4">
                    <div className="flex justify-between items-start">
                      <div className="flex-1">
                        <h3 className="font-semibold">{item.menuItem.name}</h3>
                        <p className="text-sm text-gray-500">Qty: {item.quantity}</p>
                        {item.options && item.options.length > 0 && (
                          <p className="text-xs text-gray-400">
                            {item.options.map(opt => opt.optionName).join(', ')}
                          </p>
                        )}
                        {item.memos && (
                          <p className="text-xs text-gray-400 italic">Note: {item.memos}</p>
                        )}
                      </div>
                      <div className="text-right">
                        <p className="font-semibold">${item.price.toFixed(2)}</p>
                        <div className="flex flex-col items-end mt-1">
                          <button
                            onClick={() => {
                              setEditingItem(item);
                              setShowCart(false);
                            }}
                            className="text-blue-500 text-sm hover:underline bg-transparent border-none cursor-pointer"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => removeItem(item.cartItemId)}
                            className="text-red-500 text-sm hover:underline bg-transparent border-none cursor-pointer"
                          >
                            Remove
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          {/* Footer */}
          {cart && cart.cartItems.length > 0 && (
            <div className="p-4 border-t bg-gray-50">
              <div className="flex justify-between items-center mb-4">
                <span className="text-lg font-bold">Total:</span>
                <span className="text-lg font-bold text-darkred">${cart.totalPrice.toFixed(2)}</span>
              </div>
              <button
                onClick={() => {
                  setShowCart(false);
                  navigate('/checkout');
                }}
                className="w-full bg-darkred text-white py-3 rounded-lg font-semibold hover:bg-red-800 transition cursor-pointer"
              >
                Checkout
              </button>
            </div>
          )}
        </div>
      </div >
      {editingItem && (
        <MenuItemModal
          item={editingItem.menuItem}
          onClose={() => setEditingItem(null)}
          editMode={true}
          cartItemId={editingItem.cartItemId}
          initialQuantity={editingItem.quantity}
          initialMemos={editingItem.memos}
          initialOptions={editingItem.options}
        />
      )
      }
    </>
  );
}

export default NavBar;

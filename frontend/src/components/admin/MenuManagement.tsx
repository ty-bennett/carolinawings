import { useState, useEffect } from 'react';
import { adminAPI, Menu, MenuItem } from '../../services/api';

interface MenuManagementProps {
  restaurantId: number;
  restaurantName: string;
}

function MenuManagement({ restaurantId, restaurantName }: MenuManagementProps) {
  const [menus, setMenus] = useState<Menu[]>([]);
  const [selectedMenu, setSelectedMenu] = useState<Menu | null>(null);
  const [menuItems, setMenuItems] = useState<MenuItem[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searchQuery, setSearchQuery] = useState('');

  // Modal states
  const [showAddMenuModal, setShowAddMenuModal] = useState(false);
  const [showAddItemModal, setShowAddItemModal] = useState(false);
  const [editingItem, setEditingItem] = useState<MenuItem | null>(null);
  const [deletingItem, setDeletingItem] = useState<MenuItem | null>(null);

  // Fetch menus
  useEffect(() => {
    fetchMenus();
  }, [restaurantId]);

  // Fetch menu items when selected menu changes
  useEffect(() => {
    if (selectedMenu) {
      fetchMenuItems();
    }
  }, [selectedMenu]);

  const fetchMenus = async () => {
    setLoading(true);
    try {
      const { data } = await adminAPI.getMenus(restaurantId);
      const menuList = data.content || [];
      setMenus(menuList);

      // Auto-select primary menu or first menu
      if (menuList.length > 0 && !selectedMenu) {
        const primary = menuList.find(m => m.isPrimary) || menuList[0];
        setSelectedMenu(primary);
      }
    } catch (err) {
      console.error(err);
      setError('Failed to load menus');
    } finally {
      setLoading(false);
    }
  };

  const fetchMenuItems = async () => {
    if (!selectedMenu) return;

    setLoading(true);
    try {
      const { data } = await adminAPI.getMenuItems(selectedMenu.id);
      setMenuItems(data.content || []);
    } catch (err) {
      console.error(err);
      setError('Failed to load menu items');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateMenu = async (name: string, description: string) => {
    try {
      await adminAPI.createMenu(restaurantId, { name, description });
      fetchMenus();
      setShowAddMenuModal(false);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to create menu');
    }
  };

  const handleDeleteMenu = async (menuId: number) => {
    if (!window.confirm('Are you sure you want to delete this menu?')) return;

    try {
      await adminAPI.deleteMenu(restaurantId, menuId);
      if (selectedMenu?.id === menuId) {
        setSelectedMenu(null);
        setMenuItems([]);
      }
      fetchMenus();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to delete menu');
    }
  };

  const handleSetPrimary = async (menuId: number) => {
    try {
      await adminAPI.setPrimaryMenu(restaurantId, menuId);
      fetchMenus();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to set primary menu');
    }
  };

  const handleCreateItem = async (itemData: Partial<MenuItem>) => {
    if (!selectedMenu) return;

    try {
      await adminAPI.createMenuItem(selectedMenu.id, itemData);
      fetchMenuItems();
      setShowAddItemModal(false);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to create menu item');
    }
  };

  const handleUpdateItem = async (itemData: Partial<MenuItem>) => {
    if (!editingItem) return;

    try {
      await adminAPI.updateMenuItem(editingItem.id, itemData);
      fetchMenuItems();
      setEditingItem(null);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update menu item');
    }
  };

  const handleDeleteItem = async () => {
    if (!deletingItem) return;

    try {
      await adminAPI.deleteMenuItem(deletingItem.id);
      fetchMenuItems();
      setDeletingItem(null);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to delete menu item');
    }
  };

  const filteredItems = menuItems.filter(item =>
    item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    item.description?.toLowerCase().includes(searchQuery.toLowerCase()) ||
    item.category?.toLowerCase().includes(searchQuery.toLowerCase())
  );

  // Group items by category
  const itemsByCategory = filteredItems.reduce((acc, item) => {
    const category = item.category || 'Uncategorized';
    if (!acc[category]) acc[category] = [];
    acc[category].push(item);
    return acc;
  }, {} as Record<string, MenuItem[]>);

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-2xl font-bold">Menu Management</h1>
          <p className="text-gray-500">{restaurantName}</p>
        </div>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {/* Menu Selector */}
      <div className="bg-white rounded-lg shadow p-4 mb-6">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-lg font-semibold">Menus</h2>
          <button
            onClick={() => setShowAddMenuModal(true)}
            className="bg-darkred text-white px-4 py-2 rounded-lg hover:bg-red-800 transition"
          >
            + New Menu
          </button>
        </div>

        <div className="flex gap-2 flex-wrap">
          {menus.map(menu => (
            <div
              key={menu.id}
              className={`relative flex items-center gap-2 px-4 py-2 rounded-lg border cursor-pointer transition ${selectedMenu?.id === menu.id
                ? 'bg-darkred text-white border-darkred'
                : 'bg-gray-50 hover:bg-gray-100 border-gray-200'
                }`}
              onClick={() => setSelectedMenu(menu)}
            >
              <span>{menu.name}</span>
              {menu.isPrimary && (
                <span className={`text-xs px-1 rounded ${selectedMenu?.id === menu.id ? 'bg-white text-darkred' : 'bg-green-100 text-green-800'
                  }`}>
                  Primary
                </span>
              )}

              {/* Menu actions dropdown */}
              <div className="relative group">
                <button
                  onClick={(e) => e.stopPropagation()}
                  className={`ml-2 px-1 rounded hover:bg-opacity-20 hover:bg-black ${selectedMenu?.id === menu.id ? 'text-white' : 'text-gray-500'
                    }`}
                >
                  ⋮
                </button>
                <div className="absolute right-0 top-full mt-1 bg-white border rounded shadow-lg hidden group-hover:block z-10 min-w-32">
                  {!menu.isPrimary && (
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleSetPrimary(menu.id);
                      }}
                      className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm text-gray-700"
                    >
                      Set as Primary
                    </button>
                  )}
                  <button
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDeleteMenu(menu.id);
                    }}
                    className="block w-full text-left px-4 py-2 hover:bg-gray-100 text-sm text-red-600"
                  >
                    Delete Menu
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Menu Items */}
      {selectedMenu && (
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-lg font-semibold">{selectedMenu.name} - Items</h2>
            <div className="flex gap-4">
              <input
                type="text"
                placeholder="Search items..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="border rounded-lg px-4 py-2 w-64"
              />
              <button
                onClick={() => setShowAddItemModal(true)}
                className="bg-darkred text-white px-4 py-2 rounded-lg hover:bg-red-800 transition"
              >
                + Add Item
              </button>
            </div>
          </div>

          {loading ? (
            <div className="flex justify-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-darkred"></div>
            </div>
          ) : Object.keys(itemsByCategory).length === 0 ? (
            <p className="text-center text-gray-500 py-8">No items found</p>
          ) : (
            <div className="space-y-6">
              {Object.entries(itemsByCategory).map(([category, items]) => (
                <div key={category}>
                  <h3 className="text-md font-semibold text-gray-600 mb-3 border-b pb-2">{category}</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {items.map(item => (
                      <div
                        key={item.id}
                        className={`border rounded-lg p-4 relative ${!item.enabled ? 'bg-gray-100 opacity-60' : 'bg-white'
                          }`}
                      >
                        {!item.enabled && (
                          <span className="absolute top-2 right-2 bg-red-100 text-red-600 text-xs px-2 py-1 rounded">
                            Disabled
                          </span>
                        )}
                        <div className="flex justify-between items-start mb-2">
                          <h4 className="font-semibold">{item.name}</h4>
                          <span className="text-green-600 font-bold">${item.price?.toFixed(2)}</span>
                        </div>
                        <p className="text-gray-500 text-sm mb-4 line-clamp-2">{item.description}</p>
                        <div className="flex gap-2">
                          <button
                            onClick={() => setEditingItem(item)}
                            className="flex-1 bg-blue-500 text-white py-1 rounded hover:bg-blue-600 transition text-sm"
                          >
                            Edit
                          </button>
                          <button
                            onClick={() => setDeletingItem(item)}
                            className="flex-1 bg-red-500 text-white py-1 rounded hover:bg-red-600 transition text-sm"
                          >
                            Delete
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Add Menu Modal */}
      {showAddMenuModal && (
        <AddMenuModal
          onClose={() => setShowAddMenuModal(false)}
          onSave={handleCreateMenu}
        />
      )}

      {/* Add Item Modal */}
      {showAddItemModal && (
        <MenuItemModal
          onClose={() => setShowAddItemModal(false)}
          onSave={handleCreateItem}
        />
      )}

      {/* Edit Item Modal */}
      {editingItem && (
        <MenuItemModal
          item={editingItem}
          onClose={() => setEditingItem(null)}
          onSave={handleUpdateItem}
        />
      )}

      {/* Delete Confirmation Modal */}
      {deletingItem && (
        <ConfirmModal
          title="Delete Menu Item"
          message={`Are you sure you want to delete "${deletingItem.name}"?`}
          onConfirm={handleDeleteItem}
          onCancel={() => setDeletingItem(null)}
        />
      )}
    </div>
  );
}

// Add Menu Modal Component
function AddMenuModal({ onClose, onSave }: {
  onClose: () => void;
  onSave: (name: string, description: string) => void;
}) {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(name, description);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md" onClick={e => e.stopPropagation()}>
        <h2 className="text-xl font-bold mb-4">Create New Menu</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Menu Name *</label>
            <input
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="w-full border rounded-lg p-2"
              required
            />
          </div>
          <div className="mb-4">
            <label className="block text-sm font-medium mb-1">Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full border rounded-lg p-2 h-24 resize-none"
            />
          </div>
          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-darkred text-white rounded-lg hover:bg-red-800"
            >
              Create
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

// Menu Item Modal Component (for add/edit)
function MenuItemModal({ item, onClose, onSave }: {
  item?: MenuItem;
  onClose: () => void;
  onSave: (data: Partial<MenuItem>) => void;
}) {
  const [formData, setFormData] = useState({
    name: item?.name || '',
    description: item?.description || '',
    price: item?.price?.toString() || '',
    category: item?.category || '',
    imageUrl: item?.imageUrl || '',
    enabled: item?.enabled ?? true,
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave({
      ...formData,
      price: parseFloat(formData.price),
    });
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md" onClick={e => e.stopPropagation()}>
        <h2 className="text-xl font-bold mb-4">{item ? 'Edit' : 'Add'} Menu Item</h2>
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="block text-sm font-medium mb-1">Name *</label>
            <input
              type="text"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              className="w-full border rounded-lg p-2"
              required
            />
          </div>
          <div className="mb-3">
            <label className="block text-sm font-medium mb-1">Description</label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              className="w-full border rounded-lg p-2 h-20 resize-none"
            />
          </div>
          <div className="grid grid-cols-2 gap-3 mb-3">
            <div>
              <label className="block text-sm font-medium mb-1">Price *</label>
              <input
                type="number"
                step="0.01"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                className="w-full border rounded-lg p-2"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-1">Category</label>
              <input
                type="text"
                value={formData.category}
                onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                className="w-full border rounded-lg p-2"
              />
            </div>
          </div>
          <div className="mb-3">
            <label className="block text-sm font-medium mb-1">Image URL</label>
            <input
              type="text"
              value={formData.imageUrl}
              onChange={(e) => setFormData({ ...formData, imageUrl: e.target.value })}
              className="w-full border rounded-lg p-2"
            />
          </div>
          <div className="mb-4">
            <label className="flex items-center gap-2 cursor-pointer">
              <input
                type="checkbox"
                checked={formData.enabled}
                onChange={(e) => setFormData({ ...formData, enabled: e.target.checked })}
                className="w-4 h-4"
              />
              <span className="text-sm">Enabled (visible to customers)</span>
            </label>
          </div>
          <div className="flex justify-end gap-2">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-darkred text-white rounded-lg hover:bg-red-800"
            >
              {item ? 'Save' : 'Add'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

// Confirm Modal Component
function ConfirmModal({ title, message, onConfirm, onCancel }: {
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}) {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-sm">
        <h2 className="text-xl font-bold mb-2">{title}</h2>
        <p className="text-gray-600 mb-6">{message}</p>
        <div className="flex justify-end gap-2">
          <button
            onClick={onCancel}
            className="px-4 py-2 border rounded-lg hover:bg-gray-50"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600"
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default MenuManagement;

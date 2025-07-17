import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import EditMenuItemModal from "../components/EditMenuItemModal";
import ConfirmDeleteModal from "../components/ConfirmDeleteModal";
import AddMenuItemModal from "../components/AddMenuItemModal";

function MenuDetails() {
  
 

  const [menus, setMenus] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true); // <-
  const [searchQuery, setSearchQuery] = useState("");
  const [menuItems, setMenuItems] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [itemToDelete, setItemToDelete] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddModal, setShowAddModal] = useState(false);



  const restaurantId = localStorage.getItem("restaurants");
  const menuId = localStorage.getItem("menuId");

  useEffect(() => {
      fetchMenuItems();
  }, [menuId]);

  useEffect(() => {
    fetchMenus();
  }, [restaurantId]);



  const fetchMenuItems = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("token");
      const id = localStorage.getItem("menuId");
      if(!id) {
        console.warn("no Restaurant found");
        return;
      } 

      const res = await axios.get(`http://localhost:8080/admin/menus/${id}/menuitems`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMenuItems(res.data || []);
      setError(null); 
    } catch (err) {
      console.error(err);
      setError("Failed to load menu.");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteClick = (item) => {
    setItemToDelete(item);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = async () => {
    if (!itemToDelete) return;

    const token = localStorage.getItem("token");
    const menuId = localStorage.getItem("menuId");

    try {
      await axios.delete(`http://localhost:8080/admin/menus/${menuId}/menuitems/${itemToDelete.id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      // Filter out the deleted item
      setMenuItems(prev => prev.filter(item => item.id !== itemToDelete.id));
      setShowDeleteModal(false);
      setItemToDelete(null);
    } catch (error) {
      console.error("Error deleting item:", error);
    }
  };
 


  const fetchMenus = async () => {
    const restaurantId = localStorage.getItem("restaurants");
    const menuId = localStorage.getItem("menuId");
      const token = localStorage.getItem("token");
      const menuResponse = await axios.get(`http://localhost:8080/admin/restaurants/${restaurantId}/menus/${menuId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMenus(menuResponse.data || "");
  }

  const handleAddMenuItem = async () => {
    setShowAddModal(true); 
  }


/* SEARCH QUERY *************************************************** */
const filteredMenuItems = menuItems.filter(item =>
  item.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
  item.description.toLowerCase().includes(searchQuery.toLowerCase())
);
/****************************************************************** */

  return (
    <>
      <NavBar />
      <div className="min-h-screen bg-darkred p-6">
        {/* Title Bar */}
        <div className="flex justify-between items-center bg-white p-4 rounded-md mx-7">
          <div>
            <h1 className="text-3xl font-bold">
              Menu Items for Menu {localStorage.getItem("menuId")} — {menus?.name || ""}
            </h1>
          </div>
          <div className="flex flex-row justify-evenly gap-4">
            <button className="flex bg-darkred text-white text-xl rounded-md p-2 cursor-pointer"
              onClick={handleAddMenuItem}
            >
              Add Menu Item
            </button>
            <input
              type="text"
              name="query"
              placeholder="Search..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="border border-gray-300 px-4 py-2 rounded-md w-72"
            />
          </div>
        </div>

        {/* Error Message */}
        {error && <p className="text-red-500 my-4">{error}</p>}

        {/* Content */}
        {loading ? (
          <div className="flex justify-center py-10">
            <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-gray-900"></div>
          </div>
        ) : filteredMenuItems.length > 0 ? (
          <div className="bg-darkred p-4 flex flex-wrap justify-center rounded-md mt-6 gap-4">
            {filteredMenuItems.map((item) => (
              <div
                key={item.id}
                className="bg-white p-4 rounded-lg w-110 h-80 shadow hover:scale-102 transform duration-100"
              >
                <div className="flex justify-between mb-2 px-2">
                  <h2 className="text-xl font-semibold">{item.name}</h2>
                  <p className="text-green-500 text-xl font-bold">${item.price}</p>
                </div>
                <p className="text-gray-700 flex-grow">{item.description}</p>

                <div className="flex justify-between">
                  <button
                    onClick={() => {
                    setEditingItem(item);
                    setIsModalOpen(true);
                  }}
                    className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md text-sm cursor-pointer w-full mx-2">
                    Edit
                  </button>
                  <button
                    onClick={() => handleDeleteClick(item)} 
                    className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-md text-sm cursor-pointer w-full mx-2"
                  > 
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <p className="text-center text-gray-500 py-10">No items match your search.</p>
        )}
        {isModalOpen && (
          <EditMenuItemModal
            isOpen={isModalOpen}
            onClose={() => setIsModalOpen(false)}
            item={editingItem}
            onSave={(updatedItem) => {
              const updatedList = menuItems.map((m) =>
                m.id === updatedItem.id ? updatedItem : m
              );
              setMenuItems(updatedList);
              setIsModalOpen(false);
            }}
          />
          )}
          <ConfirmDeleteModal
            isOpen={showDeleteModal}
            onClose={() => setShowDeleteModal(false)}
            onConfirm={handleConfirmDelete}
            item={itemToDelete}
          />
          {showAddModal && (
          <AddMenuItemModal
            onClose={() => setShowAddModal(false)}
            onSave={(newItem) => {
              setMenuItems((prev) => [...prev, newItem]);
              setShowAddModal(false);
            }}
          />
        )}
        </div>
      <Footer />
    </>
  );
}



export default MenuDetails;

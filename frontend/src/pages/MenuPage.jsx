import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import NavBar from "../components/NavBar";
import Footer from "../components/Footer";
import Sidebar from "../components/Sidebar";
import Switch from "@mui/material/Switch";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";

function MenuPage() {
  const [menus, setMenus] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const fetchMenus = async () => {
    setError(null);
    try {
      const token = localStorage.getItem("token");
      const restaurant = localStorage.getItem("restaurants");

      const res = await axios.get(`${import.meta.env.VITE_API_URL}/admin/restaurants/${restaurant}/menus`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setMenus(res.data.content || []);
    } catch (err) {
      console.error(err);
      setError("Failed to load menus.");
    }
  };

  useEffect(() => {
    fetchMenus()
  }, [])

  const handleMenuClick = (menuId) => {
    localStorage.setItem("menuId", menuId);
    navigate(`/admin/restaurants/menus/${menuId}`);
  };

  const handlePrimaryMenuToggle = async (menuId) => {
    const token = localStorage.getItem("token"); 
    const restaurantId = localStorage.getItem("restaurants");

    try {
      await axios.put(
        `${import.meta.env.VITE_API_URL}/admin/restaurants/${restaurantId}/menus/${menuId}/primary`,
        {}, // empty body
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      fetchMenus(); // refresh to reflect updated primary state
    } catch (err) {
      console.error(err);
      setError("Failed to set primary menu.");
    }
  }

  const handleAddMenu = async() =>
  {
    const token = localStorage.getItem('token');
    
    try {
      const res = await axios.post(
        `${import.meta.env.VITE_API_URL}/admin/restaurants/${restaurantId}/menus`,
        {
        headers: {
          Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log(res);
    } catch(err) {
      console.error(err);
      setError("failed to create menu");
    }
  }

    

  const handleDeleteMenu = async (menuId) => {
    const token = localStorage.getItem("token");
    const restaurantId = localStorage.getItem("restaurants");
    try {
      const res = await axios.delete(
        `${import.meta.env.VITE_API_URL}/admin/restaurants/${restaurantId}/menus/${menuId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Menu deleted:", res.status);
      fetchMenus(); // Refresh menus list
    } catch (err) {
      console.error(err);
      setError("Failed to delete menu.");
    }
  };

  return (
    <>
      <NavBar />
      <div className="flex min-h-screen">
      <Sidebar onSelect={"menus"}/>
      <div className="p-6 bg-gray-500 min-h-screen space-y-4 w-5/6">
          <button
            className="bg-darkred text-white px-4 py-2 rounded cursor-pointer"
            onClick={() => navigate("/admin/restaurants/dashboard")}
          >
            Back to Dashboard
          </button>
        <h1 className="text-black text-3xl py-2 font-bold">Menus</h1>
        <div className="flex flex-row justify-between">

          <button
            className="bg-darkred text-white px-4 py-2 rounded-md cursor-pointer"
            onClick={() => handleAddMenu()}
          >
            Create Menu
          </button>
        </div>
        <hr></hr>
        {error && <p className="text-red-500">{error}</p>}
        {menus.map((menu) => (
          <div
              key={menu.id}
                className="bg-gray-200 p-4 rounded-lg shadow-md border hover:scale-101 transition-transform cursor-pointer"
              >
                <div className="flex justify-between"> 
                  <h2 className="text-xl font-semibold py-2">{menu.name}</h2>
                  <div className="flex flex-col items-center gap-4"> 
                    <button 
                      className="cursor-pointer bg-darkred text-md text-white px-4 py-2 rounded-md ml-2 w-32"
                      onClick={() => handleDeleteMenu(menu.id)}
                      >Delete Menu
                    </button>
                    <button
                      className="cursor-pointer bg-blue-500 text-white text-md px-4 py-2 rounded-md ml-2 w-32"
                      onClick={() => handleMenuClick(menu.id)}
                      >View Menu
                      </button>
                    <FormGroup>
                    <FormControlLabel label="Primary menu" 
                    control={
                      <Switch 
                        checked={menu.isPrimary}
                        onChange={(e) => {
                          e.stopPropagation();
                          handlePrimaryMenuToggle(menu.id);
                        }} 
                      />
                    }
                    />
                    </FormGroup>
                    </div> 
                </div>
                  <p className="text-gray-600">{menu.description}</p>
              </div>
            ))}
          </div>
        </div>
      <Footer />
    </>
  );
}

export default MenuPage;
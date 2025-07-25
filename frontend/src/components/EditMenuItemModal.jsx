import { useState } from "react";
import axios from "axios";
import Switch from "@mui/material/Switch";
import FormControlLabel from "@mui/material/FormControlLabel";

function EditMenuItemModal({ item, menuId, onClose, onSave }) {
  const [formData, setFormData] = useState({
    id: item.id || "",
    name: item.name || "",
    description: item.description || "",
    imageUrl: item.imageUrl || "",
    price: item.price || "",
    category: item.category || "",
    enabled: item.enabled || false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    const id = formData.id
    try {
      await axios.put(
        `http://localhost:8080/admin/menuitems/${id}`,
        formData,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      onSave({ ...item, ...formData });
      onClose();
    } catch (err) {
      console.error("Update failed:", err);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex justify-center items-center z-50">
      <form
        onSubmit={handleSubmit}
        className="bg-white rounded p-6 shadow-md w-full max-w-md"
      >
        <h2 className="text-xl font-bold mb-4">Edit Menu Item</h2>

        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          placeholder="Name"
          className="w-full mb-2 p-2 border rounded"
        />

        <input
          type="text"
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Description"
          className="w-full mb-2 p-2 border rounded"
        />

        <input
          type="text"
          name="imageUrl"
          value={formData.imageUrl}
          onChange={handleChange}
          placeholder="Image URL"
          className="w-full mb-2 p-2 border rounded"
        />

        <input
          type="number"
          name="price"
          value={formData.price}
          onChange={handleChange}
          placeholder="Price"
          className="w-full mb-2 p-2 border rounded"
        />

        <input
          type="text"
          name="category"
          value={formData.category}
          onChange={handleChange}
          placeholder="Category"
          className="w-full mb-2 p-2 border rounded"
        />

        {/* ✅ Material UI Toggle */}
        <FormControlLabel
          control={
            <Switch
              checked={formData.enabled? true : false}
              onChange={(e) =>
                setFormData((prev) => ({
                  ...prev,
                  enabled: e.target.checked,
                }))
              }
              name="enabled"
              color="primary"
            />
          }
          label={formData.enabled ? "Enabled" : "Disabled"}
          className="my-2" 
        />

        <div className="flex justify-end gap-2">
          <button
            type="button"
            onClick={onClose}
            className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded cursor-pointer"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="bg-darkred text-white px-4 py-2 rounded cursor-pointer"
          >
            Save
          </button>
        </div>
      </form>
    </div>
  );
}

export default EditMenuItemModal;

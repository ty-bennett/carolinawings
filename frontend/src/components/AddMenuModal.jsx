import { useState} from "react";
import axios from "axios";

function AddMenuModal({ onClose, onSave }) {
  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    const restaurantId = localStorage.getItem("restaurants"); 

    try {
      const response = await axios.post(
        `http://localhost:8080/admin/restaurants/${restaurantId}/menus`,
        formData,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      onSave(response.data); // Pass new menu back to parent
      onClose();
    } catch (err) {
      console.error("Failed to create menu:", err);
    } 
  };

  return (
    <div className="fixed inset-0 bg-black/50 flex justify-center items-center z-50 h-full w-full">
      <form
        onSubmit={handleSubmit}
        className="bg-white rounded p-6 shadow-md w-full max-w-md"
      >
        <h2 className="text-xl font-bold mb-4">Create New Menu</h2>

        <input
          type="text"
          name="name"
          value={formData.name}
          onChange={handleChange}
          placeholder="Menu Name"
          className="w-full mb-3 p-2 border rounded"
          required
        />

        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          placeholder="Menu Description"
          className="w-full mb-3 p-2 border rounded"
          rows={3}
          required
        />

        {/* No isPrimary or items shown */}
        <div className="flex justify-end gap-2 mt-4">
          <button
            type="button"
            onClick={onClose}
            className="bg-gray-400 hover:bg-gray-500 text-white px-4 py-2 rounded"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="bg-darkred text-white px-4 py-2 rounded"
          >
            Create
          </button>
        </div>
      </form>
    </div>
  );
}

export default AddMenuModal;
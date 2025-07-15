import { jsx } from "react/jsx-runtime";
import { useState, useEffect } from "react";

const EditMenuItemModal = ({ isOpen, onClose, item, onSave }) => {
  const [name, setName] = useState(item?.name || "");
  const [price, setPrice] = useState(item?.price || "");
  const [description, setDescription] = useState(item?.description || "");
  

  useEffect(() => {
    if (item) {
      setName(item.name);
      setPrice(item.price);
      setDescription(item.description);
    }
  }, [item]);

  const handleSubmit = () => {
    const updatedItem = { ...item, name, price, description };
    onSave(updatedItem);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white p-6 rounded-lg shadow-lg w-[90%] max-w-lg">
        <h2 className="text-xl font-semibold mb-4">Edit Menu Item</h2>

        <label className="block mb-2">Name</label>
        <input
          type="text"
          className="w-full border border-gray-300 p-2 rounded mb-4"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />

        <label className="block mb-2">Price</label>
        <input
          type="number"
          className="w-full border border-gray-300 p-2 rounded mb-4"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
        />

        <label className="block mb-2">Description</label>
        <textarea
          className="w-full border border-gray-300 p-2 rounded mb-4"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />

        <div className="flex justify-end gap-2">
          <button
            onClick={onClose}
            className="bg-gray-300 hover:bg-gray-400 text-black px-4 py-2 rounded"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit}
            className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded"
          >
            Save
          </button>
        </div>
      </div>
    </div>
  );
};

export default EditMenuItemModal;
import { useState } from 'react';
import { MenuItem, AddCartItemRequest, SelectedOptionGroup } from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

interface MenuItemModalProps {
  item: MenuItem;
  onClose: () => void;
}

function MenuItemModal({ item, onClose }: MenuItemModalProps) {
  const [quantity, setQuantity] = useState(1);
  const [memos, setMemos] = useState('');
  const [selectedOptions, setSelectedOptions] = useState<Record<string, number[]>>({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  const { addItem } = useCart();
  const { isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleOptionChange = (optionGroupId: string, optionId: number, isMultiple: boolean) => {
    setSelectedOptions(prev => {
      const current = prev[optionGroupId] || [];
      
      if (isMultiple) {
        // Toggle for checkboxes
        if (current.includes(optionId)) {
          return { ...prev, [optionGroupId]: current.filter(id => id !== optionId) };
        } else {
          return { ...prev, [optionGroupId]: [...current, optionId] };
        }
      } else {
        // Replace for radio buttons
        return { ...prev, [optionGroupId]: [optionId] };
      }
    });
  };

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const selectedOptionGroups: SelectedOptionGroup[] = Object.entries(selectedOptions)
        .filter(([_, optionIds]) => optionIds.length > 0)
        .map(([groupId, optionIds]) => ({
          optionGroupId: Number(groupId),
          selectedOptionIds: optionIds,
        }));

      const request: AddCartItemRequest = {
        menuItemId: item.id,
        quantity,
        memos: memos || undefined,
        selectedOptionGroups,
      };

      await addItem(request);
      onClose();
    } catch (err: any) {
      console.error(err);
      setError(err.response?.data?.message || 'Failed to add item to cart');
    } finally {
      setLoading(false);
    }
  };

  // Calculate total price
  const calculateTotal = () => {
    let total = item.price * quantity;
    
    item.optionGroups?.forEach(og => {
      const selected = selectedOptions[og.optionGroup.id] || [];
      og.optionGroup.options.forEach(opt => {
        if (selected.includes(Number(opt.id)) && opt.price) {
          total += opt.price * quantity;
        }
      });
    });
    
    return total;
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-lg w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="p-4 border-b sticky top-0 bg-white">
          <div className="flex justify-between items-start">
            <h2 className="text-xl font-bold">{item.name}</h2>
            <button 
              onClick={onClose}
              className="text-gray-500 hover:text-gray-700 text-2xl"
            >
              &times;
            </button>
          </div>
          <p className="text-gray-600 mt-1">{item.description}</p>
          <p className="text-darkred font-bold mt-2">${item.price.toFixed(2)}</p>
        </div>

        {/* Option Groups */}
        <div className="p-4">
          {item.optionGroups?.map(og => (
            <div key={og.id} className="mb-6">
              <div className="flex justify-between items-center mb-2">
                <h3 className="font-semibold">{og.optionGroup.name}</h3>
                {og.required&& (
                  <span className="text-red-500 text-sm">Required</span>
                )}
              </div>
              <p className="text-gray-500 text-sm mb-2">
                {og.minChoices === og.maxChoices 
                  ? `Select ${og.minChoices}` 
                  : `Select ${og.minChoices} to ${og.maxChoices}`}
              </p>
              
              <div className="space-y-2">
                {og.optionGroup.options.map(opt => {
                  const isMultiple = og.maxChoices > 1;
                  const isSelected = (selectedOptions[og.optionGroup.id] || []).includes(Number(opt.id));
                  
                  return (
                    <label 
                      key={opt.id}
                      className="flex items-center justify-between p-2 border rounded hover:bg-gray-50 cursor-pointer"
                    >
                      <div className="flex items-center gap-2">
                        <input
                          type={isMultiple ? 'checkbox' : 'radio'}
                          name={`option-group-${og.optionGroup.id}`}
                          checked={isSelected}
                          onChange={() => handleOptionChange(og.optionGroup.id, Number(opt.id), isMultiple)}
                          className="w-4 h-4"
                        />
                        <span>{opt.name}</span>
                      </div>
                      {opt.price && opt.price > 0 && (
                        <span className="text-gray-500">+${opt.price.toFixed(2)}</span>
                      )}
                    </label>
                  );
                })}
              </div>
            </div>
          ))}

          {/* Special Instructions */}
          <div className="mb-6">
            <h3 className="font-semibold mb-2">Special Instructions</h3>
            <textarea
              value={memos}
              onChange={(e) => setMemos(e.target.value)}
              placeholder="Any special requests?"
              className="w-full border rounded p-2 h-20 resize-none"
            />
          </div>

          {/* Quantity */}
          <div className="mb-6">
            <h3 className="font-semibold mb-2">Quantity</h3>
            <div className="flex items-center gap-4">
              <button
                onClick={() => setQuantity(q => Math.max(1, q - 1))}
                className="w-10 h-10 rounded-full bg-gray-200 hover:bg-gray-300 text-xl font-bold"
              >
                -
              </button>
              <span className="text-xl font-semibold">{quantity}</span>
              <button
                onClick={() => setQuantity(q => q + 1)}
                className="w-10 h-10 rounded-full bg-gray-200 hover:bg-gray-300 text-xl font-bold"
              >
                +
              </button>
            </div>
          </div>

          {error && <p className="text-red-500 mb-4">{error}</p>}
        </div>

        {/* Footer */}
        <div className="p-4 border-t sticky bottom-0 bg-white">
          <button
            onClick={handleAddToCart}
            disabled={loading}
            className="w-full bg-darkred text-white py-3 rounded-lg font-semibold hover:bg-red-800 transition disabled:opacity-50"
          >
            {loading ? 'Adding...' : `Add to Cart - $${calculateTotal().toFixed(2)}`}
          </button>
        </div>
      </div>
    </div>
  );
}

export default MenuItemModal;
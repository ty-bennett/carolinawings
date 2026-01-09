import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { cartAPI, Cart, AddCartItemRequest } from '../services/api';
import { useAuth } from './AuthContext';

interface CartContextType {
  cart: Cart | null;
  isLoading: boolean;
  error: string | null;
  itemCount: number;
  fetchCart: () => Promise<void>;
  addItem: (data: AddCartItemRequest) => Promise<void>;
  updateItem: (cartItemId: number, quantity: number) => Promise<void>;
  removeItem: (cartItemId: number) => Promise<void>;
  clearCart: () => Promise<void>;
  resetCart: () => void;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export function CartProvider({ children }: { children: ReactNode }) {
  const [cart, setCart] = useState<Cart | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { isAuthenticated, user } = useAuth();

  const fetchCart = async () => {
    if (!isAuthenticated) return;

    setIsLoading(true);
    setError(null);
    try {
      const { data } = await cartAPI.getCart();
      setCart(data);
    } catch (err: any) {
      console.error('Failed to fetch cart:', err);
      // If cart not found (no active cart), just set cart to null
      if (err.response?.status === 404) {
        setCart(null);
      } else {
        setError('Failed to load cart');
      }
    } finally {
      setIsLoading(false);
    }
  };

  const addItem = async (data: AddCartItemRequest) => {
    setIsLoading(true);
    setError(null);
    try {
      const { data: updatedCart } = await cartAPI.addItem(data);
      setCart(updatedCart);
    } catch (err: any) {
      console.error('Failed to add item:', err);
      setError(err.response?.data?.message || 'Failed to add item');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const updateItem = async (cartItemId: number, quantity: number) => {
    setIsLoading(true);
    setError(null);
    try {
      const { data: updatedCart } = await cartAPI.updateItem(cartItemId, quantity);
      setCart(updatedCart);
    } catch (err) {
      console.error('Failed to update item:', err);
      setError('Failed to update item');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const removeItem = async (cartItemId: number) => {
    setIsLoading(true);
    setError(null);
    try {
      const { data: updatedCart } = await cartAPI.removeItem(cartItemId);
      setCart(updatedCart);
    } catch (err) {
      console.error('Failed to remove item:', err);
      setError('Failed to remove item');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const clearCart = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const { data: updatedCart } = await cartAPI.clearCart();
      setCart(updatedCart);
    } catch (err) {
      console.error('Failed to clear cart:', err);
      setError('Failed to clear cart');
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  const resetCart = () => {
    setCart(null);
  };

  // Fetch cart when user logs in
  useEffect(() => {
    if (isAuthenticated && user && user.roles.includes('USER')) {
      fetchCart();
    } else {
      setCart(null);
    }
  }, [isAuthenticated, user]);

  const itemCount = cart?.cartItems?.reduce((sum, item) => sum + item.quantity, 0) ?? 0;

  return (
    <CartContext.Provider value={{
      cart,
      isLoading,
      error,
      itemCount,
      fetchCart,
      addItem,
      updateItem,
      removeItem,
      clearCart,
      resetCart,
    }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (context === undefined) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
}

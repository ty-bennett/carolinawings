import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

//Types for API interaction

export interface User {
  id: string;
  username: string;
  name: string;
  roles: string[];
  restaurants: number[];
}

export interface LoginResponse {
  id: string;
  name: string;
  username: string;
  jwtToken: string;
  roles: string[];
  restaurants: number[];
}

export interface Restaurant {
  id: number;
  name: string;
  address: string;
  phone?: string;
  email?: string;
  status: string;
  acceptingOrders: boolean;
  estimatedPickupMinutes: number;
  hours: RestaurantHours[];
}

export interface RestaurantHours {
  dayOfWeek: string;
  openTime: string;
  closeTime: string;
}

export interface Menu {
  id: number;
  name: string;
  description?: string;
  isPrimary: boolean;
  menuItemsList: MenuItem[];
}

export interface MenuItem {
  id: number;
  name: string;
  description?: string;
  price: number;
  category: string;
  imageUrl?: string;
  enabled: boolean;
  optionGroups: MenuItemOptionGroup[];
}

export interface MenuItemOptionGroup {
  id: string;
  optionType: string;
  minChoices: number;
  maxChoices: number;
  optionGroup: OptionGroup;
  required: boolean;
}

export interface OptionGroup {
  id: string;
  name: string;
  options: MenuItemOption[];
}

export interface MenuItemOption {
  id: string;
  name: string;
  price?: number;
  defaultSelected: boolean;
}

export interface Cart {
  cartId: number;
  totalPrice: number;
  cartItems: CartItem[];
}

export interface CartItem {
  cartItemId: number;
  menuItem: MenuItem;
  quantity: number;
  price: number;
  memos?: string;
  options: CartItemOption[];
}

export interface CartItemOption {
  id: string;
  optionGroupName: string;
  optionName: string;
  price?: number;
}

export interface AddCartItemRequest {
  menuItemId: number;
  quantity: number;
  memos?: string;
  selectedOptionGroups: SelectedOptionGroup[];
}

export interface SelectedOptionGroup {
  optionGroupId: number;
  selectedOptionIds: number[];
}

export interface Order {
  id: string;
  status: string;
  subtotal: number;
  totalTax: number;
  totalPrice: number;
  createdAt: string;
  updatedAt: string;
  pickupTime: string;
  customerName: string;
  customerPhone: string;
  customerNotes?: string;
  orderType: string;
  restaurantId: number;
  restaurantName: string;
  items: OrderItem[];
}

export interface OrderItem {
  id: number;
  menuItemId: number;
  menuItemName: string;
  unitPrice: number;
  quantity: number;
  lineTotal: number;
  options: OrderItemOption[];
}

export interface OrderItemOption {
  optionId: number;
  optionGroupName: string;
  optionName: string;
  extraPrice: number;
}

export interface CreateOrderRequest {
  cartId: number;
  restaurantId: number;
  customerName: string;
  customerPhone: string;
  customerNotes?: string;
  requestedPickupTime?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
};

// Axios instance

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": 'application/json',
  },
});

// Attach jwtToken to every request 
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  console.log('Request interceptor:', config.url, 'Token exists:', token?.substring(0, 20));
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// handle 401 errors (unauthorized) and redirect to login 
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.log('API error interceptor:', error.response?.status, error.config?.url)
    return Promise.reject(error);
  }
)

// Auth API
export const authAPI = {
  login: (username: string, password: string) =>
    api.post<LoginResponse>('/auth/login', { username, password }),
  register: (data: { firstName: string; lastName: string; username: string; password: string; phoneNumber: string; newsletterMember: boolean }) =>
    api.post('/auth/register', data),
};

// Public API
export const publicAPI = {
  getRestaurants: () => api.get<PaginatedResponse<Restaurant>>('/public/restaurants'),
  getRestaurant: (id: number) => api.get<Restaurant>(`/public/restaurants/${id}`),
  getMenus: (restaurantId: number) => api.get<PaginatedResponse<Menu>>(`/public/restaurants/${restaurantId}/menus`),
  getMenuItems: (menuId: number) => api.get<PaginatedResponse<MenuItem>>(`/public/menus/${menuId}/items`),
};

// Cart API
export const cartAPI = {
  getCart: () => api.get<Cart>('/cart'),
  addItem: (data: AddCartItemRequest) => api.post<Cart>('/cart/items', data),
  updateItem: (cartItemId: number, quantity: number) =>
    api.patch<Cart>(`/cart/items/${cartItemId}?quantity=${quantity}`),
  removeItem: (cartItemId: number) => api.delete<Cart>(`/cart/items/${cartItemId}`),
  clearCart: () => api.delete<Cart>('/cart'),
};

// Orders API
export const orderAPI = {
  createOrder: (data: CreateOrderRequest) => api.post<Order>('/orders', data),
  getOrder: (orderId: string) => api.get<Order>(`/orders/${orderId}`),
  cancelOrder: (orderId: string) => api.patch<Order>(`/orders/${orderId}/cancel`),
};

// Admin API
export const adminAPI = {
  getRestaurants: () => api.get<Restaurant[]>('/admin/restaurants'),
  getOrders: (restaurantId: number, page = 0, size = 20) =>
    api.get(`/admin/restaurants/${restaurantId}/orders?page=${page}&pageSize=${size}`),
  updateOrderStatus: (orderId: string, status: string) =>
    api.patch(`/admin/orders/${orderId}/status?status=${status}`),
  createMenu: (restaurantId: number, data: { name: string; description?: string }) =>
    api.post(`/admin/restaurants/${restaurantId}/menus`, data),
  deleteMenu: (restaurantId: number, menuId: number) =>
    api.delete(`/admin/restaurants/${restaurantId}/menus/${menuId}`),
  createMenuItem: (menuId: number, data: Partial<MenuItem>) =>
    api.post(`/admin/menus/${menuId}/menuitems`, data),
  updateMenuItem: (menuItemId: number, data: Partial<MenuItem>) =>
    api.put(`/admin/menuitems/${menuItemId}`, data),
  deleteMenuItem: (menuItemId: number) =>
    api.delete(`/admin/menuitems/${menuItemId}`),
};

export default api;



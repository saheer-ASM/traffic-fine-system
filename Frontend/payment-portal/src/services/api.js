import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authService = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (data) => api.post('/auth/register', data),
};

export const fineService = {
  getFineByReference: (reference) => api.get(`/fines/reference/${reference}`),
  getDriverFines: (driverId, page = 0, size = 20) => 
    api.get(`/fines/driver/${driverId}`, { params: { page, size } }),
  getPaymentHistory: (userId, page = 0, size = 20) => 
    api.get(`/payments/history/${userId}`, { params: { page, size } }),
};

export const paymentService = {
  processPayment: (data) => api.post('/payments', data),
};

export default api;

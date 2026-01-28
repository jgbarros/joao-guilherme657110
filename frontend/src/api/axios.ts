import axios from 'axios';
import { showToast } from '../utils/toastService';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

// Interceptor para adicionar token JWT em todas as requisições
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para tratar erros globalmente
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const data = error.response?.data;
    
    // Não redirecionar se o erro ocorrer na rota de autenticação
    const isAuthRoute = error.config?.url?.includes('/api/auth/authenticate');
    
    if (status === 401 && !isAuthRoute) {
      localStorage.removeItem('token');
      window.location.href = '/';
      return Promise.reject(error);
    }

    // Tratamento de mensagens globais
    if (status) {
      let message = '';
      
      if (data) {
        if (typeof data === 'string') {
          message = data;
        } else if (data.message) {
          message = data.message;
        } else if (data.error) {
          message = data.error;
        }
      }

      if (status === 429) {
        showToast('error', 'Limite Excedido', message || 'Muitas requisições. Tente novamente mais tarde.');
      } else if (status === 403) {
        showToast('error', 'Acesso Negado', message || 'Você não tem permissão para realizar esta ação.');
      } else if (isAuthRoute && (status === 401 || status === 403)) {
        showToast('error', 'Falha na Autenticação', 'Usuário ou senha incorretos.');
      } else if (status >= 500) {
        showToast('error', 'Erro no Servidor', 'Ocorreu um erro no servidor. Tente novamente mais tarde.');
      } else if (status >= 400 && !isAuthRoute) {
         // Para outros erros 4xx que não sejam auth
         showToast('error', 'Erro', message || 'Ocorreu um erro inesperado.');
      }
    } else if (error.message === 'Network Error') {
      showToast('error', 'Erro de Rede', 'Não foi possível conectar ao servidor.');
    }

    return Promise.reject(error);
  }
);

export default api;

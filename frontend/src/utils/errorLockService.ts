import { createRef } from 'react';

export interface ErrorLockHandle {
  show: (title: string, message: string) => void;
}

export const errorLockRef = createRef<ErrorLockHandle>();

export const lockScreen = (title: string, message: string) => {
  if (errorLockRef.current) {
    errorLockRef.current.show(title, message);
  } else {
    // Fallback caso o componente não esteja montado (ex: erro muito cedo)
    console.error('ErrorLockDialog not mounted', title, message);
    alert(`${title}: ${message}`);
    localStorage.removeItem('token');
    window.location.href = '/';
  }
};

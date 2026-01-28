import { Toast } from 'primereact/toast';
import { createRef } from 'react';

export const toastRef = createRef<Toast>();

export const showToast = (severity: 'success' | 'info' | 'warn' | 'error', summary: string, detail: string) => {
  toastRef.current?.show({ severity, summary, detail });
};

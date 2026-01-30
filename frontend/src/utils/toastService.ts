import { Toast } from 'primereact/toast';
import { createRef } from 'react';

export const toastRef = createRef<Toast>();

let lastToast = {
  severity: '',
  summary: '',
  detail: '',
  time: 0
};

export const showToast = (severity: 'success' | 'info' | 'warn' | 'error', summary: string, detail: string) => {
  const now = Date.now();
  const timeThreshold = 3000; // 3 segundos para evitar duplicatas

  if (
    lastToast.severity === severity &&
    lastToast.summary === summary &&
    lastToast.detail === detail &&
    now - lastToast.time < timeThreshold
  ) {
    return; // Ignora toast duplicado
  }

  lastToast = { severity, summary, detail, time: now };
  toastRef.current?.show({ severity, summary, detail, life: 3000 });
};

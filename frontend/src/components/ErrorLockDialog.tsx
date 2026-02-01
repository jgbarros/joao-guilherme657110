import React, { useState, useImperativeHandle, useEffect } from 'react';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { errorLockRef } from '../utils/errorLockService';

const ErrorLockDialog: React.FC = () => {
  const [visible, setVisible] = useState(false);
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState('');

  useImperativeHandle(errorLockRef, () => ({
    show: (title: string, message: string) => {
      setTitle(title);
      setMessage(message);
      setVisible(true);
    }
  }));

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/';
  };

  return (
    <Dialog
      header={title}
      visible={visible}
      style={{ width: '50vw' }}
      modal
      closable={false} // Impede fechar clicando fora ou no X
      onHide={() => {}} // No-op obrigatório
      footer={
        <div>
          <Button label="Sair" icon="pi pi-power-off" onClick={handleLogout} autoFocus />
        </div>
      }
    >
      <p className="m-0">
        {message}
      </p>
    </Dialog>
  );
};

export default ErrorLockDialog;

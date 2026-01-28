import { useState, useRef } from 'react';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import { useNavigate } from 'react-router-dom';
import api from '../api/axios';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);

  const handleLogin = async () => {
    if (!username || !password) {
      toast.current?.show({
        severity: 'warn',
        summary: 'Campos obrigatórios',
        detail: 'Por favor, informe o usuário e a senha.',
        life: 3000
      });
      return;
    }

    setLoading(true);
    try {
      const response = await api.post('/api/auth/authenticate', { username, password });
      const { token } = response.data;
      localStorage.setItem('token', token);
      navigate('/dashboard');
    } catch (error: any) {
      console.error('Erro no login:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex align-items-center justify-content-center min-h-screen surface-ground">
      <Toast ref={toast} />
      <Card title="Login" className="w-30rem shadow-4">
        <div className="p-fluid">
          <div className="field mb-4">
            <label htmlFor="username" className="font-bold block mb-2">Username</label>
            <InputText 
              id="username" 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
              disabled={loading}
            />
          </div>
          <div className="field mb-4">
            <label htmlFor="password" className="font-bold block mb-2">Senha</label>
            <Password 
              id="password" 
              value={password} 
              onChange={(e) => setPassword(e.target.value)} 
              feedback={false} 
              toggleMask
              disabled={loading}
            />
          </div>
          <Button label="Entrar" onClick={handleLogin} severity="info" loading={loading} />
        </div>
      </Card>
    </div>
  );
}

import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Password } from 'primereact/password';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import api from './api/axios';

export default function Login() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await api.post('/api/auth/authenticate', { username, password });
      const { token } = response.data;
      localStorage.setItem('token', token);
      navigate('/dashboard');
    } catch (error) {
      console.error('Erro no login:', error);
      // Mostre toast de erro com Message de PrimeReact
    }
  };

  return (
    <div className="flex align-items-center justify-content-center min-h-screen surface-ground">
      <Card title="Login" className="w-30rem shadow-4">
        <div className="p-fluid">
          <div className="field mb-4">
            <label htmlFor="username" className="font-bold block mb-2">Username</label>
            <InputText 
              id="username" 
              value={username} 
              onChange={(e) => setUsername(e.target.value)} 
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
            />
          </div>
          <Button label="Entrar" onClick={handleLogin} severity="info" />
        </div>
      </Card>
    </div>
  );
}

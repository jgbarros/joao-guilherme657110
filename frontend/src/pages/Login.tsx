import React, { useState } from 'react';
import { Card } from 'primereact/card';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';
import { Password } from 'primereact/password';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log('Login attempt:', { email, password });
  };

  return (
    <div className="flex items-center justify-center min-h-screen w-full p-4 bg-gray-50">
      <Card title="Login" className="w-full max-w-md shadow-2xl">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="field">
            <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
              Email
            </label>
            <InputText
              id="email"
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full"
              placeholder="Digite seu email"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
              Senha
            </label>
            <Password
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full"
              placeholder="Digite sua senha"
              feedback={false}
              toggleMask
              required
            />
          </div>

          <Button
            type="submit"
            label="Entrar"
            className="w-full mt-6"
            severity="success"
          />
        </form>
      </Card>
    </div>
  );
};

export default Login;

import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  return (
    <div className="flex align-items-center justify-content-center min-h-screen">
      <Card title="Dashboard" className="w-50rem">
        <div className="text-center mb-4">
          <h2>Bem-vindo ao Dashboard!</h2>
          <p>Você está autenticado com sucesso.</p>
        </div>

        {token && (
          <Card className="mb-4" title="Token de Autenticação">
            <div className="field">
              <label htmlFor="token" className="font-bold">Seu Token JWT:</label>
              <div className="mt-2">
                <div 
                  style={{
                    backgroundColor: '#f8f9fa',
                    border: '1px solid #dee2e6',
                    borderRadius: '4px',
                    padding: '12px',
                    fontFamily: 'monospace',
                    fontSize: '12px',
                    wordBreak: 'break-all',
                    maxHeight: '150px',
                    overflowY: 'auto'
                  }}
                >
                  {token}
                </div>
              </div>
              <div className="mt-2">
                <small className="text-500">
                  Este token é usado para autenticar requisições à API
                </small>
              </div>
            </div>
          </Card>
        )}
        
        <div className="grid">
          <div className="col-12 md:col-4">
            <Card className="text-center">
              <h3>Estatísticas</h3>
              <p>Visualize suas estatísticas aqui</p>
            </Card>
          </div>
          <div className="col-12 md:col-4">
            <Card className="text-center">
              <h3>Relatórios</h3>
              <p>Acesse seus relatórios</p>
            </Card>
          </div>
          <div className="col-12 md:col-4">
            <Card className="text-center">
              <h3>Configurações</h3>
              <p>Gerencie suas configurações</p>
            </Card>
          </div>
        </div>

        <div className="text-center mt-4">
          <Button 
            label="Gerenciar Artistas" 
            icon="pi pi-users" 
            onClick={() => navigate('/artistas')}
            className="mr-2"
          />
          <Button 
            label="Sair" 
            onClick={handleLogout} 
            className="p-button-danger" 
          />
        </div>
      </Card>
    </div>
  );
}

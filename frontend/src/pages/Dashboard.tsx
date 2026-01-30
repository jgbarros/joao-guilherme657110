import { Card } from 'primereact/card';
import { Button } from 'primereact/button';
import { Divider } from "primereact/divider";
import { useNavigate } from 'react-router-dom';
import StatCard from "../components/StatCard.tsx";
import { useEffect, useState } from 'react';
import DashboardFacade from '../facades/DashboardFacade';
import type { DashboardStats } from '../facades/DashboardFacade';

export default function Dashboard() {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const [stats, setStats] = useState<DashboardStats>({
    totalArtistas: 0,
    totalAlbuns: 0,
    totalRegionais: 0,
    totalRegionaisApi: 0
  });

  useEffect(() => {
    if (!token) {
        navigate('/');
    }
  }, [token, navigate]);

  useEffect(() => {
    if (!token) return;

    const subscription = DashboardFacade.stats$.subscribe(setStats);
    DashboardFacade.fetchStats();

    return () => subscription.unsubscribe();
  }, [token]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  if (!token) {
    return null;
  }

  return (
      <div className="flex align-items-center justify-content-center min-h-screen p-3 surface-ground">
        <Card title="Dashboard" className="w-full md:w-11 shadow-2">
          <div className="p-0">
                  <div className="mb-4">
                      <p className="text-500">Gerencie seus artistas, álbuns e consulte regionais</p>
                  </div>

                  <div className="flex flex-column md:flex-row align-items-start gap-4 p-2 w-full max-w-6xl mx-auto">
                      <StatCard title="Artistas" value={stats.totalArtistas} icon="pi-users" color="#3b82f6"/>
                      <StatCard title="Álbuns" value={stats.totalAlbuns} icon="pi-folder" color="#22c55e"/>
                      <StatCard title="Regionais (Local)" value={stats.totalRegionais} icon="pi-map-marker" color="#e67e22"/>
                      <StatCard title="Regionais (Api)" value={stats.totalRegionaisApi} icon="pi-map-marker" color="#c0392b"/>

                      <div className="col-12 md:col-4">
                          <Card title={<><i className="pi pi-bolt mr-2"/>Ações Rápidas</>}>
                              <div className="flex flex-column gap-3">
                                  <Button
                                      label="Gerenciar Artistas"
                                      icon="pi pi-users"
                                      onClick={() => navigate('/artistas')}
                                      className="p-button-outlined"
                                  />

                                  <Button
                                      label="Gerenciar Álbuns"
                                      icon="pi pi-book"
                                      onClick={() => navigate('/albuns')}
                                      className="p-button-outlined"
                                  />

                              </div>
                          </Card>
                      </div>
                  </div>


                  <Divider/>

                  <div className="flex flex-column md:flex-row gap-4 p-2 w-full max-w-6xl mx-auto">
                      <div className="col-12 md:col-6">
                          <Card title="Sobre o Sistema">
                              <p className="text-700 mb-3">
                                  Este sistema permite gerenciar seu catálogo musical de forma simples
                                  e eficiente.
                              </p>
                              <ul className="pl-3 text-600">
                                  <li>Cadastre e gerencie artistas</li>
                                  <li>Organize álbuns por artista</li>
                                  <li>Consultar informações Regionais da API: <a href="https://integrador-argus-api.geia.vip/v1/regionais" target="_blank" rel="noopener noreferrer">Regionais</a></li>
                              </ul>
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
                          label="Gerenciar Álbuns"
                          icon="pi pi-book"
                          onClick={() => navigate('/albuns')}
                          className="mr-2"
                      />
                      <Button
                          label="Sair"
                          icon="pi pi-sign-out"
                          onClick={handleLogout}
                          className="p-button-danger"
                      />
                  </div>
              </div>
        </Card>
      </div>
  );
}
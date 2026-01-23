import { useState, useEffect } from 'react';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import api from './api/axios';

interface Artista {
  id: number;
  nome: string;
  biografia?: string;
  dataNascimento?: string;
  dataMorte?: string;
  nacionalidade?: string;
  albumCount?: number;
}

interface PaginatedResponse {
  content: Artista[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: {
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    unpaged: boolean;
  };
  size: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  totalElements: number;
  totalPages: number;
}

export default function ArtistList() {
  const [artistas, setArtistas] = useState<Artista[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0
  });
  const navigate = useNavigate();

  useEffect(() => {
    fetchArtistas();
  }, []);

  const fetchArtistas = async (page: number = 0, size: number = 10) => {
    try {
      const response = await api.get(`/api/artistas?page=${page}&size=${size}`);
      const data: PaginatedResponse = response.data;
      setArtistas(data.content);
      setPagination({
        page: data.number,
        size: data.size,
        totalElements: data.totalElements,
        totalPages: data.totalPages
      });
      setError(null);
    } catch (err: any) {
      console.error('Erro ao buscar artistas:', err);
      setError('Erro ao carregar artistas. Verifique se o backend está rodando.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const handleBackToDashboard = () => {
    navigate('/dashboard');
  };

  const actionBodyTemplate = (rowData: Artista) => {
    return (
      <div className="flex gap-2">
        <Button 
          icon="pi pi-pencil" 
          className="p-button-rounded p-button-success p-button-text" 
          onClick={() => console.log('Editar:', rowData)}
        />
        <Button 
          icon="pi pi-trash" 
          className="p-button-rounded p-button-danger p-button-text" 
          onClick={() => console.log('Excluir:', rowData)}
        />
      </div>
    );
  };

  return (
    <div className="p-4">
      <Card title="Lista de Artistas" className="mb-4">
        <div className="flex justify-content-between align-items-center mb-4">
          <Button 
            label="Voltar" 
            icon="pi pi-arrow-left" 
            className="p-button-outlined" 
            onClick={handleBackToDashboard}
          />
          <div className="text-center">
            <span className="text-600">
              Mostrando {pagination.totalElements > 0 ? (pagination.page * pagination.size) + 1 : 0} a {Math.min((pagination.page + 1) * pagination.size, pagination.totalElements)} de {pagination.totalElements} artistas
            </span>
          </div>
          <Button 
            label="Sair" 
            icon="pi pi-sign-out" 
            className="p-button-danger" 
            onClick={handleLogout}
          />
        </div>

        {error && (
          <div className="p-message p-message-error mb-4">
            <div className="p-message-wrapper">
              <span className="p-message-icon pi pi-times-circle"></span>
              <span className="p-message-text">{error}</span>
            </div>
          </div>
        )}

        <DataTable 
          value={artistas} 
          loading={loading}
          emptyMessage="Nenhum artista encontrado"
          responsiveLayout="scroll"
          className="p-datatable-sm"
          lazy
          paginator
          rows={pagination.size}
          totalRecords={pagination.totalElements}
          onPage={(event) => fetchArtistas(event.page, event.rows)}
        >
          <Column field="id" header="ID" sortable style={{ width: '80px' }} />
          <Column field="nome" header="Nome" sortable />
          <Column field="biografia" header="Biografia" sortable />
          <Column field="nacionalidade" header="Nacionalidade" sortable />
          <Column field="albumCount" header="Álbuns" sortable style={{ width: '100px', textAlign: 'center' }} />
          <Column 
            field="dataNascimento" 
            header="Nascimento" 
            sortable 
            body={(rowData) => {
              if (rowData.dataNascimento) {
                return new Date(rowData.dataNascimento).toLocaleDateString('pt-BR');
              }
              return '-';
            }}
            style={{ width: '120px' }}
          />
          <Column 
            header="Ações" 
            body={actionBodyTemplate} 
            style={{ width: '120px', textAlign: 'center' }}
          />
        </DataTable>

        <div className="mt-4">
          <Button 
            label="Adicionar Artista" 
            icon="pi pi-plus" 
            className="p-button-success"
            onClick={() => console.log('Adicionar novo artista')}
          />
        </div>
      </Card>
    </div>
  );
}

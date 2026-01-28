import { useState, useEffect, useRef } from 'react';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Button } from 'primereact/button';
import { useNavigate } from 'react-router-dom';
import { confirmDialog, ConfirmDialog } from 'primereact/confirmdialog';
import { Toast } from 'primereact/toast';
import { Dialog } from 'primereact/dialog';
import { Image } from 'primereact/image';
import api from '../api/axios';
import AlbumForm from './AlbumForm';

interface Album {
  id: number;
  titulo: string;
  anoLancamento: number;
  genero: string;
  capaUrl: string;
  faixas: string;
  artistaId: number;
  artistaNome: string;
  regionalId: number;
  regionalNome: string;
}

interface PaginatedResponse {
  content: Album[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  pageable: any;
  size: number;
  sort: any;
  totalElements: number;
  totalPages: number;
}

export default function AlbumList() {
  const [albuns, setAlbuns] = useState<Album[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [pagination, setPagination] = useState({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0
  });
  const [dialogVisible, setDialogVisible] = useState(false);
  const [selectedAlbumId, setSelectedAlbumId] = useState<number | null>(null);
  const navigate = useNavigate();
  const toast = useRef<Toast>(null);

  useEffect(() => {
    fetchAlbuns(pagination.page, pagination.size);
  }, []);

  const fetchAlbuns = async (page: number = 0, size: number = 10) => {
    setLoading(true);
    try {
      const response = await api.get(`/api/albuns?page=${page}&size=${size}`);
      const data: PaginatedResponse = response.data;
      setAlbuns(data.content);
      setPagination({
        page: data.number,
        size: data.size,
        totalElements: data.totalElements,
        totalPages: data.totalPages
      });
      setError(null);
    } catch (err: any) {
      console.error('Erro ao buscar álbuns:', err);
      setError('Erro ao carregar álbuns. Verifique se o backend está rodando.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = (id: number) => {
    confirmDialog({
      message: 'Tem certeza que deseja excluir este álbum?',
      header: 'Confirmação de Exclusão',
      icon: 'pi pi-exclamation-triangle',
      acceptClassName: 'p-button-danger',
      acceptLabel: 'Sim, excluir',
      rejectLabel: 'Cancelar',
      accept: async () => {
        try {
          await api.delete(`/api/albuns/${id}`);
          toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Álbum excluído com sucesso!' });
          fetchAlbuns(pagination.page, pagination.size);
        } catch (error: any) {
          console.error('Erro ao excluir álbum:', error);
        }
      }
    });
  };

  const handleOpenForm = (id: number | null = null) => {
    setSelectedAlbumId(id);
    setDialogVisible(true);
  };

  const handleFormSuccess = () => {
    setDialogVisible(false);
    fetchAlbuns(pagination.page, pagination.size);
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/');
  };

  const handleBackToDashboard = () => {
    navigate('/dashboard');
  };

  const actionBodyTemplate = (rowData: Album) => {
    return (
      <div className="flex gap-2">
        <Button 
          icon="pi pi-pencil" 
          className="p-button-rounded p-button-success p-button-text" 
          onClick={() => handleOpenForm(rowData.id)}
        />
        <Button 
          icon="pi pi-trash" 
          className="p-button-rounded p-button-danger p-button-text" 
          onClick={() => handleDelete(rowData.id)}
        />
      </div>
    );
  };

  const capaBodyTemplate = (rowData: Album) => {
    if (!rowData.capaUrl) return <i className="pi pi-image text-300" style={{ fontSize: '2rem' }}></i>;
    return (
        <Image src={rowData.capaUrl} alt={rowData.titulo} width="50" preview />
    );
  };

  return (
    <div className="p-4">
      <Toast ref={toast} />
      <ConfirmDialog />
      <Card title="Lista de Álbuns" className="mb-4">
        <div className="flex justify-content-between align-items-center mb-4">
          <Button 
            label="Voltar" 
            icon="pi pi-arrow-left" 
            className="p-button-outlined" 
            onClick={handleBackToDashboard}
          />
          <div className="text-center">
            <span className="text-600">
              Mostrando {pagination.totalElements > 0 ? (pagination.page * pagination.size) + 1 : 0} a {Math.min((pagination.page + 1) * pagination.size, pagination.totalElements)} de {pagination.totalElements} álbuns
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
          value={albuns} 
          loading={loading}
          emptyMessage="Nenhum álbum encontrado"
          responsiveLayout="scroll"
          className="p-datatable-sm"
          lazy
          paginator
          first={pagination.page * pagination.size}
          rows={pagination.size}
          totalRecords={pagination.totalElements}
          onPage={(event) => fetchAlbuns(event.page, event.rows)}
        >
          <Column header="Capa" body={capaBodyTemplate} style={{ width: '80px' }} />
          <Column field="id" header="ID" sortable style={{ width: '80px' }} />
          <Column field="titulo" header="Título" sortable />
          <Column field="artistaNome" header="Artista" sortable />
          <Column field="anoLancamento" header="Ano" sortable style={{ width: '100px' }} />
          <Column field="genero" header="Gênero" sortable />
          <Column field="regionalNome" header="Regional" sortable />
          <Column 
            header="Ações" 
            body={actionBodyTemplate} 
            style={{ width: '120px', textAlign: 'center' }}
          />
        </DataTable>

        <div className="mt-4">
          <Button 
            label="Adicionar Álbum" 
            icon="pi pi-plus" 
            className="p-button-success"
            onClick={() => handleOpenForm()}
          />
        </div>
      </Card>

      <Dialog 
        header={selectedAlbumId ? 'Editar Álbum' : 'Novo Álbum'}
        visible={dialogVisible} 
        style={{ width: '50vw' }} 
        onHide={() => setDialogVisible(false)}
        maximizable
      >
        <AlbumForm 
          albumId={selectedAlbumId} 
          onSuccess={handleFormSuccess} 
          onCancel={() => setDialogVisible(false)}
        />
      </Dialog>
    </div>
  );
}

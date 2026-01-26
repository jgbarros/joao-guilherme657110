import { useState, useEffect, useRef } from 'react';
import { InputText } from 'primereact/inputtext';
import { InputNumber } from 'primereact/inputnumber';
import { Dropdown } from 'primereact/dropdown';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import { FileUpload } from 'primereact/fileupload';
import api from '../api/axios';

interface AlbumFormProps {
  albumId: number | null;
  onSuccess: () => void;
  onCancel: () => void;
}

interface AlbumFormData {
  titulo: string;
  artistaId: number | null;
  anoLancamento: number | null;
  genero: string;
  capaUrl: string;
  faixas: string;
  regionalId: number | null;
}

interface Artista {
  id: number;
  nome: string;
}

interface Regional {
  id: number;
  nome: string;
}

export default function AlbumForm({ albumId, onSuccess, onCancel }: AlbumFormProps) {
  const [formData, setFormData] = useState<AlbumFormData>({
    titulo: '',
    artistaId: null,
    anoLancamento: null,
    genero: '',
    capaUrl: '',
    faixas: '',
    regionalId: null,
  });
  const [artistas, setArtistas] = useState<Artista[]>([]);
  const [regionais, setRegionais] = useState<Regional[]>([]);
  const [loading, setLoading] = useState(false);
  const toast = useRef<Toast>(null);

  const isEditMode = albumId !== null;

  // UseEffect para carregar listas de seleção uma única vez na montagem do componente
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [artistasRes, regionaisRes] = await Promise.all([
          api.get('/api/artistas?size=1000'),
          api.get('/api/regionais/ativas') // Mudado para carregar apenas regionais ativas (retorna lista diretamente)
        ]);
        
        const artistasList = artistasRes.data?.content || (Array.isArray(artistasRes.data) ? artistasRes.data : []);
        const regionaisList = Array.isArray(regionaisRes.data) ? regionaisRes.data : (regionaisRes.data?.content || []);
        
        setArtistas(artistasList);
        setRegionais(regionaisList);
      } catch (error) {
        console.error('Erro ao buscar artistas ou regionais:', error);
        toast.current?.show({ severity: 'error', summary: 'Erro', detail: 'Erro ao carregar listas de seleção.' });
      }
    };

    fetchData();
  }, []);

  // UseEffect para carregar dados do álbum ou resetar o formulário quando albumId mudar
  useEffect(() => {
    if (albumId) {
      const fetchAlbum = async () => {
        setLoading(true);
        try {
          const response = await api.get(`/api/albuns/${albumId}`);
          const { titulo, artistaId, anoLancamento, genero, capaUrl, faixas, regionalId } = response.data;
          setFormData({
            titulo,
            artistaId,
            anoLancamento,
            genero: genero || '',
            capaUrl: capaUrl || '',
            faixas: faixas || '',
            regionalId,
          });
        } catch (error) {
          console.error('Erro ao buscar álbum:', error);
          toast.current?.show({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar os dados do álbum.' });
        } finally {
          setLoading(false);
        }
      };
      fetchAlbum();
    } else {
      setFormData({
        titulo: '',
        artistaId: null,
        anoLancamento: null,
        genero: '',
        capaUrl: '',
        faixas: '',
        regionalId: null,
      });
    }
  }, [albumId]);

  const handleChange = (e: any) => {
    const { name, value } = e.target ? e.target : e;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const onUpload = (e: any) => {
    const url = e.xhr.response;
    setFormData(prev => ({ ...prev, capaUrl: url }));
    toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Capa enviada com sucesso!' });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    if (!formData.artistaId) {
        toast.current?.show({ severity: 'warn', summary: 'Aviso', detail: 'Selecione um artista.' });
        setLoading(false);
        return;
    }

    try {
      if (isEditMode && albumId) {
        await api.put(`/api/albuns/${albumId}`, formData);
        toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Álbum atualizado com sucesso!' });
      } else {
        await api.post('/api/albuns', formData);
        toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Álbum criado com sucesso!' });
      }
      setTimeout(() => {
        onSuccess();
      }, 1000);
    } catch (error: any) {
      console.error('Erro ao salvar álbum:', error);
      const errorMessage = error.response?.data?.message || error.response?.data || 'Ocorreu um erro ao salvar o álbum.';
      toast.current?.show({ 
        severity: 'error', 
        summary: 'Erro', 
        detail: typeof errorMessage === 'string' ? errorMessage : 'Erro na validação dos dados.' 
      });
      setLoading(false);
    }
  };

  return (
    <div>
      <Toast ref={toast} />
      <form onSubmit={handleSubmit}>
        <div className="p-fluid">
          <div className="field mb-4">
            <label htmlFor="titulo" className="font-bold block mb-2">Título</label>
            <InputText id="titulo" name="titulo" value={formData.titulo} onChange={handleChange} required />
          </div>

          <div className="flex flex-wrap gap-3">
            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="artistaId" className="font-bold block mb-2">Artista</label>
              <Dropdown 
                id="artistaId" 
                name="artistaId" 
                value={formData.artistaId} 
                options={artistas} 
                onChange={handleChange} 
                optionLabel="nome" 
                optionValue="id" 
                placeholder="Selecione um Artista" 
                filter 
                required 
              />
            </div>

            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="anoLancamento" className="font-bold block mb-2">Ano de Lançamento</label>
              <InputNumber 
                id="anoLancamento" 
                name="anoLancamento" 
                value={formData.anoLancamento} 
                onValueChange={(e) => handleChange({ target: { name: 'anoLancamento', value: e.value } })} 
                useGrouping={false} 
                min={1901}
              />
            </div>
          </div>

          <div className="flex flex-wrap gap-3">
            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="genero" className="font-bold block mb-2">Gênero</label>
              <InputText id="genero" name="genero" value={formData.genero} onChange={handleChange} />
            </div>

            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="regionalId" className="font-bold block mb-2">Regional</label>
              <Dropdown 
                id="regionalId" 
                name="regionalId" 
                value={formData.regionalId} 
                options={regionais} 
                onChange={handleChange} 
                optionLabel="nome" 
                optionValue="id" 
                placeholder="Selecione uma Regional" 
                showClear
              />
            </div>
          </div>

          <div className="field mb-4">
            <label htmlFor="faixas" className="font-bold block mb-2">Faixas (separadas por vírgula)</label>
            <InputText id="faixas" name="faixas" value={formData.faixas} onChange={handleChange} placeholder="Ex: Faixa 1, Faixa 2, Faixa 3" />
          </div>

          {isEditMode && (
            <div className="field mb-4">
              <label className="font-bold block mb-2">Capa do Álbum</label>
              <FileUpload 
                mode="basic" 
                name="file" 
                url={`http://localhost:8080/api/albuns/${albumId}/upload`} 
                accept="image/*" 
                maxFileSize={1000000} 
                onUpload={onUpload} 
                auto 
                chooseLabel="Fazer Upload da Capa" 
                onBeforeSend={(e) => {
                  const token = localStorage.getItem('token');
                  if (token) {
                    e.xhr.setRequestHeader('Authorization', `Bearer ${token}`);
                  }
                }}
              />
              {formData.capaUrl && (
                <div className="mt-2">
                   <img src={formData.capaUrl} alt="Capa" style={{ maxWidth: '200px', borderRadius: '8px' }} />
                </div>
              )}
            </div>
          )}
        </div>

        <div className="flex justify-content-end gap-2 mt-4">
          <Button type="button" label="Cancelar" icon="pi pi-times" className="p-button-outlined" onClick={onCancel} disabled={loading} style={{ width: 'auto' }} />
          <Button type="submit" label={isEditMode ? 'Salvar Alterações' : 'Salvar'} icon="pi pi-check" disabled={loading} loading={loading} style={{ width: 'auto' }} />
        </div>
      </form>
    </div>
  );
}

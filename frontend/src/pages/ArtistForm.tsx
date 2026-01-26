import { useState, useEffect, useRef } from 'react';
import { InputText } from 'primereact/inputtext';
import { InputTextarea } from 'primereact/inputtextarea';
import { Calendar } from 'primereact/calendar';
import { Button } from 'primereact/button';
import { Toast } from 'primereact/toast';
import api from '../api/axios';

interface ArtistaFormProps {
  artistId: number | null;
  onSuccess: () => void;
  onCancel: () => void;
}

interface ArtistaFormData {
  nome: string;
  biografia: string;
  nacionalidade: string;
  dataNascimento: Date | null;
  dataMorte: Date | null;
}

export default function ArtistForm({ artistId, onSuccess, onCancel }: ArtistaFormProps) {
  const [formData, setFormData] = useState<ArtistaFormData>({
    nome: '',
    biografia: '',
    nacionalidade: '',
    dataNascimento: null,
    dataMorte: null,
  });
  const [loading, setLoading] = useState(false);
  const toast = useRef<Toast>(null);

  const isEditMode = artistId !== null;

  useEffect(() => {
    if (artistId) {
      const fetchArtist = async () => {
        setLoading(true);
        try {
          const response = await api.get(`/api/artistas/${artistId}`);
          const { nome, biografia, nacionalidade, dataNascimento, dataMorte } = response.data;
          setFormData({
            nome,
            biografia: biografia || '',
            nacionalidade: nacionalidade || '',
            dataNascimento: dataNascimento ? new Date(dataNascimento) : null,
            dataMorte: dataMorte ? new Date(dataMorte) : null,
          });
        } catch (error) {
          console.error('Erro ao buscar artista:', error);
          toast.current?.show({ severity: 'error', summary: 'Erro', detail: 'Não foi possível carregar os dados do artista.' });
        } finally {
          setLoading(false);
        }
      };
      fetchArtist();
    } else {
      // Reset form when switching to create mode
      setFormData({
        nome: '',
        biografia: '',
        nacionalidade: '',
        dataNascimento: null,
        dataMorte: null,
      });
    }
  }, [artistId]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleDateChange = (e: any, name: string) => {
    setFormData(prev => ({ ...prev, [name]: e.value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    if (formData.dataNascimento && formData.dataMorte) {
      if (formData.dataNascimento > formData.dataMorte) {
        toast.current?.show({ 
          severity: 'warn', 
          summary: 'Aviso', 
          detail: 'A data de nascimento não pode ser posterior à data de morte.' 
        });
        setLoading(false);
        return;
      }
    }

    const payload = {
      ...formData,
      dataNascimento: formData.dataNascimento ? formData.dataNascimento.toISOString().split('T')[0] : null,
      dataMorte: formData.dataMorte ? formData.dataMorte.toISOString().split('T')[0] : null,
    };

    try {
      if (isEditMode && artistId) {
        await api.put(`/api/artistas/${artistId}`, payload);
        toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Artista atualizado com sucesso!' });
      } else {
        await api.post('/api/artistas', payload);
        toast.current?.show({ severity: 'success', summary: 'Sucesso', detail: 'Artista criado com sucesso!' });
      }
      // Aguarda um pouco para mostrar o toast antes de fechar
      setTimeout(() => {
        onSuccess();
      }, 1000);
    } catch (error: any) {
      console.error('Erro ao salvar artista:', error);
      const errorMessage = error.response?.data?.message || error.response?.data || 'Ocorreu um erro ao salvar o artista.';
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
            <label htmlFor="nome" className="font-bold block mb-2">Nome</label>
            <InputText id="nome" name="nome" value={formData.nome} onChange={handleChange} required />
          </div>

          <div className="field mb-4">
            <label htmlFor="biografia" className="font-bold block mb-2">Biografia</label>
            <InputTextarea id="biografia" name="biografia" value={formData.biografia} onChange={handleChange} rows={5} />
          </div>

          <div className="flex flex-wrap gap-3">
            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="nacionalidade" className="font-bold block mb-2">Nacionalidade</label>
              <InputText id="nacionalidade" name="nacionalidade" value={formData.nacionalidade} onChange={handleChange} className="w-full" />
            </div>

            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="dataNascimento" className="font-bold block mb-2">Data de Nascimento</label>
              <Calendar id="dataNascimento" name="dataNascimento" value={formData.dataNascimento} onChange={(e) => handleDateChange(e, 'dataNascimento')} dateFormat="dd/mm/yy" showIcon className="w-full" />
            </div>

            <div className="flex-1 mb-4" style={{ minWidth: '200px' }}>
              <label htmlFor="dataMorte" className="font-bold block mb-2">Data de Falecimento</label>
              <Calendar id="dataMorte" name="dataMorte" value={formData.dataMorte} onChange={(e) => handleDateChange(e, 'dataMorte')} dateFormat="dd/mm/yy" showIcon className="w-full" />
            </div>
          </div>
        </div>

        <div className="flex justify-content-end gap-2 mt-4">
          <Button type="button" label="Cancelar" icon="pi pi-times" className="p-button-outlined" onClick={onCancel} disabled={loading} style={{ width: 'auto' }} />
          <Button type="submit" label={isEditMode ? 'Salvar Alterações' : 'Salvar'} icon="pi pi-check" disabled={loading} loading={loading} style={{ width: 'auto' }} />
        </div>
      </form>
    </div>
  );
}

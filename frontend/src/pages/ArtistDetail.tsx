import { useState, useEffect } from 'react';
import { Card } from 'primereact/card';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Image } from 'primereact/image';
import api from '../api/axios';

interface AlbumSummary {
    id: number;
    titulo: string;
    anoLancamento: number;
    capaUrl: string;
}

interface ArtistaDetalhe {
    id: number;
    nome: string;
    nacionalidade: string;
    albuns: AlbumSummary[];
}

interface ArtistDetailProps {
    artistId: number;
}

export default function ArtistDetail({ artistId }: ArtistDetailProps) {
    const [artista, setArtista] = useState<ArtistaDetalhe | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchArtistDetail = async () => {
            setLoading(true);
            try {
                const response = await api.get(`/api/artistas/${artistId}/detalhe`);
                setArtista(response.data);
                setError(null);
            } catch (err: any) {
                console.error('Erro ao buscar detalhes do artista:', err);
                setError('Erro ao carregar os detalhes do artista.');
            } finally {
                setLoading(false);
            }
        };

        if (artistId) {
            fetchArtistDetail();
        }
    }, [artistId]);

    if (loading) {
        return (
            <div className="flex justify-content-center align-items-center p-5">
                <i className="pi pi-spin pi-spinner" style={{ fontSize: '2rem' }}></i>
            </div>
        );
    }

    if (error || !artista) {
        return (
            <div className="p-4">
                <div className="p-message p-message-error">
                    <p>{error || 'Artista não encontrado.'}</p>
                </div>
            </div>
        );
    }

    const capaBodyTemplate = (rowData: AlbumSummary) => {
        return (
            <Image 
                src={rowData.capaUrl || 'https://via.placeholder.com/50'} 
                alt={rowData.titulo} 
                width="50" 
                preview 
            />
        );
    };

    return (
        <div className="flex flex-column gap-4">
            <Card className="shadow-1">
                <div className="flex flex-wrap gap-4">
                    <div className="flex align-items-center">
                        <label className="font-bold mr-2">ID:</label>
                        <span className="text-700">{artista.id}</span>
                    </div>
                    <div className="flex align-items-center">
                        <label className="font-bold mr-2">Nome:</label>
                        <span className="text-700">{artista.nome}</span>
                    </div>
                    <div className="flex align-items-center">
                        <label className="font-bold mr-2">Nacionalidade:</label>
                        <span className="text-700">{artista.nacionalidade || 'Não informada'}</span>
                    </div>
                </div>
            </Card>

            <Card title="Álbuns" className="shadow-1">
                <DataTable 
                    value={artista.albuns} 
                    responsiveLayout="scroll" 
                    emptyMessage="Nenhum álbum encontrado para este artista."
                    className="p-datatable-sm"
                >
                    <Column field="id" header="ID" style={{ width: '80px' }} />
                    <Column header="Capa" body={capaBodyTemplate} style={{ width: '100px' }} />
                    <Column field="titulo" header="Título" />
                    <Column field="anoLancamento" header="Ano de Lançamento" />
                </DataTable>
            </Card>
        </div>
    );
}

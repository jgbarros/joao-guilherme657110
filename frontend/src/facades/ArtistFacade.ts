import { BehaviorSubject, tap } from 'rxjs';
import api from '../api/axios';

export interface Artista {
  id: number;
  nome: string;
  biografia?: string;
  dataNascimento?: string;
  dataMorte?: string;
  nacionalidade?: string;
  albumCount?: number;
}

export interface PaginatedResponse<T> {
  content: T[];
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

class ArtistFacade {
  private artistsSubject = new BehaviorSubject<Artista[]>([]);
  public artists$ = this.artistsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  private paginationSubject = new BehaviorSubject({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0
  });
  public pagination$ = this.paginationSubject.asObservable();

  async fetchArtists(page: number = 0, size: number = 10) {
    this.loadingSubject.next(true);
    try {
      const response = await api.get<PaginatedResponse<Artista>>(`/api/artistas?page=${page}&size=${size}`);
      const data = response.data;
      
      this.artistsSubject.next(data.content);
      this.paginationSubject.next({
        page: data.number,
        size: data.size,
        totalElements: data.totalElements,
        totalPages: data.totalPages
      });
    } catch (error) {
      console.error('Erro ao buscar artistas:', error);
      throw error;
    } finally {
      this.loadingSubject.next(false);
    }
  }

  async deleteArtist(id: number) {
    await api.delete(`/api/artistas/${id}`);
    const { page, size } = this.paginationSubject.getValue();
    await this.fetchArtists(page, size);
    
    // Atualizar dashboard se necessário
    const { default: DashboardFacade } = await import('./DashboardFacade');
    DashboardFacade.fetchStats().catch(() => {});
  }

  async getArtistById(id: number) {
    const response = await api.get<Artista>(`/api/artistas/${id}`);
    return response.data;
  }

  async getArtistDetail(id: number) {
    const response = await api.get(`/api/artistas/${id}/detalhe`);
    return response.data;
  }

  async saveArtist(artist: Partial<Artista>) {
    if (artist.id) {
      await api.put(`/api/artistas/${artist.id}`, artist);
    } else {
      await api.post('/api/artistas', artist);
    }
    const { page, size } = this.paginationSubject.getValue();
    await this.fetchArtists(page, size);

    // Atualizar dashboard se for novo artista
    if (!artist.id) {
      const { default: DashboardFacade } = await import('./DashboardFacade');
      DashboardFacade.fetchStats().catch(() => {});
    }
  }
}

export default new ArtistFacade();

import { BehaviorSubject } from 'rxjs';
import api from '../api/axios';

export interface Album {
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

class AlbumFacade {
  private albumsSubject = new BehaviorSubject<Album[]>([]);
  public albums$ = this.albumsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  private paginationSubject = new BehaviorSubject({
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0
  });
  public pagination$ = this.paginationSubject.asObservable();

  async fetchAlbums(page: number = 0, size: number = 10) {
    this.loadingSubject.next(true);
    try {
      const response = await api.get<PaginatedResponse<Album>>(`/api/albuns?page=${page}&size=${size}`);
      const data = response.data;
      
      this.albumsSubject.next(data.content);
      this.paginationSubject.next({
        page: data.number,
        size: data.size,
        totalElements: data.totalElements,
        totalPages: data.totalPages
      });
    } catch (error) {
      console.error('Erro ao buscar álbuns:', error);
      throw error;
    } finally {
      this.loadingSubject.next(false);
    }
  }

  async deleteAlbum(id: number) {
    await api.delete(`/api/albuns/${id}`);
    const { page, size } = this.paginationSubject.getValue();
    await this.fetchAlbums(page, size);

    // Atualizar dashboard
    const { default: DashboardFacade } = await import('./DashboardFacade');
    DashboardFacade.fetchStats().catch(() => {});
  }

  async getAlbumById(id: number) {
    const response = await api.get<Album>(`/api/albuns/${id}`);
    return response.data;
  }

  async saveAlbum(album: Partial<Album>) {
    if (album.id) {
      await api.put(`/api/albuns/${album.id}`, album);
    } else {
      await api.post('/api/albuns', album);
    }
    const { page, size } = this.paginationSubject.getValue();
    await this.fetchAlbums(page, size);

    // Atualizar dashboard se for novo álbum
    if (!album.id) {
      const { default: DashboardFacade } = await import('./DashboardFacade');
      DashboardFacade.fetchStats().catch(() => {});
    }
  }
}

export default new AlbumFacade();

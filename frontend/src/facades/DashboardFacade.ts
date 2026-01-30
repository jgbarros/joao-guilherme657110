import { BehaviorSubject } from 'rxjs';
import api from '../api/axios';
import RegionalApiService from '../services/RegionalApiService';

export interface DashboardStats {
  totalArtistas: number;
  totalAlbuns: number;
  totalRegionais: number;
  totalRegionaisApi: number;
}

class DashboardFacade {
  private statsSubject = new BehaviorSubject<DashboardStats>({
    totalArtistas: 0,
    totalAlbuns: 0,
    totalRegionais: 0,
    totalRegionaisApi: 0
  });
  public stats$ = this.statsSubject.asObservable();

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  async fetchStats() {
    this.loadingSubject.next(true);
    try {
      const [artistasRes, albunsRes, regionaisRes, totalApi] = await Promise.all([
        api.get('/api/artistas/count'),
        api.get('/api/albuns/count'),
        api.get('/api/regionais/count'),
        RegionalApiService.count()
      ]);

      const stats = {
        totalArtistas: artistasRes.data,
        totalAlbuns: albunsRes.data,
        totalRegionais: regionaisRes.data,
        totalRegionaisApi: totalApi
      };
      
      this.statsSubject.next(stats);
      return stats;
    } catch (error) {
      console.error('Erro ao buscar estatísticas do dashboard:', error);
      throw error;
    } finally {
      this.loadingSubject.next(false);
    }
  }
}

export default new DashboardFacade();

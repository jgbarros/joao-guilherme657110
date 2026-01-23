import axios from 'axios';

const regionalApi = axios.create({
  baseURL: 'https://integrador-argus-api.geia.vip/v1/regionais',
  timeout: 10000,
});

export interface Regional {
  id: number;
  nome: string;
  // Adicione outros campos se souber a estrutura, 
  // caso contrário manteremos flexível
  [key: string]: any;
}

const RegionalApiService = {
  /**
   * Lista todas as regionais
   */
  async getAll(): Promise<Regional[]> {
    const response = await regionalApi.get('');
    return response.data;
  },

  /**
   * Consulta uma regional por ID
   * @param id ID da regional
   */
  async getById(id: number | string): Promise<Regional> {
    const response = await regionalApi.get(`/${id}`);
    return response.data;
  },

  /**
   * Conta todos os elementos das regionais
   */
  async count(): Promise<number> {
    const regionais = await this.getAll();
    return regionais.length;
  }
};

export default RegionalApiService;

import { IRegion } from 'app/shared/model/region.model';

export interface IAccExec {
  id?: number;
  nombre?: string;
  apellido?: string;
  telefono?: string;
  celular?: string;
  mail?: string;
  repcom1?: string;
  repcom2?: string;
  regions?: IRegion[];
}

export const defaultValue: Readonly<IAccExec> = {};

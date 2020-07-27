import { IAccExec } from 'app/shared/model/acc-exec.model';

export interface IRegion {
  id?: number;
  descripcion?: string;
  valor?: string;
  accExecs?: IAccExec[];
}

export const defaultValue: Readonly<IRegion> = {};

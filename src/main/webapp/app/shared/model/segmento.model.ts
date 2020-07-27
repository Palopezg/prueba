import { IAccExec } from 'app/shared/model/acc-exec.model';

export interface ISegmento {
  id?: number;
  descripcion?: string;
  valor?: string;
  accExec?: IAccExec;
}

export const defaultValue: Readonly<ISegmento> = {};

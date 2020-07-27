export interface IAccountExecutive {
  id?: number;
  nombre?: string;
  apellido?: string;
  telefono?: string;
  celular?: string;
  mail?: string;
  repcom1?: string;
  repcom2?: string;
}

export const defaultValue: Readonly<IAccountExecutive> = {};

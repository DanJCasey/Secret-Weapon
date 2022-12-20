import { IUser } from 'app/shared/model/user.model';
import { IExpertUser } from 'app/shared/model/expert-user.model';

export interface IUserAccount {
  id?: number;
  name?: string | null;
  password?: string | null;
  user?: IUser | null;
  expertUsers?: IExpertUser[] | null;
}

export const defaultValue: Readonly<IUserAccount> = {};

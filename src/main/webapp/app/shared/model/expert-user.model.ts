import { IUserAccount } from 'app/shared/model/user-account.model';
import { Expertise } from 'app/shared/model/enumerations/expertise.model';

export interface IExpertUser {
  id?: number;
  expertise?: Expertise | null;
  login?: IUserAccount | null;
}

export const defaultValue: Readonly<IExpertUser> = {};

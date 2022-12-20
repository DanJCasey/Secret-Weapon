import dayjs from 'dayjs';
import { IUserAccount } from 'app/shared/model/user-account.model';
import { Expertise } from 'app/shared/model/enumerations/expertise.model';

export interface IPost {
  id?: number;
  title?: string;
  postBody?: string;
  date?: string | null;
  expertise?: Expertise | null;
  imageContentType?: string | null;
  image?: string | null;
  login?: IUserAccount | null;
}

export const defaultValue: Readonly<IPost> = {};

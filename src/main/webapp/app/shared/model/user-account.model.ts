import { IUser } from 'app/shared/model/user.model';
import { IExpertUser } from 'app/shared/model/expert-user.model';
import { IPost } from 'app/shared/model/post.model';

export interface IUserAccount {
  id?: number;
  name?: string | null;
  password?: string | null;
  user?: IUser | null;
  expertUsers?: IExpertUser[] | null;
  posts?: IPost[] | null;
}

export const defaultValue: Readonly<IUserAccount> = {};

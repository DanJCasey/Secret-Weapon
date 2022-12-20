import dayjs from 'dayjs';
import { Expertise } from 'app/shared/model/enumerations/expertise.model';

export interface IPost {
  id?: number;
  title?: string;
  postBody?: string;
  date?: string | null;
  expertise?: Expertise | null;
  imageContentType?: string | null;
  image?: string | null;
}

export const defaultValue: Readonly<IPost> = {};

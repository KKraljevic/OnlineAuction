import { User } from './user';

export class Rating {
    id: number;
    value: number;
    bidder: number;
    seller?: User;
}

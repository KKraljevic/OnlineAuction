import { Item } from './item';
import { Bid } from './bid';

export class User {
    id: number;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    items: Item[];
    bids: Bid[];
}

import { Item } from './item';
import { Bid } from './bid';
import { Image } from './image';

export class User {
    id: number;
    email: string;
    password: string;
    firstName: string;
    lastName: string;
    phone?: string;
    paymentId?: string;
    paymentCode?: string;
    birthDate?: Date;
    gender?: string;
    items: Item[]=[];
    userBids: Bid[]=[];
    photo: string;
    wishlist?: Item[];
}

import { User } from './user';
import { Category } from './category';
import { Bid } from './bid';
import { Image } from './image';
import { Shipping } from './shipping';

export class Item {
    id: number;
    name: String;
    description: String;
    currentPrice: number;
    startPrice: number;
    endDate: Date;
    images: Image[];
    category: Category;
    seller: User;
    itemBids: Bid[];
    shipping?: Shipping;
}

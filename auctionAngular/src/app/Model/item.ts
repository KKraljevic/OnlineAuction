import { User } from './user';
import { Category } from './category';
import { Bid } from './bid';
import { Image } from './image';

export class Item {
    id: number;
    name: String;
    description: String;
    currentPrice: number;
    startPrice: number;
    endDate: Date;
    quantity: number;
    images: Image[];
    category: Category;
    seller: User;
    itemBids: Bid[];
}

import { User } from './user';
import { Category } from './category';
import { Bid } from './bid';

export class Item {
    id: number;
    name: String;
    description: String;
    currentPrice: number;
    startPrice: number;
    endDate: Date;
    quantity: number;
    category: Category;
    seller: User;
    itemBids: Bid[];
}

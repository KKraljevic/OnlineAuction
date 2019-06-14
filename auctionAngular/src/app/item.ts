import { User } from './user';
import { Category } from './category';
import { Bid } from './bid';

export class Item {
    item_id: number;
    name: String;
    description: String;
    currentPrice: number;
    startPrice: number;
    endDate: Date;
    quantity: number;
    user: User;
    category: Category;
    bids: Bid[];
}

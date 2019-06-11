import { User } from './user';
import { Item } from './item';

export class Bid {
    bid_id: number;
    bidPrice: number;
    bidTime: Date;
    isActive: boolean;
    user: User;
    item: Item;

}

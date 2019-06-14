import { User } from './user';
import { Item } from './item';

export class Bid {
    bid_Id: number;
    bidPrice: number;
    bidTime: Date;
    isActive: boolean;
    user: User;
    item: Item;

}

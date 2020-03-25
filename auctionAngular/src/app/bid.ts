import { User } from './user';
import { Item } from './item';

export class Bid {
    id: number;
    bidPrice: number;
    bidTime: Date;
    active: boolean;
    auctionItem: Item;
    bidder: User;
  
}

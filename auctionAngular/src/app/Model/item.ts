import { User } from './user';
import { Category } from './category';
import { Bid } from './bid';
import { Image } from './image';
import { Location } from './location';

export class Item {
    id: number;
    name: String;
    description: String;
    currentPrice: number;
    startPrice: number;
    endDate: Date;
    paid: boolean;
    images: Image[];
    category: Category;
    seller: User;
    itemBids: Bid[];
    location?: Location;
    shipmentLocation?: Location;

}

import { City } from './city';
import { Item } from './item';

export class Shipping {
    id: number;
    address: string;
    zipcode: string;
    freeShipping: boolean;
    city?: City;
    shippmentItem?: Item;
}

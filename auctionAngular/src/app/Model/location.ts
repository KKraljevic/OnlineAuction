import { City } from './city';
import { Item } from './item';

export class Location {
    id: number;
    address: string;
    zipcode: string;
    freeShipping: boolean;
    city?: City;
}

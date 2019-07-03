import { Item } from './item';

export class Category {
    id: number;
    parent_id: number;
    categoryName: String;
    children: Category[];
    
    
}

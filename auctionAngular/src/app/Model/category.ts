import { Item } from './item';

export class Category {
    id: number;
    parent: Category;
    categoryName: String;
    children: Category[];
    
    
}

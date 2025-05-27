import { ICategory } from "../category/category.model";

export interface IDriver {
    name: string,
    dob: Date,
    active: boolean,
    category?: ICategory,
    id?: number 
}

export class Driver {
    constructor(
        public name: string,
        public dob: Date,
        public active: boolean,
        public category?: ICategory,
        public id?: number
    ) {}
}
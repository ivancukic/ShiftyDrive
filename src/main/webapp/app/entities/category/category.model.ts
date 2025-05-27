
export interface ICategory {
    name: string,
    id?: number,
}

export class Category {
    constructor(
        public name: string,
        public id?: number,
    ) {}
}
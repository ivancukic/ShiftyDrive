export interface IUser {
    id: number,
    fullName: string,
    email: string,
    createdAt?: Date,
    updatedAt?: Date
}

export class User {
    constructor(
        public id: number,
        public fullName: string, 
        public email: string,
        public createdAt?: Date,
        public updatedAt?: Date
    ) {}
}
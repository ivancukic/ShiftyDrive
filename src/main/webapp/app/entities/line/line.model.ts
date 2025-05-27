
export interface ILine {
    name: string,
    start_time: string,
    end_time: string,
    total_time?: string,
    num_of_drivers?: number,
    id?: number,
}

export class Line {
    constructor(
        public name: string,
        public start_time: string,
        public end_time: string,
        public total_time?: string,
        public num_of_drivers?: number,
        public id?: number,
    ) {}
}
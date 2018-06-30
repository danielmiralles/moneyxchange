import { BaseEntity } from './../../shared';

export class Currency implements BaseEntity {
    constructor(
        public id?: number,
        public code?: string,
        public symbol?: string,
        public symbolLeft?: boolean,
        public description?: string,
    ) {
        this.symbolLeft = false;
    }
}

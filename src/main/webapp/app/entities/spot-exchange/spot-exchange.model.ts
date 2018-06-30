import { BaseEntity } from './../../shared';

export const enum Operation {
    'MULTIPLY',
    'DIVIDE'
}

export class SpotExchange implements BaseEntity {
    constructor(
        public id?: number,
        public fromInstant?: any,
        public operation?: Operation,
        public sourceCurrencyId?: number,
        public targetCurrencyId?: number,
    ) {
    }
}

export class ExchangeResponse {
    constructor(
        public exchangeRate?: number,
        public operation?: string,
        public timeout?: string,
    ) {
    }
}

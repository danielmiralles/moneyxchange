import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../app.constants';

import { ExchangeResponse } from './home.model';

export type EntityResponseType = HttpResponse<ExchangeResponse>;

@Injectable()
export class HomeService {

    constructor(private http: HttpClient) {}

    exchange(source: string, target: string): Observable<EntityResponseType> {
        return this.http.get<ExchangeResponse>(SERVER_API_URL + 'api/exchange-rate/' + `${source}/${target}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ExchangeResponse = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Currency.
     */
    private convertItemFromServer(currency: ExchangeResponse): ExchangeResponse {
        const copy: ExchangeResponse = Object.assign({}, currency);
        return copy;
    }
}

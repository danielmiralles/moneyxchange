import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { SpotExchange } from './spot-exchange.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<SpotExchange>;

@Injectable()
export class SpotExchangeService {

    private resourceUrl =  SERVER_API_URL + 'api/spot-exchanges';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/spot-exchanges';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(spotExchange: SpotExchange): Observable<EntityResponseType> {
        const copy = this.convert(spotExchange);
        return this.http.post<SpotExchange>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(spotExchange: SpotExchange): Observable<EntityResponseType> {
        const copy = this.convert(spotExchange);
        return this.http.put<SpotExchange>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<SpotExchange>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<SpotExchange[]>> {
        const options = createRequestOption(req);
        return this.http.get<SpotExchange[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SpotExchange[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<SpotExchange[]>> {
        const options = createRequestOption(req);
        return this.http.get<SpotExchange[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<SpotExchange[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: SpotExchange = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<SpotExchange[]>): HttpResponse<SpotExchange[]> {
        const jsonResponse: SpotExchange[] = res.body;
        const body: SpotExchange[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to SpotExchange.
     */
    private convertItemFromServer(spotExchange: SpotExchange): SpotExchange {
        const copy: SpotExchange = Object.assign({}, spotExchange);
        copy.fromInstant = this.dateUtils
            .convertDateTimeFromServer(spotExchange.fromInstant);
        return copy;
    }

    /**
     * Convert a SpotExchange to a JSON which can be sent to the server.
     */
    private convert(spotExchange: SpotExchange): SpotExchange {
        const copy: SpotExchange = Object.assign({}, spotExchange);

        copy.fromInstant = this.dateUtils.toDate(spotExchange.fromInstant);
        return copy;
    }
}

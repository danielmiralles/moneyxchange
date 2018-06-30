import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { SpotExchangeComponent } from './spot-exchange.component';
import { SpotExchangeDetailComponent } from './spot-exchange-detail.component';
import { SpotExchangePopupComponent } from './spot-exchange-dialog.component';
import { SpotExchangeDeletePopupComponent } from './spot-exchange-delete-dialog.component';

@Injectable()
export class SpotExchangeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const spotExchangeRoute: Routes = [
    {
        path: 'spot-exchange',
        component: SpotExchangeComponent,
        resolve: {
            'pagingParams': SpotExchangeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.spotExchange.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'spot-exchange/:id',
        component: SpotExchangeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.spotExchange.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const spotExchangePopupRoute: Routes = [
    {
        path: 'spot-exchange-new',
        component: SpotExchangePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.spotExchange.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'spot-exchange/:id/edit',
        component: SpotExchangePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.spotExchange.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'spot-exchange/:id/delete',
        component: SpotExchangeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.spotExchange.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

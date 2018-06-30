import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';
import { CurrencyPopupComponent } from './currency-dialog.component';
import { CurrencyDeletePopupComponent } from './currency-delete-dialog.component';

@Injectable()
export class CurrencyResolvePagingParams implements Resolve<any> {

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

export const currencyRoute: Routes = [
    {
        path: 'currency',
        component: CurrencyComponent,
        resolve: {
            'pagingParams': CurrencyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'currency/:id',
        component: CurrencyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const currencyPopupRoute: Routes = [
    {
        path: 'currency-new',
        component: CurrencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'currency/:id/edit',
        component: CurrencyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'currency/:id/delete',
        component: CurrencyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'moneyxchangeApp.currency.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

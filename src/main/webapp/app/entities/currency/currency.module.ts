import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MoneyxchangeSharedModule } from '../../shared';
import {
    CurrencyService,
    CurrencyPopupService,
    CurrencyComponent,
    CurrencyDetailComponent,
    CurrencyDialogComponent,
    CurrencyPopupComponent,
    CurrencyDeletePopupComponent,
    CurrencyDeleteDialogComponent,
    currencyRoute,
    currencyPopupRoute,
    CurrencyResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...currencyRoute,
    ...currencyPopupRoute,
];

@NgModule({
    imports: [
        MoneyxchangeSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CurrencyComponent,
        CurrencyDetailComponent,
        CurrencyDialogComponent,
        CurrencyDeleteDialogComponent,
        CurrencyPopupComponent,
        CurrencyDeletePopupComponent,
    ],
    entryComponents: [
        CurrencyComponent,
        CurrencyDialogComponent,
        CurrencyPopupComponent,
        CurrencyDeleteDialogComponent,
        CurrencyDeletePopupComponent,
    ],
    providers: [
        CurrencyService,
        CurrencyPopupService,
        CurrencyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MoneyxchangeCurrencyModule {}

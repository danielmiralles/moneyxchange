import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MoneyxchangeSharedModule } from '../../shared';
import {
    SpotExchangeService,
    SpotExchangePopupService,
    SpotExchangeComponent,
    SpotExchangeDetailComponent,
    SpotExchangeDialogComponent,
    SpotExchangePopupComponent,
    SpotExchangeDeletePopupComponent,
    SpotExchangeDeleteDialogComponent,
    spotExchangeRoute,
    spotExchangePopupRoute,
    SpotExchangeResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...spotExchangeRoute,
    ...spotExchangePopupRoute,
];

@NgModule({
    imports: [
        MoneyxchangeSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SpotExchangeComponent,
        SpotExchangeDetailComponent,
        SpotExchangeDialogComponent,
        SpotExchangeDeleteDialogComponent,
        SpotExchangePopupComponent,
        SpotExchangeDeletePopupComponent,
    ],
    entryComponents: [
        SpotExchangeComponent,
        SpotExchangeDialogComponent,
        SpotExchangePopupComponent,
        SpotExchangeDeleteDialogComponent,
        SpotExchangeDeletePopupComponent,
    ],
    providers: [
        SpotExchangeService,
        SpotExchangePopupService,
        SpotExchangeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MoneyxchangeSpotExchangeModule {}

import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { MoneyxchangeCurrencyModule } from './currency/currency.module';
import { MoneyxchangeSpotExchangeModule } from './spot-exchange/spot-exchange.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        MoneyxchangeCurrencyModule,
        MoneyxchangeSpotExchangeModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MoneyxchangeEntityModule {}

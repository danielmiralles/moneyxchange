import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MoneyxchangeSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';
import { HomeService } from './home.service';

@NgModule({
    imports: [
        MoneyxchangeSharedModule,
        RouterModule.forChild([ HOME_ROUTE ])
    ],
    declarations: [
        HomeComponent,
    ],
    entryComponents: [
    ],
    providers: [
        HomeService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MoneyxchangeHomeModule {}

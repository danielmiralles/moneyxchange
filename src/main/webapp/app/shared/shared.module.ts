import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { DatePipe } from '@angular/common';
import { TimerComponent} from './timer/timer.component';

import {
    MoneyxchangeSharedLibsModule,
    MoneyxchangeSharedCommonModule,
    CSRFService,
    AuthServerProvider,
    AccountService,
    UserService,
    StateStorageService,
    LoginService,
    LoginModalService,
    JhiLoginModalComponent,
    Principal,
    HasAnyAuthorityDirective,
} from './';
import { TimerManagerService } from './timer/timer-manager.service';
import { CustomCurrencyPipe } from './pipes/custom-currency.pipe';
import { CustomCurrencyDirective } from './format/custom-currency.directive';

@NgModule({
    imports: [
        MoneyxchangeSharedLibsModule,
        MoneyxchangeSharedCommonModule
    ],
    declarations: [
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        CustomCurrencyDirective,
        CustomCurrencyDirective,
        TimerComponent,
        CustomCurrencyPipe
    ],
    providers: [
        LoginService,
        LoginModalService,
        AccountService,
        StateStorageService,
        Principal,
        CSRFService,
        AuthServerProvider,
        UserService,
        DatePipe,
        CustomCurrencyPipe,
        TimerManagerService,

    ],
    entryComponents: [JhiLoginModalComponent],
    exports: [
        MoneyxchangeSharedCommonModule,
        JhiLoginModalComponent,
        HasAnyAuthorityDirective,
        CustomCurrencyDirective,
        DatePipe,
        CustomCurrencyPipe,
        TimerComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]

})
export class MoneyxchangeSharedModule {}

import { Component, OnInit, OnDestroy } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';

import { Account, LoginModalService, Principal } from '../shared';
import { HomeService } from './home.service';
import {ExchangeResponse} from './home.model';
import { TimerManagerService } from '../shared/timer/timer-manager.service';
import { SessionStorageService } from 'ngx-webstorage';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit, OnDestroy  {
    account: Account;
    modalRef: NgbModalRef;

    dollarVal: number;
    euroVal: number;
    showTimer: boolean;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private homeService: HomeService,
        private timerManager: TimerManagerService,
        private $sessionStorage: SessionStorageService

    ) {
        this.dollarVal = null;
        this.euroVal = null;
        this.showTimer = false;
    }

    updateFields(exchangeResponse: HttpResponse<ExchangeResponse>) {
        if (exchangeResponse.body.operation === 'MULTIPLY') {
            this.euroVal = this.dollarVal * exchangeResponse.body.exchangeRate;
        } else {
            this.euroVal = this.dollarVal / exchangeResponse.body.exchangeRate;
        }
    }

    calculate() {
        if (this.dollarVal != null) {
            if (this.$sessionStorage.retrieve('exchange') == null) {
                this.homeService.exchange('USD', 'EUR')
                    .subscribe(( exchangeResponse: HttpResponse<ExchangeResponse> ) => {
                        this.$sessionStorage.store('exchange', exchangeResponse);
                        this.updateFields(exchangeResponse);
                        this.timerManager.beginTimer(exchangeResponse.body.timeout * 60);
                        this.showTimer = true;
                    });
            } else {
                this.updateFields(this.$sessionStorage.retrieve('exchange'));
            }
        } else {
           this.euroVal = null;
        }

    }

    ngOnInit() {
        this.$sessionStorage.store('exchange', null);
        this.dollarVal = null;
        this.euroVal = null;
        this.showTimer = false;
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.timerManager.timerEndedObservable.subscribe((bol) => {
            if (bol) {
                this.showTimer = false;
                this.$sessionStorage.store('exchange', null);
                this.calculate();
            }
        });
    }

    ngOnDestroy() {
        this.showTimer = false;
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
                this.$sessionStorage.store('exchange', null);
                this.dollarVal = null;
                this.euroVal = null;
                this.showTimer = false;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();

    }
}

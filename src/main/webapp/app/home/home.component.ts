import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';
import { HttpResponse } from '@angular/common/http';

import { Account, LoginModalService, Principal } from '../shared';
import { HomeService } from './home.service';
import {ExchangeResponse} from './home.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    dollarVal: number;
    euroVal: number;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private homeService: HomeService
    ) {
    }

    calculate() {
        this.homeService.exchange('USD', 'EUR')
            .subscribe(( exchangeResponse: HttpResponse<ExchangeResponse> ) => {
                if (exchangeResponse.body.operation === 'MULTIPLY') {
                    this.euroVal = this.dollarVal * exchangeResponse.body.exchangeRate;
                } else {
                    this.euroVal = this.dollarVal / exchangeResponse.body.exchangeRate;
                }
            });
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
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

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SpotExchange } from './spot-exchange.model';
import { SpotExchangePopupService } from './spot-exchange-popup.service';
import { SpotExchangeService } from './spot-exchange.service';
import { Currency, CurrencyService } from '../currency';

@Component({
    selector: 'jhi-spot-exchange-dialog',
    templateUrl: './spot-exchange-dialog.component.html'
})
export class SpotExchangeDialogComponent implements OnInit {

    spotExchange: SpotExchange;
    isSaving: boolean;

    currencies: Currency[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private spotExchangeService: SpotExchangeService,
        private currencyService: CurrencyService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.currencyService.query()
            .subscribe((res: HttpResponse<Currency[]>) => { this.currencies = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.spotExchange.id !== undefined) {
            this.subscribeToSaveResponse(
                this.spotExchangeService.update(this.spotExchange));
        } else {
            this.subscribeToSaveResponse(
                this.spotExchangeService.create(this.spotExchange));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SpotExchange>>) {
        result.subscribe((res: HttpResponse<SpotExchange>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SpotExchange) {
        this.eventManager.broadcast({ name: 'spotExchangeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCurrencyById(index: number, item: Currency) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-spot-exchange-popup',
    template: ''
})
export class SpotExchangePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private spotExchangePopupService: SpotExchangePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.spotExchangePopupService
                    .open(SpotExchangeDialogComponent as Component, params['id']);
            } else {
                this.spotExchangePopupService
                    .open(SpotExchangeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

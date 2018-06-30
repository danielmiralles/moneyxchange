import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SpotExchange } from './spot-exchange.model';
import { SpotExchangeService } from './spot-exchange.service';

@Component({
    selector: 'jhi-spot-exchange-detail',
    templateUrl: './spot-exchange-detail.component.html'
})
export class SpotExchangeDetailComponent implements OnInit, OnDestroy {

    spotExchange: SpotExchange;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private spotExchangeService: SpotExchangeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSpotExchanges();
    }

    load(id) {
        this.spotExchangeService.find(id)
            .subscribe((spotExchangeResponse: HttpResponse<SpotExchange>) => {
                this.spotExchange = spotExchangeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSpotExchanges() {
        this.eventSubscriber = this.eventManager.subscribe(
            'spotExchangeListModification',
            (response) => this.load(this.spotExchange.id)
        );
    }
}

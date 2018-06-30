import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SpotExchange } from './spot-exchange.model';
import { SpotExchangePopupService } from './spot-exchange-popup.service';
import { SpotExchangeService } from './spot-exchange.service';

@Component({
    selector: 'jhi-spot-exchange-delete-dialog',
    templateUrl: './spot-exchange-delete-dialog.component.html'
})
export class SpotExchangeDeleteDialogComponent {

    spotExchange: SpotExchange;

    constructor(
        private spotExchangeService: SpotExchangeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.spotExchangeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'spotExchangeListModification',
                content: 'Deleted an spotExchange'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-spot-exchange-delete-popup',
    template: ''
})
export class SpotExchangeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private spotExchangePopupService: SpotExchangePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.spotExchangePopupService
                .open(SpotExchangeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}

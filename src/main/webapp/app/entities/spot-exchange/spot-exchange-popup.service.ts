import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { SpotExchange } from './spot-exchange.model';
import { SpotExchangeService } from './spot-exchange.service';

@Injectable()
export class SpotExchangePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private spotExchangeService: SpotExchangeService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.spotExchangeService.find(id)
                    .subscribe((spotExchangeResponse: HttpResponse<SpotExchange>) => {
                        const spotExchange: SpotExchange = spotExchangeResponse.body;
                        spotExchange.fromInstant = this.datePipe
                            .transform(spotExchange.fromInstant, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.spotExchangeModalRef(component, spotExchange);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.spotExchangeModalRef(component, new SpotExchange());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    spotExchangeModalRef(component: Component, spotExchange: SpotExchange): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.spotExchange = spotExchange;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}

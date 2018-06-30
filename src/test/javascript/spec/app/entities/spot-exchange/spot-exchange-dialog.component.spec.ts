/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MoneyxchangeTestModule } from '../../../test.module';
import { SpotExchangeDialogComponent } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange-dialog.component';
import { SpotExchangeService } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.service';
import { SpotExchange } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.model';
import { CurrencyService } from '../../../../../../main/webapp/app/entities/currency';

describe('Component Tests', () => {

    describe('SpotExchange Management Dialog Component', () => {
        let comp: SpotExchangeDialogComponent;
        let fixture: ComponentFixture<SpotExchangeDialogComponent>;
        let service: SpotExchangeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MoneyxchangeTestModule],
                declarations: [SpotExchangeDialogComponent],
                providers: [
                    CurrencyService,
                    SpotExchangeService
                ]
            })
            .overrideTemplate(SpotExchangeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpotExchangeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotExchangeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SpotExchange(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.spotExchange = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'spotExchangeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new SpotExchange();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.spotExchange = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'spotExchangeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

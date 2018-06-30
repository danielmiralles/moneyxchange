/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { MoneyxchangeTestModule } from '../../../test.module';
import { SpotExchangeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange-delete-dialog.component';
import { SpotExchangeService } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.service';

describe('Component Tests', () => {

    describe('SpotExchange Management Delete Component', () => {
        let comp: SpotExchangeDeleteDialogComponent;
        let fixture: ComponentFixture<SpotExchangeDeleteDialogComponent>;
        let service: SpotExchangeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MoneyxchangeTestModule],
                declarations: [SpotExchangeDeleteDialogComponent],
                providers: [
                    SpotExchangeService
                ]
            })
            .overrideTemplate(SpotExchangeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpotExchangeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotExchangeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});

/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { MoneyxchangeTestModule } from '../../../test.module';
import { SpotExchangeDetailComponent } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange-detail.component';
import { SpotExchangeService } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.service';
import { SpotExchange } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.model';

describe('Component Tests', () => {

    describe('SpotExchange Management Detail Component', () => {
        let comp: SpotExchangeDetailComponent;
        let fixture: ComponentFixture<SpotExchangeDetailComponent>;
        let service: SpotExchangeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MoneyxchangeTestModule],
                declarations: [SpotExchangeDetailComponent],
                providers: [
                    SpotExchangeService
                ]
            })
            .overrideTemplate(SpotExchangeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpotExchangeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotExchangeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new SpotExchange(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.spotExchange).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

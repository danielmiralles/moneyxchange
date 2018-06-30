/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MoneyxchangeTestModule } from '../../../test.module';
import { SpotExchangeComponent } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.component';
import { SpotExchangeService } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.service';
import { SpotExchange } from '../../../../../../main/webapp/app/entities/spot-exchange/spot-exchange.model';

describe('Component Tests', () => {

    describe('SpotExchange Management Component', () => {
        let comp: SpotExchangeComponent;
        let fixture: ComponentFixture<SpotExchangeComponent>;
        let service: SpotExchangeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [MoneyxchangeTestModule],
                declarations: [SpotExchangeComponent],
                providers: [
                    SpotExchangeService
                ]
            })
            .overrideTemplate(SpotExchangeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpotExchangeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpotExchangeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new SpotExchange(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.spotExchanges[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});

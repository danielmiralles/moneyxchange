import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('SpotExchange e2e test', () => {

    let navBarPage: NavBarPage;
    let spotExchangeDialogPage: SpotExchangeDialogPage;
    let spotExchangeComponentsPage: SpotExchangeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load SpotExchanges', () => {
        navBarPage.goToEntity('spot-exchange');
        spotExchangeComponentsPage = new SpotExchangeComponentsPage();
        expect(spotExchangeComponentsPage.getTitle())
            .toMatch(/moneyxchangeApp.spotExchange.home.title/);

    });

    it('should load create SpotExchange dialog', () => {
        spotExchangeComponentsPage.clickOnCreateButton();
        spotExchangeDialogPage = new SpotExchangeDialogPage();
        expect(spotExchangeDialogPage.getModalTitle())
            .toMatch(/moneyxchangeApp.spotExchange.home.createOrEditLabel/);
        spotExchangeDialogPage.close();
    });

   /* it('should create and save SpotExchanges', () => {
        spotExchangeComponentsPage.clickOnCreateButton();
        spotExchangeDialogPage.setFromInstantInput(12310020012301);
        expect(spotExchangeDialogPage.getFromInstantInput()).toMatch('2001-12-31T02:30');
        spotExchangeDialogPage.operationSelectLastOption();
        spotExchangeDialogPage.sourceCurrencySelectLastOption();
        spotExchangeDialogPage.targetCurrencySelectLastOption();
        spotExchangeDialogPage.save();
        expect(spotExchangeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });*/

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SpotExchangeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-spot-exchange div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SpotExchangeDialogPage {
    modalTitle = element(by.css('h4#mySpotExchangeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    fromInstantInput = element(by.css('input#field_fromInstant'));
    operationSelect = element(by.css('select#field_operation'));
    sourceCurrencySelect = element(by.css('select#field_sourceCurrency'));
    targetCurrencySelect = element(by.css('select#field_targetCurrency'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFromInstantInput = function(fromInstant) {
        this.fromInstantInput.sendKeys(fromInstant);
    };

    getFromInstantInput = function() {
        return this.fromInstantInput.getAttribute('value');
    };

    setOperationSelect = function(operation) {
        this.operationSelect.sendKeys(operation);
    };

    getOperationSelect = function() {
        return this.operationSelect.element(by.css('option:checked')).getText();
    };

    operationSelectLastOption = function() {
        this.operationSelect.all(by.tagName('option')).last().click();
    };
    sourceCurrencySelectLastOption = function() {
        this.sourceCurrencySelect.all(by.tagName('option')).last().click();
    };

    sourceCurrencySelectOption = function(option) {
        this.sourceCurrencySelect.sendKeys(option);
    };

    getSourceCurrencySelect = function() {
        return this.sourceCurrencySelect;
    };

    getSourceCurrencySelectedOption = function() {
        return this.sourceCurrencySelect.element(by.css('option:checked')).getText();
    };

    targetCurrencySelectLastOption = function() {
        this.targetCurrencySelect.all(by.tagName('option')).last().click();
    };

    targetCurrencySelectOption = function(option) {
        this.targetCurrencySelect.sendKeys(option);
    };

    getTargetCurrencySelect = function() {
        return this.targetCurrencySelect;
    };

    getTargetCurrencySelectedOption = function() {
        return this.targetCurrencySelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}

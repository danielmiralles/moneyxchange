import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Currency e2e test', () => {

    let navBarPage: NavBarPage;
    let currencyDialogPage: CurrencyDialogPage;
    let currencyComponentsPage: CurrencyComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Currencies', () => {
        navBarPage.goToEntity('currency');
        currencyComponentsPage = new CurrencyComponentsPage();
        expect(currencyComponentsPage.getTitle())
            .toMatch(/moneyxchangeApp.currency.home.title/);

    });

    it('should load create Currency dialog', () => {
        currencyComponentsPage.clickOnCreateButton();
        currencyDialogPage = new CurrencyDialogPage();
        expect(currencyDialogPage.getModalTitle())
            .toMatch(/moneyxchangeApp.currency.home.createOrEditLabel/);
        currencyDialogPage.close();
    });

    it('should create and save Currencies', () => {
        currencyComponentsPage.clickOnCreateButton();
        currencyDialogPage.setCodeInput('code');
        expect(currencyDialogPage.getCodeInput()).toMatch('code');
        currencyDialogPage.setSymbolInput('symbol');
        expect(currencyDialogPage.getSymbolInput()).toMatch('symbol');
        currencyDialogPage.getSymbolLeftInput().isSelected().then((selected) => {
            if (selected) {
                currencyDialogPage.getSymbolLeftInput().click();
                expect(currencyDialogPage.getSymbolLeftInput().isSelected()).toBeFalsy();
            } else {
                currencyDialogPage.getSymbolLeftInput().click();
                expect(currencyDialogPage.getSymbolLeftInput().isSelected()).toBeTruthy();
            }
        });
        currencyDialogPage.setDescriptionInput('description');
        expect(currencyDialogPage.getDescriptionInput()).toMatch('description');
        currencyDialogPage.save();
        expect(currencyDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class CurrencyComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-currency div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class CurrencyDialogPage {
    modalTitle = element(by.css('h4#myCurrencyLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    codeInput = element(by.css('input#field_code'));
    symbolInput = element(by.css('input#field_symbol'));
    symbolLeftInput = element(by.css('input#field_symbolLeft'));
    descriptionInput = element(by.css('input#field_description'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setCodeInput = function(code) {
        this.codeInput.sendKeys(code);
    };

    getCodeInput = function() {
        return this.codeInput.getAttribute('value');
    };

    setSymbolInput = function(symbol) {
        this.symbolInput.sendKeys(symbol);
    };

    getSymbolInput = function() {
        return this.symbolInput.getAttribute('value');
    };

    getSymbolLeftInput = function() {
        return this.symbolLeftInput;
    };
    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    };

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
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

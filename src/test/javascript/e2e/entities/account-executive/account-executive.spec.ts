import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import AccountExecutiveComponentsPage from './account-executive.page-object';
import AccountExecutiveUpdatePage from './account-executive-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('AccountExecutive e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accountExecutiveComponentsPage: AccountExecutiveComponentsPage;
  let accountExecutiveUpdatePage: AccountExecutiveUpdatePage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    accountExecutiveComponentsPage = new AccountExecutiveComponentsPage();
    accountExecutiveComponentsPage = await accountExecutiveComponentsPage.goToPage(navBarPage);
  });

  it('should load AccountExecutives', async () => {
    expect(await accountExecutiveComponentsPage.title.getText()).to.match(/Account Executives/);
    expect(await accountExecutiveComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete AccountExecutives', async () => {
    const beforeRecordsCount = (await isVisible(accountExecutiveComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(accountExecutiveComponentsPage.table);
    accountExecutiveUpdatePage = await accountExecutiveComponentsPage.goToCreateAccountExecutive();
    await accountExecutiveUpdatePage.enterData();

    expect(await accountExecutiveComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(accountExecutiveComponentsPage.table);
    await waitUntilCount(accountExecutiveComponentsPage.records, beforeRecordsCount + 1);
    expect(await accountExecutiveComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await accountExecutiveComponentsPage.deleteAccountExecutive();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(accountExecutiveComponentsPage.records, beforeRecordsCount);
      expect(await accountExecutiveComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(accountExecutiveComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import AccExecComponentsPage from './acc-exec.page-object';
import AccExecUpdatePage from './acc-exec-update.page-object';
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

describe('AccExec e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let accExecComponentsPage: AccExecComponentsPage;
  let accExecUpdatePage: AccExecUpdatePage;

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
    accExecComponentsPage = new AccExecComponentsPage();
    accExecComponentsPage = await accExecComponentsPage.goToPage(navBarPage);
  });

  it('should load AccExecs', async () => {
    expect(await accExecComponentsPage.title.getText()).to.match(/Acc Execs/);
    expect(await accExecComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete AccExecs', async () => {
    const beforeRecordsCount = (await isVisible(accExecComponentsPage.noRecords)) ? 0 : await getRecordsCount(accExecComponentsPage.table);
    accExecUpdatePage = await accExecComponentsPage.goToCreateAccExec();
    await accExecUpdatePage.enterData();

    expect(await accExecComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(accExecComponentsPage.table);
    await waitUntilCount(accExecComponentsPage.records, beforeRecordsCount + 1);
    expect(await accExecComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await accExecComponentsPage.deleteAccExec();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(accExecComponentsPage.records, beforeRecordsCount);
      expect(await accExecComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(accExecComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

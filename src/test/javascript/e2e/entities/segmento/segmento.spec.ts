import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SegmentoComponentsPage from './segmento.page-object';
import SegmentoUpdatePage from './segmento-update.page-object';
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

describe('Segmento e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let segmentoComponentsPage: SegmentoComponentsPage;
  let segmentoUpdatePage: SegmentoUpdatePage;

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
    segmentoComponentsPage = new SegmentoComponentsPage();
    segmentoComponentsPage = await segmentoComponentsPage.goToPage(navBarPage);
  });

  it('should load Segmentos', async () => {
    expect(await segmentoComponentsPage.title.getText()).to.match(/Segmentos/);
    expect(await segmentoComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Segmentos', async () => {
    const beforeRecordsCount = (await isVisible(segmentoComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(segmentoComponentsPage.table);
    segmentoUpdatePage = await segmentoComponentsPage.goToCreateSegmento();
    await segmentoUpdatePage.enterData();

    expect(await segmentoComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(segmentoComponentsPage.table);
    await waitUntilCount(segmentoComponentsPage.records, beforeRecordsCount + 1);
    expect(await segmentoComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await segmentoComponentsPage.deleteSegmento();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(segmentoComponentsPage.records, beforeRecordsCount);
      expect(await segmentoComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(segmentoComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

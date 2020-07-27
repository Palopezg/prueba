import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import AccExecUpdatePage from './acc-exec-update.page-object';

const expect = chai.expect;
export class AccExecDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('testElasticSearchApp.accExec.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-accExec'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class AccExecComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('acc-exec-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('acc-exec');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateAccExec() {
    await this.createButton.click();
    return new AccExecUpdatePage();
  }

  async deleteAccExec() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const accExecDeleteDialog = new AccExecDeleteDialog();
    await waitUntilDisplayed(accExecDeleteDialog.deleteModal);
    expect(await accExecDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/testElasticSearchApp.accExec.delete.question/);
    await accExecDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(accExecDeleteDialog.deleteModal);

    expect(await isVisible(accExecDeleteDialog.deleteModal)).to.be.false;
  }
}

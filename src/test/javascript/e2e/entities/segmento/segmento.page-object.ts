import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import SegmentoUpdatePage from './segmento-update.page-object';

const expect = chai.expect;
export class SegmentoDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('testElasticSearchApp.segmento.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-segmento'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class SegmentoComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('segmento-heading'));
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
    await navBarPage.getEntityPage('segmento');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateSegmento() {
    await this.createButton.click();
    return new SegmentoUpdatePage();
  }

  async deleteSegmento() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const segmentoDeleteDialog = new SegmentoDeleteDialog();
    await waitUntilDisplayed(segmentoDeleteDialog.deleteModal);
    expect(await segmentoDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/testElasticSearchApp.segmento.delete.question/);
    await segmentoDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(segmentoDeleteDialog.deleteModal);

    expect(await isVisible(segmentoDeleteDialog.deleteModal)).to.be.false;
  }
}

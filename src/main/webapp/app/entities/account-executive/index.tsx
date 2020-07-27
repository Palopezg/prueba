import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccountExecutive from './account-executive';
import AccountExecutiveDetail from './account-executive-detail';
import AccountExecutiveUpdate from './account-executive-update';
import AccountExecutiveDeleteDialog from './account-executive-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccountExecutiveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccountExecutiveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccountExecutiveDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccountExecutive} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccountExecutiveDeleteDialog} />
  </>
);

export default Routes;

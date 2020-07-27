import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccExec from './acc-exec';
import AccExecDetail from './acc-exec-detail';
import AccExecUpdate from './acc-exec-update';
import AccExecDeleteDialog from './acc-exec-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccExecUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccExecUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccExecDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccExec} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AccExecDeleteDialog} />
  </>
);

export default Routes;

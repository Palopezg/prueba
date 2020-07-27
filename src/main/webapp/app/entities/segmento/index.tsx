import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Segmento from './segmento';
import SegmentoDetail from './segmento-detail';
import SegmentoUpdate from './segmento-update';
import SegmentoDeleteDialog from './segmento-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SegmentoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SegmentoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SegmentoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Segmento} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SegmentoDeleteDialog} />
  </>
);

export default Routes;

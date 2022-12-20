import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ExpertUser from './expert-user';
import ExpertUserDetail from './expert-user-detail';
import ExpertUserUpdate from './expert-user-update';
import ExpertUserDeleteDialog from './expert-user-delete-dialog';

const ExpertUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ExpertUser />} />
    <Route path="new" element={<ExpertUserUpdate />} />
    <Route path=":id">
      <Route index element={<ExpertUserDetail />} />
      <Route path="edit" element={<ExpertUserUpdate />} />
      <Route path="delete" element={<ExpertUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ExpertUserRoutes;

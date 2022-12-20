import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserAccount from './user-account';
import Post from './post';
import ExpertUser from './expert-user';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-account/*" element={<UserAccount />} />
        <Route path="post/*" element={<Post />} />
        <Route path="expert-user/*" element={<ExpertUser />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

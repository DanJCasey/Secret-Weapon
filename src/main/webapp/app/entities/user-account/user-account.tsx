import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserAccount } from 'app/shared/model/user-account.model';
import { getEntities } from './user-account.reducer';

export const UserAccount = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const userAccountList = useAppSelector(state => state.userAccount.entities);
  const loading = useAppSelector(state => state.userAccount.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="user-account-heading" data-cy="UserAccountHeading">
        <Translate contentKey="secretWeaponApp.userAccount.home.title">User Accounts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="secretWeaponApp.userAccount.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-account/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="secretWeaponApp.userAccount.home.createLabel">Create new User Account</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAccountList && userAccountList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="secretWeaponApp.userAccount.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="secretWeaponApp.userAccount.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="secretWeaponApp.userAccount.password">Password</Translate>
                </th>
                <th>
                  <Translate contentKey="secretWeaponApp.userAccount.user">User</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAccountList.map((userAccount, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-account/${userAccount.id}`} color="link" size="sm">
                      {userAccount.id}
                    </Button>
                  </td>
                  <td>{userAccount.name}</td>
                  <td>{userAccount.password}</td>
                  <td>{userAccount.user ? userAccount.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-account/${userAccount.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-account/${userAccount.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-account/${userAccount.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="secretWeaponApp.userAccount.home.notFound">No User Accounts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserAccount;

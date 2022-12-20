import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IExpertUser } from 'app/shared/model/expert-user.model';
import { getEntities } from './expert-user.reducer';

export const ExpertUser = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const expertUserList = useAppSelector(state => state.expertUser.entities);
  const loading = useAppSelector(state => state.expertUser.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="expert-user-heading" data-cy="ExpertUserHeading">
        <Translate contentKey="secretWeaponApp.expertUser.home.title">Expert Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="secretWeaponApp.expertUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/expert-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="secretWeaponApp.expertUser.home.createLabel">Create new Expert User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {expertUserList && expertUserList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="secretWeaponApp.expertUser.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="secretWeaponApp.expertUser.expertise">Expertise</Translate>
                </th>
                <th>
                  <Translate contentKey="secretWeaponApp.expertUser.userAccount">User Account</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {expertUserList.map((expertUser, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/expert-user/${expertUser.id}`} color="link" size="sm">
                      {expertUser.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`secretWeaponApp.Expertise.${expertUser.expertise}`} />
                  </td>
                  <td>
                    {expertUser.userAccount ? (
                      <Link to={`/user-account/${expertUser.userAccount.id}`}>{expertUser.userAccount.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/expert-user/${expertUser.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/expert-user/${expertUser.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/expert-user/${expertUser.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="secretWeaponApp.expertUser.home.notFound">No Expert Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ExpertUser;

import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './expert-user.reducer';

export const ExpertUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const expertUserEntity = useAppSelector(state => state.expertUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="expertUserDetailsHeading">
          <Translate contentKey="secretWeaponApp.expertUser.detail.title">ExpertUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{expertUserEntity.id}</dd>
          <dt>
            <span id="expertise">
              <Translate contentKey="secretWeaponApp.expertUser.expertise">Expertise</Translate>
            </span>
          </dt>
          <dd>{expertUserEntity.expertise}</dd>
          <dt>
            <Translate contentKey="secretWeaponApp.expertUser.userAccount">User Account</Translate>
          </dt>
          <dd>{expertUserEntity.userAccount ? expertUserEntity.userAccount.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/expert-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/expert-user/${expertUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ExpertUserDetail;

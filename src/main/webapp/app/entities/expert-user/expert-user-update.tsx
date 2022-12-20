import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserAccount } from 'app/shared/model/user-account.model';
import { getEntities as getUserAccounts } from 'app/entities/user-account/user-account.reducer';
import { IExpertUser } from 'app/shared/model/expert-user.model';
import { Expertise } from 'app/shared/model/enumerations/expertise.model';
import { getEntity, updateEntity, createEntity, reset } from './expert-user.reducer';

export const ExpertUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userAccounts = useAppSelector(state => state.userAccount.entities);
  const expertUserEntity = useAppSelector(state => state.expertUser.entity);
  const loading = useAppSelector(state => state.expertUser.loading);
  const updating = useAppSelector(state => state.expertUser.updating);
  const updateSuccess = useAppSelector(state => state.expertUser.updateSuccess);
  const expertiseValues = Object.keys(Expertise);

  const handleClose = () => {
    navigate('/expert-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserAccounts({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...expertUserEntity,
      ...values,
      userAccount: userAccounts.find(it => it.id.toString() === values.userAccount.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          expertise: 'PLUMBING',
          ...expertUserEntity,
          userAccount: expertUserEntity?.userAccount?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="secretWeaponApp.expertUser.home.createOrEditLabel" data-cy="ExpertUserCreateUpdateHeading">
            <Translate contentKey="secretWeaponApp.expertUser.home.createOrEditLabel">Create or edit a ExpertUser</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="expert-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('secretWeaponApp.expertUser.expertise')}
                id="expert-user-expertise"
                name="expertise"
                data-cy="expertise"
                type="select"
              >
                {expertiseValues.map(expertise => (
                  <option value={expertise} key={expertise}>
                    {translate('secretWeaponApp.Expertise.' + expertise)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="expert-user-userAccount"
                name="userAccount"
                data-cy="userAccount"
                label={translate('secretWeaponApp.expertUser.userAccount')}
                type="select"
              >
                <option value="" key="0" />
                {userAccounts
                  ? userAccounts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/expert-user" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ExpertUserUpdate;

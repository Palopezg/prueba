import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './account-executive.reducer';
import { IAccountExecutive } from 'app/shared/model/account-executive.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAccountExecutiveUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountExecutiveUpdate = (props: IAccountExecutiveUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { accountExecutiveEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/account-executive' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...accountExecutiveEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="testElasticSearchApp.accountExecutive.home.createOrEditLabel">Create or edit a AccountExecutive</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : accountExecutiveEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="account-executive-id">ID</Label>
                  <AvInput id="account-executive-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="account-executive-nombre">
                  Nombre
                </Label>
                <AvField id="account-executive-nombre" type="text" name="nombre" />
              </AvGroup>
              <AvGroup>
                <Label id="apellidoLabel" for="account-executive-apellido">
                  Apellido
                </Label>
                <AvField id="account-executive-apellido" type="text" name="apellido" />
              </AvGroup>
              <AvGroup>
                <Label id="telefonoLabel" for="account-executive-telefono">
                  Telefono
                </Label>
                <AvField id="account-executive-telefono" type="text" name="telefono" />
              </AvGroup>
              <AvGroup>
                <Label id="celularLabel" for="account-executive-celular">
                  Celular
                </Label>
                <AvField id="account-executive-celular" type="text" name="celular" />
              </AvGroup>
              <AvGroup>
                <Label id="mailLabel" for="account-executive-mail">
                  Mail
                </Label>
                <AvField id="account-executive-mail" type="text" name="mail" />
              </AvGroup>
              <AvGroup>
                <Label id="repcom1Label" for="account-executive-repcom1">
                  Repcom 1
                </Label>
                <AvField id="account-executive-repcom1" type="text" name="repcom1" />
              </AvGroup>
              <AvGroup>
                <Label id="repcom2Label" for="account-executive-repcom2">
                  Repcom 2
                </Label>
                <AvField id="account-executive-repcom2" type="text" name="repcom2" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/account-executive" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  accountExecutiveEntity: storeState.accountExecutive.entity,
  loading: storeState.accountExecutive.loading,
  updating: storeState.accountExecutive.updating,
  updateSuccess: storeState.accountExecutive.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountExecutiveUpdate);

import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRegion } from 'app/shared/model/region.model';
import { getEntities as getRegions } from 'app/entities/region/region.reducer';
import { getEntity, updateEntity, createEntity, reset } from './acc-exec.reducer';
import { IAccExec } from 'app/shared/model/acc-exec.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAccExecUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccExecUpdate = (props: IAccExecUpdateProps) => {
  const [idsregion, setIdsregion] = useState([]);
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { accExecEntity, regions, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/acc-exec' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getRegions();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...accExecEntity,
        ...values,
        regions: mapIdList(values.regions),
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
          <h2 id="testElasticSearchApp.accExec.home.createOrEditLabel">Create or edit a AccExec</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : accExecEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="acc-exec-id">ID</Label>
                  <AvInput id="acc-exec-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nombreLabel" for="acc-exec-nombre">
                  Nombre
                </Label>
                <AvField id="acc-exec-nombre" type="text" name="nombre" />
              </AvGroup>
              <AvGroup>
                <Label id="apellidoLabel" for="acc-exec-apellido">
                  Apellido
                </Label>
                <AvField id="acc-exec-apellido" type="text" name="apellido" />
              </AvGroup>
              <AvGroup>
                <Label id="telefonoLabel" for="acc-exec-telefono">
                  Telefono
                </Label>
                <AvField id="acc-exec-telefono" type="text" name="telefono" />
              </AvGroup>
              <AvGroup>
                <Label id="celularLabel" for="acc-exec-celular">
                  Celular
                </Label>
                <AvField id="acc-exec-celular" type="text" name="celular" />
              </AvGroup>
              <AvGroup>
                <Label id="mailLabel" for="acc-exec-mail">
                  Mail
                </Label>
                <AvField id="acc-exec-mail" type="text" name="mail" />
              </AvGroup>
              <AvGroup>
                <Label id="repcom1Label" for="acc-exec-repcom1">
                  Repcom 1
                </Label>
                <AvField id="acc-exec-repcom1" type="text" name="repcom1" />
              </AvGroup>
              <AvGroup>
                <Label id="repcom2Label" for="acc-exec-repcom2">
                  Repcom 2
                </Label>
                <AvField id="acc-exec-repcom2" type="text" name="repcom2" />
              </AvGroup>
              <AvGroup>
                <Label for="acc-exec-region">Region</Label>
                <AvInput
                  id="acc-exec-region"
                  type="select"
                  multiple
                  className="form-control"
                  name="regions"
                  value={accExecEntity.regions && accExecEntity.regions.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {regions
                    ? regions.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/acc-exec" replace color="info">
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
  regions: storeState.region.entities,
  accExecEntity: storeState.accExec.entity,
  loading: storeState.accExec.loading,
  updating: storeState.accExec.updating,
  updateSuccess: storeState.accExec.updateSuccess,
});

const mapDispatchToProps = {
  getRegions,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccExecUpdate);

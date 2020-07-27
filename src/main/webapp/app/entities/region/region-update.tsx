import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAccExec } from 'app/shared/model/acc-exec.model';
import { getEntities as getAccExecs } from 'app/entities/acc-exec/acc-exec.reducer';
import { getEntity, updateEntity, createEntity, reset } from './region.reducer';
import { IRegion } from 'app/shared/model/region.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRegionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RegionUpdate = (props: IRegionUpdateProps) => {
  const [accExecId, setAccExecId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { regionEntity, accExecs, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/region' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getAccExecs();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...regionEntity,
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
          <h2 id="testElasticSearchApp.region.home.createOrEditLabel">Create or edit a Region</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : regionEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="region-id">ID</Label>
                  <AvInput id="region-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="descripcionLabel" for="region-descripcion">
                  Descripcion
                </Label>
                <AvField id="region-descripcion" type="text" name="descripcion" />
              </AvGroup>
              <AvGroup>
                <Label id="valorLabel" for="region-valor">
                  Valor
                </Label>
                <AvField id="region-valor" type="text" name="valor" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/region" replace color="info">
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
  accExecs: storeState.accExec.entities,
  regionEntity: storeState.region.entity,
  loading: storeState.region.loading,
  updating: storeState.region.updating,
  updateSuccess: storeState.region.updateSuccess,
});

const mapDispatchToProps = {
  getAccExecs,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RegionUpdate);

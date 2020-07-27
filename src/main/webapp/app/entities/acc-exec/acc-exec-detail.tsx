import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './acc-exec.reducer';
import { IAccExec } from 'app/shared/model/acc-exec.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccExecDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccExecDetail = (props: IAccExecDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { accExecEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          AccExec [<b>{accExecEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{accExecEntity.nombre}</dd>
          <dt>
            <span id="apellido">Apellido</span>
          </dt>
          <dd>{accExecEntity.apellido}</dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{accExecEntity.telefono}</dd>
          <dt>
            <span id="celular">Celular</span>
          </dt>
          <dd>{accExecEntity.celular}</dd>
          <dt>
            <span id="mail">Mail</span>
          </dt>
          <dd>{accExecEntity.mail}</dd>
          <dt>
            <span id="repcom1">Repcom 1</span>
          </dt>
          <dd>{accExecEntity.repcom1}</dd>
          <dt>
            <span id="repcom2">Repcom 2</span>
          </dt>
          <dd>{accExecEntity.repcom2}</dd>
          <dt>Region</dt>
          <dd>
            {accExecEntity.regions
              ? accExecEntity.regions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {accExecEntity.regions && i === accExecEntity.regions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/acc-exec" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/acc-exec/${accExecEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ accExec }: IRootState) => ({
  accExecEntity: accExec.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccExecDetail);

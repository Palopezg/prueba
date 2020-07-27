import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './account-executive.reducer';
import { IAccountExecutive } from 'app/shared/model/account-executive.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAccountExecutiveDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AccountExecutiveDetail = (props: IAccountExecutiveDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { accountExecutiveEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          AccountExecutive [<b>{accountExecutiveEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="nombre">Nombre</span>
          </dt>
          <dd>{accountExecutiveEntity.nombre}</dd>
          <dt>
            <span id="apellido">Apellido</span>
          </dt>
          <dd>{accountExecutiveEntity.apellido}</dd>
          <dt>
            <span id="telefono">Telefono</span>
          </dt>
          <dd>{accountExecutiveEntity.telefono}</dd>
          <dt>
            <span id="celular">Celular</span>
          </dt>
          <dd>{accountExecutiveEntity.celular}</dd>
          <dt>
            <span id="mail">Mail</span>
          </dt>
          <dd>{accountExecutiveEntity.mail}</dd>
          <dt>
            <span id="repcom1">Repcom 1</span>
          </dt>
          <dd>{accountExecutiveEntity.repcom1}</dd>
          <dt>
            <span id="repcom2">Repcom 2</span>
          </dt>
          <dd>{accountExecutiveEntity.repcom2}</dd>
        </dl>
        <Button tag={Link} to="/account-executive" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/account-executive/${accountExecutiveEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ accountExecutive }: IRootState) => ({
  accountExecutiveEntity: accountExecutive.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AccountExecutiveDetail);

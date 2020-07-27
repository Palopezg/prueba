import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './segmento.reducer';
import { ISegmento } from 'app/shared/model/segmento.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISegmentoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SegmentoDetail = (props: ISegmentoDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { segmentoEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Segmento [<b>{segmentoEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="descripcion">Descripcion</span>
          </dt>
          <dd>{segmentoEntity.descripcion}</dd>
          <dt>
            <span id="valor">Valor</span>
          </dt>
          <dd>{segmentoEntity.valor}</dd>
        </dl>
        <Button tag={Link} to="/segmento" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/segmento/${segmentoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ segmento }: IRootState) => ({
  segmentoEntity: segmento.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SegmentoDetail);

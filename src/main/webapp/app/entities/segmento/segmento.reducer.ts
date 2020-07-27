import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISegmento, defaultValue } from 'app/shared/model/segmento.model';

export const ACTION_TYPES = {
  SEARCH_SEGMENTOS: 'segmento/SEARCH_SEGMENTOS',
  FETCH_SEGMENTO_LIST: 'segmento/FETCH_SEGMENTO_LIST',
  FETCH_SEGMENTO: 'segmento/FETCH_SEGMENTO',
  CREATE_SEGMENTO: 'segmento/CREATE_SEGMENTO',
  UPDATE_SEGMENTO: 'segmento/UPDATE_SEGMENTO',
  DELETE_SEGMENTO: 'segmento/DELETE_SEGMENTO',
  RESET: 'segmento/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISegmento>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type SegmentoState = Readonly<typeof initialState>;

// Reducer

export default (state: SegmentoState = initialState, action): SegmentoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SEGMENTOS):
    case REQUEST(ACTION_TYPES.FETCH_SEGMENTO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SEGMENTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SEGMENTO):
    case REQUEST(ACTION_TYPES.UPDATE_SEGMENTO):
    case REQUEST(ACTION_TYPES.DELETE_SEGMENTO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_SEGMENTOS):
    case FAILURE(ACTION_TYPES.FETCH_SEGMENTO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SEGMENTO):
    case FAILURE(ACTION_TYPES.CREATE_SEGMENTO):
    case FAILURE(ACTION_TYPES.UPDATE_SEGMENTO):
    case FAILURE(ACTION_TYPES.DELETE_SEGMENTO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SEGMENTOS):
    case SUCCESS(ACTION_TYPES.FETCH_SEGMENTO_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_SEGMENTO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SEGMENTO):
    case SUCCESS(ACTION_TYPES.UPDATE_SEGMENTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SEGMENTO):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/segmentos';
const apiSearchUrl = 'api/_search/segmentos';

// Actions

export const getSearchEntities: ICrudSearchAction<ISegmento> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SEGMENTOS,
  payload: axios.get<ISegmento>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<ISegmento> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SEGMENTO_LIST,
    payload: axios.get<ISegmento>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ISegmento> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SEGMENTO,
    payload: axios.get<ISegmento>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISegmento> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SEGMENTO,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISegmento> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SEGMENTO,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISegmento> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SEGMENTO,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccExec, defaultValue } from 'app/shared/model/acc-exec.model';

export const ACTION_TYPES = {
  SEARCH_ACCEXECS: 'accExec/SEARCH_ACCEXECS',
  FETCH_ACCEXEC_LIST: 'accExec/FETCH_ACCEXEC_LIST',
  FETCH_ACCEXEC: 'accExec/FETCH_ACCEXEC',
  CREATE_ACCEXEC: 'accExec/CREATE_ACCEXEC',
  UPDATE_ACCEXEC: 'accExec/UPDATE_ACCEXEC',
  DELETE_ACCEXEC: 'accExec/DELETE_ACCEXEC',
  RESET: 'accExec/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccExec>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AccExecState = Readonly<typeof initialState>;

// Reducer

export default (state: AccExecState = initialState, action): AccExecState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACCEXECS):
    case REQUEST(ACTION_TYPES.FETCH_ACCEXEC_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCEXEC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCEXEC):
    case REQUEST(ACTION_TYPES.UPDATE_ACCEXEC):
    case REQUEST(ACTION_TYPES.DELETE_ACCEXEC):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACCEXECS):
    case FAILURE(ACTION_TYPES.FETCH_ACCEXEC_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCEXEC):
    case FAILURE(ACTION_TYPES.CREATE_ACCEXEC):
    case FAILURE(ACTION_TYPES.UPDATE_ACCEXEC):
    case FAILURE(ACTION_TYPES.DELETE_ACCEXEC):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACCEXECS):
    case SUCCESS(ACTION_TYPES.FETCH_ACCEXEC_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCEXEC):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCEXEC):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCEXEC):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCEXEC):
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

const apiUrl = 'api/acc-execs';
const apiSearchUrl = 'api/_search/acc-execs';

// Actions

export const getSearchEntities: ICrudSearchAction<IAccExec> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACCEXECS,
  payload: axios.get<IAccExec>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAccExec> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCEXEC_LIST,
    payload: axios.get<IAccExec>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAccExec> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCEXEC,
    payload: axios.get<IAccExec>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAccExec> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCEXEC,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccExec> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCEXEC,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccExec> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCEXEC,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccountExecutive, defaultValue } from 'app/shared/model/account-executive.model';

export const ACTION_TYPES = {
  SEARCH_ACCOUNTEXECUTIVES: 'accountExecutive/SEARCH_ACCOUNTEXECUTIVES',
  FETCH_ACCOUNTEXECUTIVE_LIST: 'accountExecutive/FETCH_ACCOUNTEXECUTIVE_LIST',
  FETCH_ACCOUNTEXECUTIVE: 'accountExecutive/FETCH_ACCOUNTEXECUTIVE',
  CREATE_ACCOUNTEXECUTIVE: 'accountExecutive/CREATE_ACCOUNTEXECUTIVE',
  UPDATE_ACCOUNTEXECUTIVE: 'accountExecutive/UPDATE_ACCOUNTEXECUTIVE',
  DELETE_ACCOUNTEXECUTIVE: 'accountExecutive/DELETE_ACCOUNTEXECUTIVE',
  RESET: 'accountExecutive/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccountExecutive>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type AccountExecutiveState = Readonly<typeof initialState>;

// Reducer

export default (state: AccountExecutiveState = initialState, action): AccountExecutiveState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ACCOUNTEXECUTIVES):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCOUNTEXECUTIVE):
    case REQUEST(ACTION_TYPES.UPDATE_ACCOUNTEXECUTIVE):
    case REQUEST(ACTION_TYPES.DELETE_ACCOUNTEXECUTIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ACCOUNTEXECUTIVES):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE):
    case FAILURE(ACTION_TYPES.CREATE_ACCOUNTEXECUTIVE):
    case FAILURE(ACTION_TYPES.UPDATE_ACCOUNTEXECUTIVE):
    case FAILURE(ACTION_TYPES.DELETE_ACCOUNTEXECUTIVE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ACCOUNTEXECUTIVES):
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCOUNTEXECUTIVE):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCOUNTEXECUTIVE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCOUNTEXECUTIVE):
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

const apiUrl = 'api/account-executives';
const apiSearchUrl = 'api/_search/account-executives';

// Actions

export const getSearchEntities: ICrudSearchAction<IAccountExecutive> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ACCOUNTEXECUTIVES,
  payload: axios.get<IAccountExecutive>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`),
});

export const getEntities: ICrudGetAllAction<IAccountExecutive> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE_LIST,
    payload: axios.get<IAccountExecutive>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IAccountExecutive> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCOUNTEXECUTIVE,
    payload: axios.get<IAccountExecutive>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IAccountExecutive> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCOUNTEXECUTIVE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccountExecutive> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCOUNTEXECUTIVE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccountExecutive> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCOUNTEXECUTIVE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});

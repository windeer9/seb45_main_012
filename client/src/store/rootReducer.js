import { combineReducers } from '@reduxjs/toolkit';
import authReducer from './authSlice.js';
import menuReducer from './menuSlice.ts';

const rootReducer = combineReducers({
    auth: authReducer,
    menu: menuReducer,
});

export default rootReducer;
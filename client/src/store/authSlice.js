import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    isLoggedIn: false,
    accessToken: null,
  },
  reducers: {
    setLoggedIn: (state, action) => {
      state.isLoggedIn = action.payload;
    },
    setAccessToken: (state, action) => {
      state.accessToken = action.payload;
    },
    logout: (state) => {
      state.isLoggedIn = false;
      state.accessToken = null;
    },
  },
});

export const { setLoggedIn, setAccessToken, logout } = authSlice.actions;
export default authSlice.reducer;
import { createSlice } from '@reduxjs/toolkit';

// 초기상태 비워뒀습니다
const initialState = {
//   activeMenu: '전체글보기',
};

//슬라이스 생성
const menuSlice = createSlice({
  name: 'menu',
  initialState,
  //리듀서 함수 정의
  reducers: {
    setActiveMenu: (state, action) => {
      state.activeMenu = action.payload;
    },
  },
});

// 액션 생성자를 내보냅니다
export const { setActiveMenu } = menuSlice.actions;
// 선택자 함수를 내보냅니다 (현재 활성 메뉴를 선택하는 역할)
export const selectActiveMenu = (state) => state.menu.activeMenu;
// 리듀서 함수를 내보냅니다 (상태관리 수행하는 역할)
export default menuSlice.reducer;
import axios from 'axios';
import { instance } from 'api/api.js';

const LoginFunc = async ( id, password ) => {

  try {
    if ( !id && !password ) {
      return 'ID를 입력하세요.';
    } else if (!password) {
      return '비밀번호를 입력하세요.';
    }
    const data = {
      userUseId: id,
      password: password
    }

    const res = await instance.post('auth/login', data, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    const auth = res.headers['authorization'];
    const accessToken = auth.substring(6);
    localStorage.setItem('accessToken', accessToken);
    axios.defaults.headers.common['Authorization'] = `Bearer${accessToken}`;
    return true;

    } catch (error) {
      if ( error.response.status === 401) {
        return 'ID 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요.';
      }
      return '오류가 발생했습니다.';
    } 
  }

export default LoginFunc;
import axios from 'axios';
import { instance } from 'api/api.js';

const LoginFunc = async ( id, password ) => {

  try {
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
    console.log(accessToken[0], '로그인 성공')
    localStorage.setItem('accessToken', accessToken);
    axios.defaults.headers.common['Authorization'] = `Bearer${accessToken}`;
    return true;
    }
  catch (err) {
    console.log('err message: ', err);
    return false;
  }

}

export default LoginFunc;
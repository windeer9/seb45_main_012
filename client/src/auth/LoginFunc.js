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
    
    localStorage.setItem('accessToken', accessToken);
    axios.defaults.headers.common['Authorization'] = `Bearer${accessToken}`;
    }

  catch (err) {
    console.log('err message: ', err)
  }
}

export default LoginFunc;
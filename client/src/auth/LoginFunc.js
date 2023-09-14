import axios from 'axios';
import Cookies from 'js-cookie';

const LoginFunc = async ( id, password ) => {
  
  try {
    const data = {
      userUseId: id,
      password: password
    }
    const res = await axios.post('http://52.78.145.37:8080/auth/login', data, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    const auth = res.headers['authorization'];
    const accessToken = auth.substring(6);
    const refreshValue = res.headers['refresh'];
    Cookies.set('refreshToken', refreshValue, { httpOnly: true, expires: 7 });
    localStorage.setItem('accessToken', accessToken);
    axios.defaults.headers.common['Authorization'] = `Bearer${accessToken}`;
    }

  catch (err) {
    console.log('err message: ', err)
  }
}

export default LoginFunc;
import axios from 'axios';

const service = axios.create({
    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    // easy-mock服务挂了，暂时不使用了
    /*baseURL: 'http://localhost:8081',*/
    timeout: 5000,
 /*   headers: {'Content-Type':'application/json'},*/
});
axios.defaults.withCredentials=true;
axios.defaults.crossDomain=true;
axios.defaults.headers.post['Content-Type'] = 'apmpplication/x-www-form-urlencoded';
axios.defaults.headers.delete['Content-Type'] = 'application/json';

service.interceptors.request.use(
    config => {
    /*    config.headers['Content-Type'] = 'application/x-www-form-urlencoded'*/
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

service.interceptors.response.use(
    response => {
        if (response.status === 200) {
            return response.data;
        } else {
            Promise.reject();
        }
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

export default service;

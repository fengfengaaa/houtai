import request from '../utils/request';

export const fetchSysuserData = query => {
    return request({
        url: '/api/sysuser/all',
        method: 'get',
        params: query,
    });
};



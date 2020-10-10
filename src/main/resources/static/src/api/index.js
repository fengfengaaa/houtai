import request from '../utils/request';

export const fetchData = query => {
    return request({
        url: '/api/user/all',
        method: 'get',
        params: query
    });
};



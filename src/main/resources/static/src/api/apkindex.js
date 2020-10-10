import request from '../utils/request';

export const fetchPatientData = query => {
    return request({
        url: '/api/apk/current',
        method: 'get',
        params: query,
    });
};



import request from '../utils/request';

export const fetchPatientData = query => {
    return request({
        url: '/api/patient/all',
        method: 'get',
        params: query,
    });
};



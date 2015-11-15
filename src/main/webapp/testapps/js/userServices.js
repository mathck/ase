var userServices = angular.module('taskit.userServices', ['ngResource']);

userServices.factory('UsersFactory', function ($resource) {
    return $resource('/taskit/api/user/', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
    })
});

userServices.factory('UserFactory', function ($resource) {
    return $resource('/taskit/api/user/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {id: '@id'} },
        delete: { method: 'DELETE', params: {id: '@id'} }
    })
});

userServices.factory('LoginFactory', function ($resource) {
    return $resource('/taskit/api/user/login', {}, {
        create: { method: 'POST' }
    })
});
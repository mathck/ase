var services = angular.module('taskit.userServices', ['ngResource']);

services.factory('UsersFactory', function ($resource) {
    return $resource('/taskit/api/user/', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
    })
});

services.factory('UserFactory', function ($resource) {
    return $resource('/taskit/api/user/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {id: '@id'} },
        delete: { method: 'DELETE', params: {id: '@id'} }
    })
});

services.factory('LoginFactory', function ($resource) {
    return $resource('/taskit/api/user/test/login', {}, {
        create: { method: 'POST' }
    })
});
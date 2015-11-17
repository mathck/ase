var taskServices = angular.module('taskit.projectServices', ['ngResource']);

taskServices.factory('ProjectsFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST'}
    })
});

taskServices.factory('ProjectFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {pid: '@pid'} },
        delete: { method: 'DELETE', params: {pid: '@pid'} }
    })
});
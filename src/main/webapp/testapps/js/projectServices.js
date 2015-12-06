var projectServices = angular.module('taskit.projectServices', ['ngResource']);

projectServices.factory('ProjectsFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/all', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST'}
    })
});

projectServices.factory('ProjectFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {pid: '@pid'} },
        delete: { method: 'DELETE', params: {pid: '@pid'} }
    })
});
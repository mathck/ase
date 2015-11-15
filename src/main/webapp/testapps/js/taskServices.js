var taskServices = angular.module('taskit.taskServices', ['ngResource']);

taskServices.factory('TasksFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects', {}, {
        query: { method: 'GET', isArray: true },
        create: { method: 'POST' }
    })
});

taskServices.factory('TaskFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {pid: '@pid', id: '@id'} },
        delete: { method: 'DELETE', params: {pid: '@pid', id: '@id'} }
    })
});
var taskServices = angular.module('taskit.taskServices', ['ngResource']);

taskServices.factory('TasksFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/tasks', {}, {
        query: {
            method: 'GET',
            params: {pid: '@pid'},
            isArray: true
        },
        create: {
            method: 'POST',
            params: {pid: '@pid'},
            isArray: true
        }
    })
});

taskServices.factory('TaskFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/tasks/:tID', {}, {
        show: {
            method: 'GET',
            params: {tID: '@tID'}
        },
        update: {
            method: 'PUT',
            params: {pid: '@pid', id: '@id'}
        },
        delete: {
            method: 'DELETE',
            params: {pid: '@pid', id: '@id'}
        }
    })
});
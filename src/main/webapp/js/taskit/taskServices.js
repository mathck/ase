var taskServices = angular.module('taskit.taskServices', ['ngResource']);

taskServices.factory('TasksFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid', {}, {
        query: {
            method: 'GET',
            params: {pid: '@pid'},
            isArray: true,
            headers: { 'auth-token': '@token' }
        },
        create: {
            method: 'POST',
            params: {pid: '@pid'},
            headers: { 'auth-token': '@token' }
        }
    })
});

taskServices.factory('TaskFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/:id', {}, {
        show: {
            method: 'GET',
            headers: { 'auth-token': '@token' }
        },
        update: {
            method: 'PUT',
            params: {pid: '@pid', id: '@id'},
            headers: { 'auth-token': '@token' }
        },
        delete: {
            method: 'DELETE',
            params: {pid: '@pid', id: '@id'},
            headers: { 'auth-token': '@token' }
        }
    })
});
var projectServices = angular.module('taskit.projectServices', ['ngResource']);

projectServices.factory('ProjectsFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/all', {}, {
        query: {
            method: 'GET',
            isArray: true
            //headers: { 'auth-token': '@token' }
        },
        create: {
            method: 'POST'
            //headers: { 'auth-token': '@token' }
        }
    })
});

projectServices.factory('ProjectFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid', {}, {
        show: {
            method: 'GET',
            params: {pID: '@pid'}
            //headers: { 'auth-token': '@token' }
        },
        update: {
            method: 'PUT',
            params: {pid: '@pid'}
            //headers: { 'auth-token': '@token' }
        },
        delete: {
            method: 'DELETE',
            params: {pid: '@pid'}
            //headers: { 'auth-token': '@token' }
        }
    })
});
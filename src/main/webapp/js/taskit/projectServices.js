var projectServices = angular.module('taskit.projectServices', ['ngResource']);

projectServices.factory('ProjectsFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/user', {}, {
        query: {
            method: 'GET',
            params: {uID: '@uID'},
            isArray: true
        },
        create: {
            method: 'POST'
        }
    })
});

projectServices.factory('AdminProjectsFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/all', {}, {
        query: {
            method: 'GET',
            isArray: true
        },
        create: {
            method: 'POST'
        }
    })
});

projectServices.factory('ProjectFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects', {}, {
        show: {
            method: 'GET',
            params: {pID: '@pID'}
        },
        update: {
            method: 'PATCH',
            params: {pID: '@pID'}
        },
        delete: {
            method: 'DELETE',
            params: {pID: '@pID'}
        }
    })
});
var issueServices = angular.module('taskit.issueServices', ['ngResource']);

issueServices.factory('IssuesFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/issues', {}, {
        query: {
            method: 'GET',
            params: {pid: '@pid'},
            isArray: true
        }
    })
});

issueServices.factory('IssueFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pID/issues', {}, {
        show: {
            method: 'GET'
        },
        update: {
            method: 'PUT',
            params: {pid: '@pID', id: '@id'}
        },
        delete: {
            method: 'DELETE',
            params: {pID: '@pID', uID: '@id'}
        },
        create: {
            method: 'POST',
            params: {pID: '@pID', uID: '@uID'}
        }
    })
});
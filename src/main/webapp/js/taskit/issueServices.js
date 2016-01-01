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

issueServices.factory('IssuesMessageBoxFactory', function ($resource) {
    return $resource('/taskit/api/workspace/users/issues', {}, {
        query: {
            method: 'GET',
            params: {uid: '@uid'},
            isArray: true
        }
    })
});

issueServices.factory('IssuePostFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pID/issues', {}, {
        create: {
            method: 'POST',
            params: {pID: '@pID', uID: '@uID'}
        }
    })
});

issueServices.factory('IssueRetrieveFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/issues/:issueID', {}, {
        show: {
            method: 'GET',
            params: {issueID: '@iID'}
        },
        delete: {
            method: 'DELETE',
            params: {issueID: '@iID'}
        }
    })
});
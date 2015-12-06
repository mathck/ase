var issueServices = angular.module('taskit.issueServices', ['ngResource']);

issueServices.factory('IssuesFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/issues', {}, {
        query: {
            method: 'GET',
            params: {pid: '@pid'},
            isArray: true
            //headers: { 'auth-token': '@token' }
        },
        create: {
            method: 'POST',
            params: {pid: '@pid'}
            //headers: { 'auth-token': '@token' }
        }
    })
});

issueServices.factory('IssueFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/issues/:id', {}, {
        show: {
            method: 'GET'
            //headers: { 'auth-token': '@token' }
        },
        update: {
            method: 'PUT',
            params: {pid: '@pid', id: '@id'}
            //headers: { 'auth-token': '@token' }
        },
        delete: {
            method: 'DELETE',
            params: {pid: '@pid', id: '@id'}
            //headers: { 'auth-token': '@token' }
        }
    })
});
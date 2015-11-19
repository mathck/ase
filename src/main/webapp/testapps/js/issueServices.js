var issueServices = angular.module('taskit.issueServices', ['ngResource']);

issueServices.factory('IssuesFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/issues', {}, {
        query: { method: 'GET', params: {pid: '@pid'}, isArray: true },
        create: { method: 'POST', params: {pid: '@pid'}}
    })
});

issueServices.factory('IssueFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pid/issues/:id', {}, {
        show: { method: 'GET' },
        update: { method: 'PUT', params: {pid: '@pid', id: '@id'} },
        delete: { method: 'DELETE', params: {pid: '@pid', id: '@id'} }
    })
});
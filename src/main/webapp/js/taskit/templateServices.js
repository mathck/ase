var templateServices = angular.module('taskit.templateServices', ['ngResource']);

templateServices.factory('TemplateFactory', function ($resource) {
    return $resource('/taskit/api/workspace/templates/:tID', {}, {
        show: {
            method: 'GET',
            params: {tID: '@tID'},
        },
        query: {
            method: 'GET',
            isArray: true
        },
        create: {
            method: 'POST',
            params: {tID: '@tID'}
        },
        update: {
            method: 'PUT',
            params: {tID: '@tID'}
        }
    })
});

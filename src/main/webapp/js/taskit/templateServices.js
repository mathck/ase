var templateServices = angular.module('taskit.templateServices', ['ngResource']);

templateServices.factory('TemplateFactory', function ($resource) {
    return $resource('/taskit/api/workspace/templates/:templateId', {}, {
        show: {
            method: 'GET',
            params: {templateId: '@templateId'}
        },
        query: {
            method: 'GET',
            isArray: true
        },
        create: {
            method: 'POST',
            params: {templateId: '@templateId'}
        },
        update: {
            method: 'PUT',
            params: {templateId: '@templateId'}
        }
    })
});

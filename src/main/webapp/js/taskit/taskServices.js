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
            params: {pID: '@pID', tID: '@tID'}
        },
        delete: {
            method: 'DELETE',
            params: {pID: '@pID', tID: '@ID'}
        }
    })
});

taskServices.factory('TaskUserFactory', function ($resource) {
    return $resource('/taskit/api/workspace/users/:uID/tasks/:tID', {}, {
        add: {
            method: 'POST',
            params: {uID: '@uID', tID: '@tID'}
        },
        delete: {
            method: 'DELETE',
            params: {uID: '@uID', tID: '@tID'}
        }
    })
});


taskServices.factory('TaskCommentFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/tasks/:tID/comments/:cID', {}, {
        add: {
            method: 'POST',
            params: {tID: '@tID'}
        },
        delete: {
            method: 'DELETE',
            params: {cID:'@cID', tID: '@tID'}
        }
    })
});
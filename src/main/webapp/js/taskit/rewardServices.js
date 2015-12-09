var rewardServices = angular.module('taskit.rewardServices', ['ngResource']);

rewardServices.factory('RewardsByProjectFactory', function ($resource) {
    return $resource('/taskit/api/workspace/projects/:pID/users/:uID/rewards', {}, {
        query: {
            method: 'GET',
            params: {pID: '@pID', uID: '@uID'},
            isArray: true
        },
        create: {
            method: 'POST',
            params: {pID: '@pID', uID: '@uID', rID: '@rID'}
        }
    })
});

rewardServices.factory('RewardsFactory', function ($resource) {
    return $resource('/taskit/workspace/rewards', {}, {
        create: {
            method: 'POST'
        }
    })
});

rewardServices.factory('RewardFactory', function ($resource) {
    return $resource('/taskit/workspace/rewards/:rID', {}, {
        show: {
            method: 'GET',
            params: {pID: '@rID'}
        }
    })
});
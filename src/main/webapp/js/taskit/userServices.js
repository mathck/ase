var userServices = angular.module('taskit.userServices', ['ngResource']);

userServices.factory('UsersFactory', function ($resource) {
    return $resource('/taskit/api/user/', {}, {
        query: {
            method: 'GET',
            isArray: true
            //headers: { 'auth-token': '@token' }
        },
        create: {
            method: 'POST'
        }
    })
});

userServices.factory('UserFactory', function ($resource) {
    return $resource('/taskit/api/user/:id', {}, {
        show: {
            method: 'GET'
            //headers: { 'auth-token': '@token' }
        },
        update: {
            method: 'PUT',
            params: {email: '@email'}
            //headers: { 'auth-token': '@token' }
        },
        delete: {
            method: 'DELETE',
            params: {email: '@email'}
            //headers: { 'auth-token': '@token' }
        }
    })
});

userServices.factory('LoginFactory', function ($resource) {
    return $resource('/taskit/api/user/login', {}, {
        create: {
            method: 'GET',
            params: {email: '@email', password: '@password'}
        },
        logout: {
            method: 'PUT',
            params: {email: '@email'}
        }
    })
});

userServices.factory('TokenService', [function() {
  var token = {
    isLogged: false,
    token: '',
    username: ''
  };
  return token;
}]);
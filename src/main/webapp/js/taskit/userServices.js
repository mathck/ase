var userServices = angular.module('taskit.userServices', ['ngResource']);

userServices.factory('UsersFactory', function ($resource) {
    return $resource('/taskit/api/user/all', {}, {
        query: {
            method: 'GET',
            isArray: true
        }
    })
});

userServices.factory('UserRegistrationFactory', function ($resource) {
    return $resource('/taskit/api/user/register', {}, {
        create: {
            method: 'POST'
        }
    })
});

userServices.factory('UserFactory', function ($resource) {
    return $resource('/taskit/api/user/', {}, {
        show: {
            method: 'GET',
            params: {uID: '@uID'}
        },
        update: {
            method: 'PUT',
            params: {uID: '@uID'}
        },
        delete: {
            method: 'DELETE',
            params: {uID: '@uID'}
        }
    })
});

userServices.factory('LoginFactory', function ($resource) {
    return $resource('/taskit/api/user/login', {}, {
        receive: {
            method: 'GET',
            params: {email: '@email', password: '@password'}
        }
    })
});


userServices.factory('LogoutFactory', function ($resource) {
    return $resource('/taskit/api/user/logout', {}, {
        logout: {
            method: 'PATCH',
            params: {email: '@email'}
        }
    })
});

userServices.factory('TokenService', [function() {
  var token = {
    isLogged: false,
    token: '',
    username: '',
    user:{}
  };
  return token;
}]);
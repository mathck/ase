angular.module('taskit', ['ngRoute', 'taskit.filters', 'taskit.userServices', 'taskit.directives', 'taskit.controllers']).
    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/user-login', {templateUrl: 'user/user-login.html', controller: 'restUserListCtrl'});
        $routeProvider.when('/user-list', {templateUrl: 'user/user-list.html', controller: 'restUserListCtrl'});
        $routeProvider.when('/user-update/:id', {templateUrl: 'user/user-update.html', controller: 'restUserUpdateCtrl'});
        $routeProvider.when('/user-register', {templateUrl: 'user/user-register.html', controller: 'restUserCreationCtrl'});
        $routeProvider.otherwise({redirectTo: '/user-login'});
    }]);

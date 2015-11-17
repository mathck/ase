angular.module('taskit', ['ngRoute', 'taskit.filters', 'taskit.directives',
    'taskit.userServices', 'taskit.userControllers',
    'taskit.taskServices', 'taskit.taskControllers',
    'taskit.projectServices', 'taskit.projectControllers']).

    config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/user-login', {templateUrl: 'user/user-login.html', controller: 'restLoginCtrl'});

        $routeProvider.when('/user-list', {templateUrl: 'user/user-list.html', controller: 'restUserListCtrl'});
        $routeProvider.when('/user-update/:id', {templateUrl: 'user/user-update.html', controller: 'restUserUpdateCtrl'});
        $routeProvider.when('/user-register', {templateUrl: 'user/user-register.html', controller: 'restUserRegistrationCtrl'});

        $routeProvider.when('/project-list', {templateUrl: 'project/project-list.html', controller: 'restProjectListCtrl'});
        $routeProvider.when('/project-create', {templateUrl: 'project/project-create.html', controller: 'restProjectCreateCtrl'});
        $routeProvider.when('/project-update/:id', {templateUrl: 'project/project-update.html', controller: 'restProjectUpdateCtrl'});

        $routeProvider.when('/task-list', {templateUrl: 'task/task-list.html', controller: 'restTaskListCtrl'});
        $routeProvider.when('/task-create', {templateUrl: 'task/task-create.html', controller: 'restTaskCreateCtrl'});
        $routeProvider.when('/task-update/:id', {templateUrl: 'task/task-update.html', controller: 'restTaskUpdateCtrl'});

        $routeProvider.otherwise({redirectTo: 'user-login'});
    }]);

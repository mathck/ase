var app = angular.module('taskit.userControllers', []);

app.controller('restUserListCtrl', ['$scope', 'UsersFactory', 'UserFactory', '$location',
    function ($scope, UsersFactory, UserFactory, $location) {

        // callback for ng-click 'editUser':
        $scope.updateUser = function (userId) {
            $location.path('/user-update/' + userId);
        };

        // callback for ng-click 'deleteUser':
        $scope.deleteUser = function (userId) {
            UserFactory.delete({ id: userId });
            $scope.users = UsersFactory.query();
        };

        // callback for ng-click 'createUser':
        $scope.saveUser = function () {
            $location.path('/user-register');
        };

        $scope.users = UsersFactory.query();
    }]);

app.controller('restUserUpdateCtrl', ['$scope', '$routeParams', 'UserFactory', '$location',
    function ($scope, $routeParams, UserFactory, $location) {

        // callback for ng-click 'updateUser':
        $scope.updateUser = function () {
            UserFactory.update($scope.user);
            $location.path('/user-list');
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/user-list');
        };

        $scope.user = UserFactory.show({id: $routeParams.id});
    }]);

app.controller('restUserRegistrationCtrl', ['$scope', 'UsersFactory', '$location',
    function ($scope, UsersFactory, $location) {

        // callback for ng-click 'saveUser':
        $scope.saveUser = function () {
            console.log($scope.user);
            UsersFactory.create($scope.user);
            console.log($scope.user);
            $location.path('/user-list');
        }
    }]);

app.controller('restLoginCtrl', ['$scope', 'LoginFactory', '$location',
    function ($scope, LoginFactory, $location) {

        // callback for ng-click 'loginUser':
        $scope.loginUser = function () {
            LoginFactory.create($scope.login);
            $location.path('/user-list');
        }
    }]);

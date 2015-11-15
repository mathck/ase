var app = angular.module('taskit.taskControllers', []);

app.controller('restTaskListCtrl', ['$scope', 'TasksFactory', 'TaskFactory', '$location',
    function ($scope, TasksFactory, TaskFactory, $location) {

        // callback for ng-click 'editTask':
        $scope.updateTask = function (taskId) {
            $location.path('/task-update/' + taskId);
        };

        // callback for ng-click 'deleteTask':
        $scope.deleteTask = function (taskId) {
            TaskFactory.delete({ id: taskId });
            $scope.tasks = TasksFactory.query();
        };

        // callback for ng-click 'createTask':
        $scope.createTask = function () {
            $location.path('/task-list');
        };

        $scope.tasks = TasksFactory.query();
    }]);

app.controller('restTaskUpdateCtrl', ['$scope', '$routeParams', 'TaskFactory', '$location',
    function ($scope, $routeParams, TaskFactory, $location) {

        // callback for ng-click 'showTask':
        $scope.showTask = function (taskid) {
            $location.path('#/task-update/'+taskid);
        };

        // callback for ng-click 'updateTask':
        $scope.updateTask = function () {
            TaskFactory.update($scope.task);
            $location.path('/task-list');
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/task-list');
        };

        $scope.task = TaskFactory.show({id: $routeParams.id});
    }]);

app.controller('restTaskCreateCtrl', ['$scope', 'TasksFactory', '$location',
    function ($scope, TasksFactory, $location) {

        // callback for ng-click 'saveTask':
        $scope.createTask = function () {
            console.log($scope.task);
            TasksFactory.create($scope.task);
            //console.log($scope.task);
            $location.path('/task-list');
        }
    }]);
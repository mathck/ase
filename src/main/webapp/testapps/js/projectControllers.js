var app = angular.module('taskit.projectControllers', []);

app.controller('restProjectListCtrl', ['$scope', 'ProjectsFactory', 'ProjectFactory', '$location',
    function ($scope, ProjectsFactory, ProjectFactory, $location) {

        // callback for ng-click 'editProject':
        $scope.updateProject = function (ProjectId) {
            $location.path('/project-update/' + projectId);
        };

        // callback for ng-click 'deleteProject':
        $scope.deleteProject = function (projectId) {
            ProjectFactory.delete({ id: projectId });
            $scope.projects = ProjectsFactory.query();
        };

        // callback for ng-click 'createProject':
        $scope.createProject = function () {
            $location.path('/project-list');
        };

        $scope.projects = ProjectsFactory.query();
    }]);

app.controller('restProjectUpdateCtrl', ['$scope', '$routeParams', 'ProjectFactory', '$location',
    function ($scope, $routeParams, ProjectFactory, $location) {

        // callback for ng-click 'showProject':
        $scope.showProject = function (projectid) {
            $location.path('#/project-update/'+projectid);
        };

        // callback for ng-click 'updateProject':
        $scope.updateProject = function () {
            ProjectFactory.update($scope.project);
            $location.path('/project-list');
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/project-list');
        };

        $scope.project = ProjectFactory.show({id: $routeParams.id});
    }]);

app.controller('restProjectCreateCtrl', ['$scope', 'ProjectsFactory', '$location',
    function ($scope, ProjectsFactory, $location) {

        // callback for ng-click 'saveProject':
        $scope.createProject = function () {
            console.log($scope.project);
            ProjectsFactory.create($scope.project);
            //console.log($scope.project);
            $location.path('/project-list');
        }
    }]);
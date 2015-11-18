var projectController = angular.module('taskit.projectControllers', []);

projectController.controller('restProjectListCtrl', ['$scope', 'ProjectsFactory', 'ProjectFactory', 'UserFactory', '$location',
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
            var loggedUser=UserFactory.show('test');
            var newUserList=[];
            var newProject;
            newProject[id]=$scope.project.id;
            newProject[title]=$scope.project.title;
            newProject[description]=$scope.project.description;
            newProject[userList]=[];
            //newProject.userList.push(loggedUser);
            newProject[taskList]=[];
            newProject[issueList]=[];

            console.log("User: " + loggedUser);
            console.log("Project: " + newProject);
            console.log("still here");
            ProjectFactory.create(newProject);
            //$location.path('/project-list');
            console.log("and here");
        };

        $scope.projects = ProjectsFactory.query();
    }]);

projectController.controller('restProjectUpdateCtrl', ['$scope', '$routeParams', 'ProjectFactory', '$location',
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

projectController.controller('restProjectCreateCtrl', ['$scope', 'ProjectsFactory', '$location',
    function ($scope, ProjectsFactory, $location) {

        // callback for ng-click 'saveProject':
        $scope.createProject = function () {
            console.log($scope.project);
            ProjectsFactory.create($scope.project);
            //console.log($scope.project);
            $location.path('/project-list');
        }
    }]);
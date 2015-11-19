var issueController = angular.module('taskit.issueControllers', []);

issueController.controller('restIssueListCtrl', ['$scope', 'IssuesFactory', 'IssueFactory', '$location',
    function ($scope, IssuesFactory, IssueFactory, $location) {

        // callback for ng-click 'editIssue':
        $scope.updateIssue = function (issueId) {
            $location.path('/issue-update/' + issueId);
        };

        // callback for ng-click 'deleteIssue':
        $scope.deleteIssue = function (issueId) {
            IssueFactory.delete({ id: issueId });
            $scope.issues = IssuesFactory.query();
        };

        // callback for ng-click 'createIssue':
        $scope.createIssue = function () {
            $location.path('/issue-list');
        };

        $scope.issues = IssuesFactory.query();
    }]);

issueController.controller('restIssueUpdateCtrl', ['$scope', '$routeParams', 'IssueFactory', '$location',
    function ($scope, $routeParams, IssueFactory, $location) {

        // callback for ng-click 'showIssue':
        $scope.showIssue = function (issueid) {
            $location.path('#/issue-update/'+issueid);
        };

        // callback for ng-click 'updateIssue':
        $scope.updateIssue = function () {
            IssueFactory.update($scope.issue);
            $location.path('/issue-list');
        };

        // callback for ng-click 'cancel':
        $scope.cancel = function () {
            $location.path('/issue-list');
        };

        $scope.issue = IssueFactory.show({id: $routeParams.id});
    }]);

issueController.controller('restIssueCreateCtrl', ['$scope', 'IssuesFactory', '$location',
    function ($scope, IssuesFactory, $location) {

        // callback for ng-click 'saveIssue':
        $scope.createIssue = function () {
            console.log("Issue:" + $scope.issue);
            IssuesFactory.create($scope.issue);
            //console.log($scope.issue);
            $location.path('/issue-list');
        }
    }]);
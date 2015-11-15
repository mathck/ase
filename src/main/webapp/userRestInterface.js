

    angular.module('taskit', ['taskit.LoginService', 'taskit.LoginService', 'taskit.restLoginApp', 'ngResource']);

    angular.module('taskit.LoginService',['ngResource']);

    angular.module('taskit.LoginService').factory('Login', function($resource){
        return $resource('/api/user/login/:email');
    })

    var loginModule=angular.module('taskit.restLoginApp',['taskit.LoginService']);

    loginModule.controller('taskit.restLoginCtrl'),function($scope, Login) {
        $scope.tester="Binding Works";

        $scope.restLogin=new Login();

        $scope.restLogin.data={mail:$scope.login.email, password:$scope.login.password};
        Login.save($scope.restLogin, function(){
            //TODO enter callback reaction
        });
    };
/*
    angular.module('taskit.UserService').factory('User', function($resource){
        return $resource('/rest/user/:mail', (mail: '@_maiil'),{
            update: {
                method: 'PUT'
            }
        });
    });

    var registerModule=angular.module('restRegisterApp',['taskit.UserService']);

    registerModule.controller('restRegisterCtrl'),function(%scope, Login) {

        //get one User
        function getUser(email){
            //$scope.user=new User();
            var user = Entry.get({mail:$scope.email}, function(){
                console.log.entry
            })
            return user;
        }

        //query all users
        function getUsers(){
            var users = User.query(function(){
                console.log(entries);
            });
        }

        //save a new user
        function saveUser(email){
            $scope.user=new User();
            $scope.user.data="{mail:"+$scope.email+";" +
                "firstName:"+ $scope.firstName + ";" +
                "lastName:" + $scope.lastName + ";" +
                "projects:[];" +
                "password:" + $scope.passwordRegistration1;

            Entry.save($scope.entry, function(){
             //TODO enter callback reaction
            });
        }

        //update a new user
        function updateUser(email){
            $scope.user=new User();
            if ($scope.mail.length===0){
                $scope.user.data+="{mail:"+$scope.email+";";
            }
            if ($scope.firstName.length===0) {
                "firstName:"+= $scope.firstName + ";";
            }
            if ($scope.lastName.length===0{
                "lastName:"+= $scope.lastName + ";";
            }
            $scope.user.$update(function(){
             //TODO enter callback reaction
            });
        }


        function deleteUser(email){
            User userToDelete = User.get(mail:$scope.email);
            scope.userToDelete.$delete(function{
                //TODO enter callback function
            });
        }

    };


    //=====================================================================================

    var services = angular.module('taskit.services', ['ngResource']);

    services.factory('LoginFactory', function ($resource) {
        return $resource('/api/user/login', {}, {
            create: { method: 'POST' }
        })
    });

    //User API functionality
    services.factory('UsersFactory', function ($resource) {
        return $resource('/api/user', {}, {
            query: { method: 'GET', isArray: true },
            create: { method: 'POST' }
        })
    });

    services.factory('UserFactory', function ($resource) {
        return $resource('/api/user/:id', {}, {
            show: { method: 'GET' },
            update: { method: 'PUT', params: {id: '@id'} },
            delete: { method: 'DELETE', params: {id: '@id'} }
        })
    });

    app.controller('restLoginCtrl', ['$scope', 'LoginFactory', '$location',
    function ($scope, UsersFactory, $location) {

        // callback for ng-click 'loginUser':
        $scope.loginUser = function () {
            LoginFactory.create($scope.login);
            //$location.path('/user-list');
        }
    }]);


    app.controller('UserDetailCtrl', ['$scope', '$routeParams', 'UserFactory', '$location',
        function ($scope, $routeParams, UserFactory, $location) {

            /* callback for ng-click 'updateUser':
            $scope.updateUser = function () {
                UserFactory.update($scope.user);
                $location.path('/user-list');
            };

            $scope.user = UserFactory.show({id: $routeParams.id});
        }]);

    app.controller('UserCreationCtrl', ['$scope', 'UsersFactory', '$location',
        function ($scope, UsersFactory, $location) {

            // callback for ng-click 'createNewUser':
            $scope.createNewUser = function () {
                UsersFactory.create($scope.user);
                $location.path('/user-list');
            }
        }]);
*/
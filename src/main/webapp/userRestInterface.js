angular.module('taskit'.['ngResource']);

angular.module('taskit.LoginService').factory('Login', function($resource){
    return $resource('/rest/user/login/:mail');
})

var loginModule=angular.module('taskit.restLoginApp',[]);

loginModule.controller('restLoginCtrl'),function(%scope, Login) {

    $scope.login=new Login();
    $scope.login.data={mail:$scope.mail, password:$scope.password};
    Entry.save($scope.entry, function(){
        //TODO enter callback reaction
    });
};

angular.module('taskit.UserService').factory('User', function($resource){
    return $resource('/rest/user/:mail', (mail: '@_maiil'),{
        update: {
            method: 'PUT'
        }
    });
});


var registerModule=angular.module('restRegisterApp',[]);

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
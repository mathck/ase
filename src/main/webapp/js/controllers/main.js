materialAdmin
    // =========================================================================
    // Base controller for common functions
    // =========================================================================

    .controller('materialadminCtrl', function($timeout, $state, $window, $http, $cookies, $scope, growlService, LogoutFactory, UserFactory, TokenService){
        //Welcome Message

        TokenService.username = $cookies.email;
        TokenService.token = ($cookies.token).valueOf();
        $http.defaults.headers.common['user-token'] = String(TokenService.token);
        $http.defaults.headers.common['userID'] = String($cookies.email);


        if ( (TokenService.token == '') || (typeof TokenService.token === 'undefined') ){
            $window.location.href='/taskit/index.html';
        }
        TokenService.isLogged=true;
        UserFactory.show({uID: TokenService.username}).$promise.then(function(response){
            TokenService.user=response; //set persistent UserInformation
            growlService.growl('Welcome back ' + TokenService.user.firstName +' :D', 'inverse');
            //initialize Variables for Menubar
            $scope.loggedUser={};
            $scope.loggedUser.avatar=TokenService.user.avatar;
            $scope.loggedUser.firstName=TokenService.user.firstName;
            $scope.loggedUser.lastName=TokenService.user.lastName;
            $scope.loggedUser.userID=TokenService.user.userID;
            $scope.loggedUser.userLevel=TokenService.user.level.currentLevel;
            console.log($scope.loggedUser.userLevel);
        });

        // Detact Mobile Browser
        if( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
           angular.element('html').addClass('ismobile');
        }

        // By default Sidbars are hidden in boxed layout and in wide layout only the right sidebar is hidden.
        this.sidebarToggle = {
            left: false,
            right: false
        }

        // By default template has a boxed layout
        this.layoutType = localStorage.getItem('ma-layout-status');
        
        // For Mainmenu Active Class
        this.$state = $state;    


        $scope.logoutUser = function () {
            LogoutFactory.logout({email:TokenService.username});
            TokenService.user={}
            TokenService.token='';
            TokenService.isLogged=false;
            $cookies.email='';
            $cookies.token='';
            growlService.growl('Logging out...');
            $window.location.href='/taskit/index.html';
            delete $cookies['email'];
            delete $cookies['token'];
        };

        //Close sidebar on click
        this.sidebarStat = function(event) {
            console.log(event);
            if (!angular.element(event.target).parent().hasClass('active')) {
                this.sidebarToggle.left = false;
            }
        }

        //Listview Search (Check listview pages)
        this.listviewSearchStat = false;
        
        this.lvSearch = function() {
            this.listviewSearchStat = true; 
        }
        
        //Listview menu toggle in small screens
        this.lvMenuStat = false;
        
        //Blog
        this.wallCommenting = [];
        
        this.wallImage = false;
        this.wallVideo = false;
        this.wallLink = false;
    })


    // =========================================================================
    // Header
    // =========================================================================
    .controller('headerCtrl', function($scope, $timeout, messageService, ErrorHandler, IssuesMessageBoxFactory, TokenService){

         // Top Search
        this.openSearch = function(){
            angular.element('#header').addClass('search-toggled');
            //growlService.growl('Welcome back Mallinda Hollaway', 'inverse');
        }

        this.closeSearch = function(){
            angular.element('#header').removeClass('search-toggled');
        }



        $scope.filter="";
        $scope.ibLoadedIssues={};
        $scope.ibNumberOfIssues=0;
        IssuesMessageBoxFactory.query({uID: TokenService.username}).$promise.then(function(response){
            $scope.ibLoadedIssues=response;
            $scope.ibLoadedIssues.forEach(function(issue){
                $scope.ibNumberOfIssues++;
                issue.title=issue.title.trim();
                if(issue.description == null)
                    issue.description = "";
                else
                    issue.description=issue.description.trim();
            });
        }, function(error){
           ErrorHandler.handle("Could not fetch issues.", error);
        });


        // Get messages and notification for header
        /*this.img = messageService.img;
        this.user = messageService.user;
        this.text = messageService.text;

        this.messageResult = messageService.getMessage(this.img, this.user, this.text);
        */

        //Clear Notification
        /*this.clearNotification = function($event) {
            $event.preventDefault();
            
            var x = angular.element($event.target).closest('.listview');
            var y = x.find('.lv-item');
            var z = y.size();
            
            angular.element($event.target).parent().fadeOut();
            
            x.find('.list-group').prepend('<i class="grid-loading hide-it"></i>');
            x.find('.grid-loading').fadeIn(1500);
            var w = 0;
            
            y.each(function(){
                var z = $(this);
                $timeout(function(){
                    z.addClass('animated fadeOutRightBig').delay(1000).queue(function(){
                        z.remove();
                    });
                }, w+=150);
            })
            
            $timeout(function(){
                angular.element('#notifications').addClass('empty');
            }, (z*150)+200);
        }*/
        
        // Clear Local Storage
        this.clearLocalStorage = function() {
            
            //Get confirmation, if confirmed clear the localStorage
            swal({   
                title: "Are you sure?",   
                text: "All your saved localStorage values will be removed",   
                type: "warning",   
                showCancelButton: true,   
                confirmButtonColor: "#F44336",   
                confirmButtonText: "Yes, delete it!",   
                closeOnConfirm: false 
            }, function(){
                localStorage.clear();
                swal("Done!", "localStorage is cleared", "success");
            });
            
        }
        
        //Fullscreen View
        this.fullScreen = function() {
            //Launch
            function launchIntoFullscreen(element) {
                if(element.requestFullscreen) {
                    element.requestFullscreen();
                } else if(element.mozRequestFullScreen) {
                    element.mozRequestFullScreen();
                } else if(element.webkitRequestFullscreen) {
                    element.webkitRequestFullscreen();
                } else if(element.msRequestFullscreen) {
                    element.msRequestFullscreen();
                }
            }

            //Exit
            function exitFullscreen() {
                if(document.exitFullscreen) {
                    document.exitFullscreen();
                } else if(document.mozCancelFullScreen) {
                    document.mozCancelFullScreen();
                } else if(document.webkitExitFullscreen) {
                    document.webkitExitFullscreen();
                }
            }

            if (exitFullscreen()) {
                launchIntoFullscreen(document.documentElement);
            }
            else {
                launchIntoFullscreen(document.documentElement);
            }
        }
    
    })



    // =========================================================================
    // Best Selling Widget
    // =========================================================================

    .controller('bestsellingCtrl', function(bestsellingService){
        // Get Best Selling widget Data
        this.img = bestsellingService.img;
        this.name = bestsellingService.name;
        this.range = bestsellingService.range; 
        
        this.bsResult = bestsellingService.getBestselling(this.img, this.name, this.range);
    })

 
    // =========================================================================
    // Todo List Widget
    // =========================================================================

    .controller('todoCtrl', function(todoService){
        
        //Get Todo List Widget Data
        this.todo = todoService.todo;
        
        this.tdResult = todoService.getTodo(this.todo);
        
        //Add new Item (closed by default)
        this.addTodoStat = false;
    })


    // =========================================================================
    // Recent Items Widget
    // =========================================================================

    .controller('recentitemCtrl', function(recentitemService){
        
        //Get Recent Items Widget Data
        this.id = recentitemService.id;
        this.name = recentitemService.name;
        this.parseInt = recentitemService.price;
        
        this.riResult = recentitemService.getRecentitem(this.id, this.name, this.price);
    })


    // =========================================================================
    // Recent Posts Widget
    // =========================================================================
    
    .controller('recentpostCtrl', function(recentpostService){
        
        //Get Recent Posts Widget Items
        this.img = recentpostService.img;
        this.user = recentpostService.user;
        this.text = recentpostService.text;
        
        this.rpResult = recentpostService.getRecentpost(this.img, this.user, this.text);
    })


    //=================================================
    // Profile
    //=================================================

    .controller('profileCtrl', function($scope, $stateParams, ErrorHandler, growlService, TokenService, UserFactory, ProjectsFactory, RewardsByProjectFactory, RewardsByUserFactory, ErrorHandler){

        var currentUID;
        var userRewardProjectIds = [];
        var userRewardNames = [];
        var userRewardXpBases = [];
        $scope.userRewards = [];


        if((typeof $stateParams.uID === 'undefined') || $stateParams.uID=="" )
            currentUID = TokenService.username.trim();
        else
            currentUID = $stateParams.uID.trim();

        //Get Profile Information from User Service
        UserFactory.get({uID: currentUID}).$promise.then(function(user){
            $scope.avatar=user.avatar;
            $scope.firstName=user.firstName;
            $scope.lastName=user.lastName;
            $scope.userID=user.userID;
            console.log("$scope.userID " + $scope.userID + " , currentUID " + currentUID + " $scope.lastName " + $scope.lastName);
        }, function(error){
           ErrorHandler.handle("Could not fetch user information from server.", error);
        });

        ProjectsFactory.query({uID: currentUID}).$promise.then(function(response){
        //AdminProjectsFactory.query().$promise.then(function(response){
        //edit project information so it can easily be displayed (trim returned variables and identify user role for each project
            response.forEach(function(project){
                project.title=project.title.trim();

                if(project.description == null)
                    project.description = "";
                else
                    project.description=project.description.trim();

                userInProject=project.allUser.filter(function(user){
                    return(user.user.trim()==TokenService.username);
                });
                project.role=userInProject[0].role.trim();

                RewardsByUserFactory.query({uID: currentUID}).$promise.then(function(response){
                    response.forEach(function(reward){
                        reward.name = reward.name.trim();
                    });
                    $scope.userRewards.push(response);
                }, function(error){
                   ErrorHandler.handle("Could not fetch reward/s.", error);
                });

            });
            $scope.userProjects=response;
        }, function(error){
           ErrorHandler.handle("Could not fetch projects.", error);
        });



        //Edit
        this.editSummary = 0;
        this.editInfo = 0;
        this.editContact = 0;
    
        
        this.submit = function(item, message) {            
            if(item === 'profileSummary') {
                this.editSummary = 0;
            }
            
            if(item === 'profileInfo') {
                this.editInfo = 0;
            }
            
            if(item === 'profileContact') {
                this.editContact = 0;
            }
            
            growlService.growl(message+' has updated Successfully!', 'inverse'); 
        }

    })



    //=================================================
    // LOGIN & REGISTER
    //=================================================

    .controller('loginCtrl', function ($rootScope, $cookies, $window, $timeout, $q, $http, growlService, LoginFactory, UserRegistrationFactory) {

            $rootScope.avatar="img/avatars/0.png";

            // callback for ng-click 'loginUser':
            this.loginUser = function () {
                var email = "";
                var password = "";
                var passwordCheck = "";
                $rootScope.loginFail=false;

                if(this.login){
                    var email = this.login.email;
                    var password = this.login.password;
                    var passwordCheck = this.login.passwordCheck;
                }

                //request token
                LoginFactory.receive(this.login).$promise.then(function(data){
                    //workaround to evaluate successful login (currently server returns 200 even if login failed)
                    if(typeof data.token === 'undefined'){
                        //console.log("Could not log in. Wrong username or password?");
                        $rootScope.loginFail=true;
                        console.log(data);
                    }else{
                        //otherwise log in by setting auth header, rerouting and setting the cookie
                        $http.defaults.headers.common['user-token'] = data.token;
                        console.log("Token is:" + data.token);
                        $cookies.email=email;
                        $cookies.token=data.token;
                        $window.location.href='/taskit/main.html';
                    }
                }, function(error){
                    //console.log("Could not log in. Wrong username or password?");
                    //console.log(error);
                    $rootScope.loginFail=true;
                });
            };

            // callback for ng-click 'saveUser':
            this.createUser = function () {
                $rootScope.firstNameFail = false;
                $rootScope.lastNameFail = false;
                $rootScope.emailFail = false;
                $rootScope.passwordMatchFail = false;
                $rootScope.passwordFail = false;
                $rootScope.registrationFail = false;

                if(!this.user){
                    $timeout(function(){document.getElementById('firstName').focus();});
                    console.log("FirstName cannot be empty!");
                    $rootScope.firstNameFail = true;
                } else {
                    userToRegister={userID: this.user.userID, password: this.user.password,
                        firstName: this.user.firstName, lastName: this.user.lastName, avatar: "img/avatars/0.png"};

                    if(!userToRegister.firstName){
                        $timeout(function(){document.getElementById('firstName').focus();});
                        console.log("FirstName cannot be empty!");
                        $rootScope.firstNameFail = true;
                    } else
                    if(!userToRegister.lastName){
                        $timeout(function(){document.getElementById('lastName').focus();});
                        console.log("LastName cannot be empty!");
                        $rootScope.lastNameFail = true;
                    } else
                    if(!userToRegister.userID){
                        $timeout(function(){document.getElementById('email').focus();});
                        console.log("LastName cannot be empty!");
                        $rootScope.emailFail = true;
                    } else
                    if (userToRegister.password && userToRegister.password.length>7){
                        if(this.passwordCheck && userToRegister.password===this.passwordCheck){
                            userToRegister.avatar=$rootScope.avatar;
                            //console.log("Root avatar: " + $rootScope.avatar);
                            //console.log("registration is " + userToRegister.userID + userToRegister.password + userToRegister.firstName, + userToRegister.lastName + userToRegister.avatar);

                            UserRegistrationFactory.create(userToRegister).$promise.then(function(response){
                                var loginRegisteredUser = {email: userToRegister.userID, password: userToRegister.password};
                                //growlService.growl("Registration successful, logging in...");
                                LoginFactory.receive(loginRegisteredUser).$promise.then(function(token){
                                    $http.defaults.headers.common['user-token'] = token.token;
                                    $cookies.email=loginRegisteredUser.email;
                                    $cookies.token=token.token;
                                    $window.location.href='/taskit/main.html';
                                });
                            },function(error){
                                console.log("Registration failed." + error.value);
                                $rootScope.registrationFail=true;
                                $rootScope.errorMessage=error.item;
                            });
                        }else{
                            //growlService.growl("Password has to be longer than 7 characters. Please try again.");
                            $timeout(function(){document.getElementById('passwordRepeat').focus();});
                            console.log("Passwords did not match. Please try again.");
                            $rootScope.passwordMatchFail = true;
                        }
                    }else{
                        //growlService.growl("Passwords did not match. Please try again.");
                        $timeout(function(){document.getElementById('password').focus();});
                        console.log("Password has to be longer than 7 characters. Please try again.");
                        $rootScope.passwordFail = true;
                    }
                }
            };

            //Status
            this.loginStat = 1;
            this.register = 0;
            this.forgot = 0;
    })


    //=================================================
    // MAIN VIEW
    //=================================================

    .controller('mainViewCtrl', function($filter, $scope, ErrorHandler, ProjectsFactory, TokenService){

        $scope.sort="";
        $scope.filter="";
        ProjectsFactory.query({uID: TokenService.username}).$promise.then(function(response){
        //AdminProjectsFactory.query().$promise.then(function(response){
        //edit project information so it can easily be displayed (trim returned variables and identify user role for each project
            response.forEach(function(project){
                project.title=project.title.trim();

                if(project.description == null)
                    project.description = "";
                else
                    project.description=project.description.trim();

                userInProject=project.allUser.filter(function(user){
                    return(user.user.trim()==TokenService.username);
                });
                project.role=userInProject[0].role.trim();
            });
            $scope.userProjects=response;
            $scope.userProjects.forEach
        }, function(error){
           ErrorHandler.handle("Could not fetch projects.", error);
        });

    })

    //=================================================
    // REWARD VIEW
    //=================================================

    .controller('createRewardCtrl', function (ProjectsFactory, UsersFactory, RewardFactory, RewardsCreatedByUserFactory, ErrorHandler,
        TokenService, $scope, $location, $window) {


        updateRewards=function(){
            RewardsCreatedByUserFactory.query({uID: TokenService.username}).$promise.then(function(rewards){
                $scope.rewards=rewards;
            }, function(error){
                ErrorHandler.handle("Could not fetch rewards from server.", error);
            });
        }

        updateRewards();

        $scope.createReward=function(){
            RewardFactory.create({userMail: TokenService.username, name: $scope.newReward.name, description:$scope.newReward.description,
            xpbase:$scope.newReward.xp, imageLink:$scope.newReward.link}).$promise.then(function(rewards){
                updateRewards();
            }, function(error){
                ErrorHandler.handle("Could not fetch rewards from server.", error);
            });
        };

        $scope.deleteReward=function(rewardID){
            RewardFactory.delete({rID:rewardID}).$promise.then(function(rewards){
                updateRewards();
            }, function(error){
                ErrorHandler.handle("Could not delete reward from server.", error);
            });
        }
    })

    //=================================================
    // PROJECT CREATION
    //=================================================

    .controller('createProjectCtrl', function ($scope, $location, $window, $timeout, $state, growlService, ErrorHandler,
        TokenService, ProjectFactory, AddUserToProjectFactory, UsersFactory, RewardsCreatedByUserFactory) {

        console.log("starting Project Creation");

        $scope.descriptionFail = false;
        $scope.titleFail = false;

        $scope.users={};
        $scope.users.allUsers=[];
        $scope.users.allUsersTemp=[];
        $scope.users.contributableUsers=[];
        $scope.users.managementableUsers=[];
        $scope.users.userPickerContributor=[];
        $scope.users.userPickerManager=[];

        // Update Contributors before selecting a Contributor
        $scope.updateContributors=function(){
            $scope.users.contributableUsers = [];

            $scope.users.allUsersTemp = $scope.users.allUsers;
            [].forEach.call($scope.users.userPickerManager,function(name){
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== name;});
            });
            $scope.users.allUsersTemp.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";

                $scope.users.contributableUsers.push(user);
            });
            console.log("userPickerContributor: " + $scope.users.userPickerContributor);
        };

        // Update Managers before selecting a Manager
        $scope.updateManagers=function(){
            $scope.users.managementableUsers = [];

            $scope.users.allUsersTemp = $scope.users.allUsers;
            [].forEach.call($scope.users.userPickerContributor,function(name){
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== name;});
            });
            $scope.users.allUsersTemp.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.managementableUsers.push(user);
            });
        };

        // get all users (for adding users to the project)
        UsersFactory.query().$promise.then(function(response){

            //add a field containing a readable caption for the users
            response.forEach(function(user){
                $scope.users.allUsers.push(user);
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.contributableUsers.push(user);
                $scope.users.managementableUsers.push(user);
            });
        }, function(error){
           ErrorHandler.handle("Could not fetch users from server.", error);
        });

        //Get all created rewards for the current user
        RewardsCreatedByUserFactory.query({uID: TokenService.username}).$promise.then(function(rewards){
            $scope.rewardList=rewards;
            console.log(rewards);
        }, function(error){
           ErrorHandler.handle("Could not fetch your rewards from server.", error);
        });

        $scope.createProject = function () {
            if(!$scope.project) {
                $scope.titleFail = true;
                $timeout(function(){document.getElementById('projectTitle').focus();});
            }
            else
                if(!$scope.project.description) {
                    $scope.descriptionFail = true;
                    $timeout(function(){document.getElementById('projectDescription').focus();});
                }
            else {
                //console.log($scope.users.userPickerContributor);
                ProjectFactory.create({title: $scope.project.title, description: $scope.project.description}).$promise.then(function(response){
                    $scope.pID=response.item;

                    AddUserToProjectFactory.add({project: $scope.pID, user: TokenService.username, role: "ADMIN"});
                    $scope.users.userPickerContributor.forEach(function(contributor){
                        AddUserToProjectFactory.add({project: $scope.pID, user: contributor, role: "CONTRIBUTOR"});
                    });
                    $scope.users.userPickerManager.forEach(function(manager){
                        AddUserToProjectFactory.add({project: $scope.pID, user: manager, role: "ADMIN"});
                    });

                    $state.go("viewProject",{pID:$scope.pID});

                    swal({
                    	title: "Project Created!",
                    	type: "success",
                    	timer: 2500,
                    	showConfirmButton: false
                    });

                }, function(error){
                   ErrorHandler.handle("Could not save your project.", error);
                });
            }
        };
    })

    //=================================================
    // PROJECT UPDATE
    //=================================================

    .controller('updateProjectCtrl', function ($scope, $stateParams, $timeout, growlService, ErrorHandler, TokenService,
        ProjectFactory, AddUserToProjectFactory, UserFactory, UsersFactory, RewardsByProjectFactory, RewardsCreatedByUserFactory, RemoveUserFromProjectFactory) {

       growlService.growl('Fetching project information...');

       $scope.users={};
       $scope.users.allUsers=[];
       $scope.users.allUsersTemp=[];
       $scope.users.contributableUsers=[];
       $scope.users.managementableUsers=[];
       $scope.users.userPickerContributor=[];
       $scope.users.userPickerManager=[];

       $scope.descriptionFail = false;
       $scope.titleFail = false;

       //Set project ID according to parameter
       $scope.currentPID = $stateParams.pID;

       $scope.selectedProject={};
       $scope.selectedProject.userList=[];

        $scope.updateProjectInformation=function(){
            //Get project information
            ProjectFactory.show({pID: $scope.currentPID, uID:TokenService.username}).$promise.then(function(response){0
                response.title=response.title.trim();
                response.description=response.description.trim();
                $scope.selectedProject=response;
                $scope.selectedProject.userList=[];
                //get user information for all users of the current project
                $scope.selectedProject.allUser.forEach(function(participant){
                    UserFactory.get({uID: participant.user.trim()}).$promise.then(function(user){
                        user.role=participant.role.trim();
                        $scope.selectedProject.userList.push(user);
                    }, function(error){
                        ErrorHandler.handle("Could not fetch users from server.", error);
                    });
                }, function(error){
                      ErrorHandler.handle("Could not fetch user information from server.", error);
                });
                thisUser=$scope.selectedProject.allUser.filter(function(user){
                    return(user.user.trim()==TokenService.username);
                });
                $scope.currentUserRole=thisUser[0].role.trim();
                //console.log("Current User Role:" + $scope.currentUserRole);
                //console.log($scope.selectedProject.allTasks);
            }, function(error){
                ErrorHandler.handle("Could not fetch project information from server.", error);
            });
        };
        $scope.updateProjectInformation();

        // Update Contributors before selecting a Contributor
        $scope.updateContributors=function(){
            $scope.users.contributableUsers = [];

            $scope.users.allUsersTemp = $scope.users.allUsers;
            [].forEach.call($scope.users.userPickerManager,function(name){
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== name;});
            });
            [].forEach.call($scope.selectedProject.userList,function(user){
                var userID = user.userID;
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== userID;});
            });


            $scope.users.allUsersTemp.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";

                $scope.users.contributableUsers.push(user);
            });
        };

        // Update Managers before selecting a Manager
        $scope.updateManagers=function(){
            $scope.users.managementableUsers = [];

            $scope.users.allUsersTemp = $scope.users.allUsers;
            [].forEach.call($scope.users.userPickerContributor,function(name){
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== name;});
            });
            [].forEach.call($scope.selectedProject.userList,function(user){
                var userID = user.userID;
                $scope.users.allUsersTemp = $scope.users.allUsersTemp.filter(function (user) {return user.userID !== userID;});
            });
            $scope.users.allUsersTemp.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.managementableUsers.push(user);
            });
        };

        UsersFactory.query().$promise.then(function(response){
            //add a field containing a readable caption for the users
            response.forEach(function(user){
                $scope.users.allUsers.push(user);
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.contributableUsers.push(user);
                $scope.users.managementableUsers.push(user);
            });
        }, function(error){
           ErrorHandler.handle("Could not fetch users from server.", error);
        });

        //Get all rewards for the current project and user
        RewardsByProjectFactory.query({pID: $scope.currentPID, uID: TokenService.username}).$promise.then(function(rewards){
            $scope.selectedProject.rewards=rewards;
        }, function(error){
            ErrorHandler.handle("Could not get rewards for this project.", error);
        });

        //Get all created rewards for the current user
        RewardsCreatedByUserFactory.query({uID: TokenService.username}).$promise.then(function(rewards){
            //console.log(rewards);
            $scope.rewardList=rewards;
        }, function(error){
           ErrorHandler.handle("Could not fetch your rewards from server.", error);
        });

        $scope.deleteUserFromProject=function(userID){
            swal({
                title: "Remove User from Project?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes!",
                cancelButtonText: "No!",
                closeOnConfirm: false,
                closeOnCancel: true
            }, function(isConfirm){
                if (isConfirm) {
                   RemoveUserFromProjectFactory.delete({pID:$scope.currentPID, uID:userID}).$promise.then(function(response){
                       $scope.updateProjectInformation();
                       swal({
                       	title: "User Removed!",
                       	type: "success",
                       	timer: 2000,
                       	showConfirmButton: false
                       });
                   }, function(error){
                       ErrorHandler.handle("Could not delete user from project.", error);
                   });
                }
            });
        }

        //Save changes after button is clicked
        $scope.saveProject=function(){
            if(!$scope.selectedProject.title) {
                $scope.titleFail = true;
                $timeout(function(){document.getElementById('projectTitle').focus();});
            }
            else
                if(!$scope.selectedProject.description) {
                    $scope.descriptionFail = true;
                    $timeout(function(){document.getElementById('projectDescription').focus();});
                }
            else {
                ProjectFactory.update({pID: $scope.currentPID}, {
                    title: $scope.selectedProject.title,
                    description: $scope.selectedProject.description
                });
                //save newly added users to project:
                $scope.users.userPickerContributor.forEach(function(contributor){
                    AddUserToProjectFactory.add({project: $scope.currentPID, user: contributor, role: "CONTRIBUTOR"});
                });
                $scope.users.userPickerManager.forEach(function(manager){
                    AddUserToProjectFactory.add({project: $scope.currentPID, user: manager, role: "ADMIN"});
                });
                $scope.updateProjectInformation();
                swal({
                	title: "Project Saved!",
                	type: "success",
                	timer: 2500,
                	showConfirmButton: false
                });
            }
        };
    })

    //=================================================
    // ISSUE CREATION
    //=================================================

    .controller('createIssueCtrl', function ( $scope, $state, $stateParams, $state, $timeout, growlService, ErrorHandler, IssuePostFactory, ProjectFactory, TokenService) {

        // callback for ng-click 'create Issue':
        $scope.currentPID = $stateParams.pID;

        ProjectFactory.show({pID: $scope.currentPID, uID: TokenService.username}).$promise.then(function(response){
            $scope.selectedProject=response;
        });


        $scope.createIssue = function () {
           if(!$scope.issue) {
                $scope.titleFail = true;
                $timeout(function(){document.getElementById('issueTitle').focus();});
           }
           else
               if(!$scope.issue.description) {
                    $scope.descriptionFail = true;
                    $timeout(function(){document.getElementById('issueDescription').focus();});
               }
           else {
               IssuePostFactory.create({pID: $scope.currentPID, uID: TokenService.username}, {title:$scope.issue.title, description:$scope.issue.description}).$promise.then(function(response){
                    $state.go("viewProject", {pID:$scope.currentPID});

                    swal({
                    	title: "Issue Created!",
                    	type: "success",
                    	timer: 2500,
                    	showConfirmButton: false
                    });

               }, function(error){
                    ErrorHandler.handle("Could not create Issue.", error);
               });
           }
        };
    })

    //=================================================
    // ISSUE TRANSFORMATION
    //=================================================

    .controller('viewIssue', function ($scope, $state, $stateParams, $state, growlService, ErrorHandler, IssueRetrieveFactory, ProjectFactory, TokenService) {
        $scope.currentIID = $stateParams.iID;
        $scope.currentPID = $stateParams.pID;

        ProjectFactory.show({pID: $scope.currentPID, uID: TokenService.username}).$promise.then(function(response){
            $scope.selectedProject=response;
        });

        IssueRetrieveFactory.show({issueID: $scope.currentIID}).$promise.then(function(response){
            $scope.issue=response;
            $scope.issue.title=response.title.trim();
            $scope.issue.description=response.description.trim();
        }, function(error){
            ErrorHandler.handle("Could not retrieve Issue Information.", error);
        });

        $scope.discardIssue=function(){
            swal({
                title: "Discard Issue?",
                text: "You will not be able to recover this Issue!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, discard it!",
                cancelButtonText: "Cancel",
                closeOnConfirm: false,
                closeOnCancel: true
            }, function(isConfirm){
                if (isConfirm) {
                    IssueRetrieveFactory.delete({issueID: $scope.currentIID}).$promise.then(function(response){
                        $state.go("home");
                    }, function(error){
                        ErrorHandler.handle("Could not delete Issue.", error);
                    });
                    swal({
                        title: "Issue discarded!",
                        timer: 2000,
                        showConfirmButton: false,
                        type: "success"
                    });
                }
            });
        }
	})

    //=================================================
    // TASK CREATION
    //=================================================

    .controller('createTaskCtrl', function ( $scope, $state, $stateParams, $timeout, growlService, TokenService, ErrorHandler,
        TasksFactory, TemplateFactory, IssueRetrieveFactory, ProjectFactory, UserFactory) {
        $scope.titleFail = false;
        $scope.descriptionFail = false;
        $scope.templateFail = false;
        $scope.isTransformedIssue=false;
        $scope.task={};
        $scope.task.comments = false;
        $scope.task.userType = false;
        $scope.task.type = "collaborative_task";
        $scope.currentPID = $stateParams.pID;
        $scope.currentIID = $stateParams.iID;


        //set fields if task originates from an issue
        if (!(typeof $scope.currentIID==='undefined')){
            $scope.currentIID = $stateParams.iID;
            IssueRetrieveFactory.show({issueID: $scope.currentIID}).$promise.then(function(response){
                    $scope.isTransformedIssue=true;
                    $scope.issue=response;
                    $scope.task={};
                    $scope.task.title=response.title.trim();
                    $scope.task.description=response.description.trim();
                }, function(error){
                    ErrorHandler.handle("Could not retrieve Issue Information although issue ID was provided.", error);
                });
        }

        $scope.templates={};
        //initialize available states for subtasks
        $scope.states = [
            {id:'1',title:'Draft'},
            {id:'2',title:'Not Started'},
            {id:'3',title:'Awaiting precondition'},
            {id:'4',title:'Waiting'},
            {id:'5',title:'Created'},
            {id:'6',title:'Open'},
            {id:'7',title:'Active'},
            {id:'8',title:'In Progress'},
            {id:'9',title:'Working'},
            {id:'10',title:'Verified'},
            {id:'11',title:'Resolved'},
            {id:'12',title:'Completed'},
            {id:'13',title:'Finished'},
            {id:'14',title:'Done'},
            {id:'15',title:'Accepted'},
            {id:'16',title:'Info Requested'},
            {id:'17',title:'Incomplete'},
            {id:'18',title:'Cancelled'},
            {id:'19',title:'Failed'}
            ];
        $scope.allTemplates='Templates: '
        //retrieve list of templates from backend
        TemplateFactory.query().$promise.then(function(response){
            $scope.templates=response;
            $scope.templates.forEach(function(template){
                template.title=template.title.trim();
                template.description=template.description.trim();
                template.creationDate=template.creationDate.trim();
                $scope.allTemplates=$scope.allTemplates + ", " + template.title;
            })
            //console.log($scope.templates);
        }, function(error){
            ErrorHandler.handle("Could not retrieve templates from server", error);
        });

        //initialize arrays for states and templates/subtasks
        var counterTemplates = 0;
        var counterStates= 0;

        $scope.data = {
            stateFields: [],
            stateFieldIds:[],
            templateFields: [],
            templateFieldIds: []
        }

        //add and remove selection fields for states and templates on click
        $scope.addState = function() {
            $scope.data.stateFieldIds.push({
                name: "state" + counterStates++,
                id: counterStates
            });
            //console.log($scope.data.stateFields);
            $scope.allStateFields="";
            $scope.data.stateFields.forEach(function (stateField){
                $scope.allStateFields+=", " + stateField.title;
            })
        };
        $scope.addTemplate = function() {
            $scope.data.templateFieldIds.push({
                name: "template" + counterTemplates++,
                id: counterTemplates
            });
            //console.log($scope.data.templateFields);
            $scope.allTemplateFields="";
            $scope.data.templateFields.forEach(function (templateField){
                $scope.allTemplateFields+=", " + templateField.id;
            })
        };

        $scope.removeTemplate= function() {
            $scope.data.templateFieldIds.pop();
            $scope.data.templateFields.pop();
            counterTemplates--;
        };

        $scope.removeState = function() {
            $scope.data.stateFieldIds.pop();
            $scope.data.stateFields.pop();
            counterStates--;
        };

        $scope.addTemplate();
        $scope.userList=[];

        //retrieve user list of the current project from server
        ProjectFactory.show({pID: $scope.currentPID, uID:TokenService.username}).$promise.then(function(response){
            $scope.selectedProject=response;

            //get user information for all users of the current project
            $scope.selectedProject.allUser.forEach(function(participant){
                UserFactory.get({uID: participant.user.trim()}).$promise.then(function(user){
                    user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                    user.userID={userID: user.userID.trim()};
                    $scope.userList.push(user);
                }, function(error){
                    ErrorHandler.handle("Could not fetch users from server.", error);
                });
            });
        }, function(error){
            ErrorHandler.handle("Could not fetch project information from server.", error);
        });

        //prepare data and send request for task creation to backend
        $scope.createTask = function() {
            $scope.templateFail = false;
            $scope.titleFail = false;
            $scope.descriptionFail = false;

            //create an array of subtasks that conforms to the API
            $scope.task.subtasks=[];
            $scope.data.templateFields.forEach(function (templateField){
                if (!(templateField.id===undefined)){
                    var subtask={dslTemplateId: templateField.id};
                    $scope.task.subtasks.push(subtask);
                }
            });

            //error handling if not title is filled in
            if(!$scope.task || !$scope.task.title) {
                $scope.titleFail = true;
                $timeout(function(){
                    $('html, body').animate({ scrollTop: 0 }, 'fast');
                    document.getElementById('taskTitle').focus();
                });
            }
            else
                //error handling if no description is filled in
                if(!$scope.task.description) {
                    $scope.descriptionFail = true;
                    $timeout(function(){
                        $('html, body').animate({ scrollTop: 0 }, 'fast');
                        document.getElementById('taskDescription').focus();
                    });
                }
            else
                //error handling if no template is selected
                if ( $scope.task.subtasks.length == 0 ) {
                    $scope.templateFail = true;
                    $timeout(function(){
                        $('html, body').animate({ scrollTop: 0 }, 'fast');
                    });
                }
            else {
                //if all required information is available, send task creation request to backend
                //console.log("users:");
                if ($scope.task.userType==='all'){
                    $scope.task.contributors=[];
                    $scope.userList.forEach(function(user){
                        $scope.task.contributors.push(user.userID);
                    })
                }else{
                    $scope.task.contributors=$scope.task.contributorSelection;
                }

                //create an array of states that conforms to the API and includes Open and Closed
                $scope.task.states=[{stateName:'Open'}, {stateName:'Closed'}];
                $scope.data.stateFields.forEach(function (stateField){
                    if (!(stateField.state===undefined)){
                        var state={stateName: stateField.title};
                        $scope.task.states.push(state);
                    }
                });
                //console.log($scope.task.states);

                //Send request to server
                TasksFactory.create({pid: $scope.currentPID},
                    {title: $scope.task.title, description: $scope.task.description, executionType: $scope.task.type,
                    projectId: $scope.currentPID, userMail: TokenService.username, commentsAllowed: $scope.task.comments,
                    subtaskList: $scope.task.subtasks, userList: $scope.task.contributors,
                    taskStates: $scope.task.states}).$promise.then(function(result){
                        //if successful, delete Issue if applicable and change to ProjectView
                        if($scope.isTransformedIssue){
                                IssueRetrieveFactory.delete({issueID: $scope.currentIID}).$promise.then(function(response){
                            }, function(error){
                                ErrorHandler.handle("Could not delete Issue.", error);
                            });
                        }
                        swal({
                        	title: "Task Created!",
                        	type: "success",
                        	timer: 2500,
                        	showConfirmButton: false
                        });
                        $state.go("viewProject", {pID:$scope.currentPID});
                }, function(error){
                    ErrorHandler.handle("Could not save task.", error);
                });
            }
        }
    })

    //=================================================
    // TASK UPDATE
    //=================================================

    .controller('updateTaskCtrl', function ( $scope, $state, $stateParams, $timeout, growlService, TokenService, ErrorHandler,
        TaskFactory, TaskUserFactory, TaskCommentFactory, IssuesFactory, TemplateFactory, ProjectFactory, UserFactory, SubtaskFactory) {

        //retrieve current project id and task id from the state params
        $scope.currentPID = $stateParams.pID;
        $scope.currentTID = $stateParams.tID;
        $scope.maxXp = 0;
        $scope.userXp = 0;

        updateTaskInformation=function(){
            //retrieve information from the project the current task is related to
            ProjectFactory.show({pID: $scope.currentPID, uID:TokenService.username}).$promise.then(function(response){
                $scope.project=response;
                $scope.project.title=$scope.project.title.trim();
                $scope.project.description=$scope.project.description.trim();

                //generate user list of potential contributors
                $scope.project.userList=[];
                //get user information for all users of the current project
                $scope.project.allUser.forEach(function(participant){
                    UserFactory.get({uID: participant.user.trim()}).$promise.then(function(user){
                        user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                        user.role=participant.role.trim();
                        $scope.project.userList.push(user);
                    }, function(error){
                        ErrorHandler.handle("Could not fetch users from server.", error);
                    });
                });
                thisUser=$scope.project.allUser.filter(function(user){
                    return(user.user.trim()==TokenService.username);
                });

                $scope.currentUserRole=thisUser[0].role.trim();
                console.log("Current User Role:" + $scope.currentUserRole);
            }, function(error){
                ErrorHandler.handle("Could not fetch project information from server.", error);
            });

            //retrieve task related information from server
            TaskFactory.show({tID: $stateParams.tID}).$promise.then(function(response){
                $scope.task=response;
                $scope.comments=[];
                $scope.commentObject = {};
                $scope.task.title=$scope.task.title.trim();
                $scope.task.description=$scope.task.description.trim();
                $scope.task.status=$scope.task.status.trim();

                //get user information regarding the comments section
                $scope.task.commentList.forEach(function(comment){
                    UserFactory.get({uID: comment.user_mail.trim()}).$promise.then(function(user){
                        user.id = comment.id;
                        user.text = comment.text.trim();
                        user.creationDate = comment.creationDate;
                        user.user_mail = user.userID.trim();
                        user.firstName = user.firstName.trim();
                        user.lastName = user.lastName.trim();
                        user.avatar = user.avatar.trim();
                        $scope.comments.push(user);
                    }, function(error){
                        ErrorHandler.handle("Could not fetch users for comments from server.", error);
                    });
                });

            //parse the DSL of all subtasks of the task
            $scope.task.parsedSubtaskList=[];
            $scope.model = {};
            $scope.model.range = {};
            $scope.values = {};
            $scope.task.subtaskList.forEach(function(subtask){
                $scope.MaxXp = $scope.MaxXp + subtask.xp;
                $scope.userXp = $scope.userXp + subtask.xp * subtask.percentageReached;
                subtask.status = subtask.status.trim();
                parsedTemplate=showDSL(ErrorHandler, subtask, $scope);
                $scope.task.parsedSubtaskList.push(parsedTemplate);
            });

            //console.log("Parsed Templates:");
            //console.log($scope.task.parsedSubtaskList);
            }, function(error){
                ErrorHandler.handle("Could not fetch task information from server.", error);
            });
        };
        updateTaskInformation();

        //delete user from task
        $scope.deleteUserFromTask=function(userID){
            swal({
                title: "Remove User from Task?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes!",
                cancelButtonText: "No!",
                closeOnConfirm: false,
                closeOnCancel: true
            },function(isConfirm){
                if (isConfirm) {
                    TaskUserFactory.delete({uID: userID, tID:$scope.currentTID}).$promise.then(function(response){
                        updateTaskInformation();
                        swal({
                            title: "User Removed!",
                            type: "success",
                            timer: 2000,
                            showConfirmButton: false
                        });
                    }, function(error){
                        ErrorHandler.handle("Could not fetch task information from server.", error);
                    });
                }
            });
        };

        $scope.updateTask=function(){
            if(!$scope.task.title) {
                $scope.titleFail = true;
                $timeout(function(){document.getElementById('taskTitle').focus();});
            }
            else
                if(!$scope.task.description) {
                    $scope.descriptionFail = true;
                    $timeout(function(){document.getElementById('taskDescription').focus();});
                }
            else {
                //Bitte noch die successmeldung hinzufügen, welche z.b. bei updateProject() verwendet wird, oder Michi bescheid geben, wenns implementiert ist.
                /*TaskFactory.update({pID: $scope.currentPID, tID: $scope.currentTID}, {
                    title: $scope.task.title,
                    description: $scope.task.description
                });*/
                //save newly added users to project:
                $scope.userPicker.forEach(function(contributor){
                    TaskUserFactory.add({uID: contributor, tID:$scope.currentTID});
                });
                updateTaskInformation();
            }
        };

        //save a new comment
        $scope.addComment=function(){
            TaskCommentFactory.add({tID: $scope.currentTID},{text: $scope.newCommentText, user_mail:TokenService.username}).$promise.then(function(response){
                updateTaskInformation();
            }, function(error){
                ErrorHandler.handle("Could not add comment.", error);
            });
            $timeout(function(){$scope.newCommentText="";});
        };

        //delete a comment
        $scope.deleteComment=function(commentID){
            swal({
                title: "Delete Comment?",
                text: "You will not be able to recover this Comment!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, delete it!",
                cancelButtonText: "Cancel",
                closeOnConfirm: false,
                closeOnCancel: true
            }, function(isConfirm){
                if (isConfirm) {
                    TaskCommentFactory.delete({tID: $scope.currentTID, cID: commentID}).$promise.then(function(response){
                        updateTaskInformation();
                    }, function(error){
                        ErrorHandler.handle("Could not delete Comment.", error);
                    });
                    swal({
                        title: "Comment deleted!",
                        timer: 2000,
                        showConfirmButton: false,
                        type: "success"
                    });
                }
            });
        };

        //update subtask
        $scope.updateSubtask=function(subtaskID){

            $scope.task.parsedSubtaskList.forEach(function(subtask) {
                if( subtask.id == subtaskID ) {
                    var taskElementList = [];
                    var jsonTaskElement = {};
                    var jsonRequest = {};
                    var subtaskTitle = "";
                    var subtaskDescription = "";
                    subtask.taskElements.forEach(function(taskElement) {
                        jsonTaskElement={};

                        console.log(subtaskID + "[" + taskElement.id + "]");
                        //console.log($scope.task.parsedSubtaskList);
                        switch(taskElement.itemType.trim()){

                        case "checkbox":
                            if( document.getElementById(subtaskID + "[" + taskElement.id + "]").checked ){
                                jsonTaskElement = { id:taskElement.id, status:"checked", value:taskElement.value};
                            }else{
                                jsonTaskElement = { id:taskElement.id, status:"unchecked", value:taskElement.value};
                            }
                            break;
                        case "textbox":
                            jsonTaskElement = { id:taskElement.id, value:document.getElementById(subtaskID + "[" + taskElement.id + "]").value, status:document.getElementById(subtaskID + "[" + taskElement.id + "]").value};
                            break;
                        case "slider":
                            jsonTaskElement = { id:taskElement.id, status:document.getElementById(subtaskID + "[" + taskElement.id + "]").value, value:taskElement.value};
                            break;
                        case "file":
                            jsonTaskElement = { id:taskElement.id, status:"file", link: taskElement.link};
                            break;
                        case "image":
                            jsonTaskElement = { id:taskElement.id, status:"image", link: taskElement.link};
                            break;
                        }
                        taskElementList.push(jsonTaskElement);
                    });
                    console.log("taskElementList");
                    console.log(taskElementList);
                    updateSubtaskRequest = {status:document.getElementById(subtask.id).value, taskElements:taskElementList};
                    if(subtask.status == "closed")
                        SubtaskFactory.update({sID:subtask.id},{status:"closed", title:subtask.title.trim(), description:subtask.description.trim(), xp:subtask.xp, taskElements:taskElementList}).$promise.then(function(response){
                            swal({
                                title: "Subtask Closed!",
                                type: "success",
                                timer: 2000,
                                showConfirmButton: false
                            });
                        }, function(error){
                           ErrorHandler.handle("Could not update Subtask.", error);
                        });
                    else
                        SubtaskFactory.update({sID:subtask.id}, {title:subtask.title.trim(), description:subtask.description.trim(), xp:subtask.xp, taskElements:taskElementList}).$promise.then(function(response){
                            swal({
                                title: "Subtask Saved!",
                                type: "success",
                                timer: 2000,
                                showConfirmButton: false
                            });
                        }, function(error){
                           ErrorHandler.handle("Could not update Subtask.", error);
                        });
                }
            });
        };
    })

    .directive('displayStates', function($compile) {
      return {
        scope: {
          display: "=", //import referenced model to our directives scope
          states: "="
        },
        templateUrl: 'template/stateSelect.html',
        link: function(scope, elem, attr, ctrl) {
        }
      }
    })

    .directive('displayTemplates', function($compile) {
      return {
        scope: {
          display: "=", //import referenced model to our directives scope
          templates: "="
        },
        templateUrl: 'template/templateSelect.html',
        link: function(scope, elem, attr, ctrl) {
        }
      }
    })


    //=================================================
    // CREATE TEMPLATE
    //=================================================

    .controller('createTemplateCtrl', function ( $scope, $state, growlService, TokenService, ErrorHandler, TemplateFactory) {
        //console.log("Template creation started");
        $scope.minimalCode="<?xml version='1.0' encoding='UTF-8' standalone='yes'?>\n<template>\n<identifier>\n<title>Smallest</title>\n<description>Sample</description>\n<estimatedWorkTime>60</estimatedWorkTime>\n<deadline>2016-03-03</deadline>\n<githook>???</githook>\n<comments>false</comments>\n</identifier>\n</template>";

        $scope.error=false;
        $scope.showPreview=false;

        var myCodeMirror = CodeMirror.fromTextArea(document.getElementById("dslsyntax"),{
            mode: "application/xml",
            lineNumbers:true,
            htmlMode:false,
            value:$scope.minimalCode
        });
        //, mode:"create"
        $scope.template={};
        $scope.createTemplate = function(){
            console.log(myCodeMirror.getValue());
            TemplateFactory.create({mode:"create"},{templateCategoryName: "default", templateCategoryDescription: "default category",
            title: $scope.template.title.trim(), description: $scope.template.description.trim(),
            syntax: myCodeMirror.getValue(), user_mail:TokenService.username}).$promise.then(function(response){
                swal({
                    title: "Template Created!",
                    type: "success",
                    timer: 2500,
                    showConfirmButton: false
                });
                $state.go("viewTemplates");
            }, function(error){
                ErrorHandler.handle("Could not save template.", error);
            });
        }

        $scope.previewTemplate = function(){
            $scope.showPreview=false;
            $scope.error=false;
            TemplateFactory.create({mode:"validate"},{templateCategoryName: "default", templateCategoryDescription: "default category",
            title: $scope.template.title.trim(), description: $scope.template.description.trim(),
            syntax: myCodeMirror.getValue(), user_mail:TokenService.username}).$promise.then(function(response){
                growlService.growl("Template successfully parsed!");
                $scope.showPreview=true;
                $scope.template.syntax=myCodeMirror.getValue();
                configureDSL($scope, $scope.template, ErrorHandler, TemplateFactory);
            }, function(error){
                $scope.error=true;
                $scope.templateError="Could not parse template: " + error.data.item;
            });
        }

    })


    //=================================================
    // VIEW TEMPLATES
    //=================================================

    .controller('viewTemplatesCtrl', function ( $scope, $state, growlService, TokenService, ErrorHandler, TemplateFactory) {
        //console.log("Template overview started");
        $scope.templates={};
        TemplateFactory.query().$promise.then(function(response){
            $scope.templates=response;
            $scope.templates.forEach(function(template){
                template.title=template.title.trim();
                template.description=template.description.trim();
                template.creationDate=template.creationDate.trim();
            })
            //console.log($scope.templates);
        }, function(error){
            ErrorHandler.handle("Could not retrieve templates.", error);
        });
    })


    //=================================================
    // VIEW TEMPLATE
    //=================================================

    .controller('viewTemplateCtrl', function ( $scope, $state, $stateParams, $compile, $sce, growlService, TokenService, ErrorHandler, TemplateFactory) {

        $scope.tID=$stateParams.tID;

        /*var myCodeMirror = CodeMirror.fromTextArea(document.getElementById("dslsyntax"),{
            mode: "application/xml",
            lineNumbers:true,
            htmlMode:false,
            value:$scope.minimalCode
        });*/

        $scope.template={};
        TemplateFactory.show({tID: $scope.tID}).$promise.then(function(response){
            $scope.template=response;
            configureDSL($scope, $scope.template, ErrorHandler, TemplateFactory);
        }, function(error){
            ErrorHandler.handle("Could not retrieve template.", error);
        });

    })

    //=================================================
    // DSL SPECIFIC DIRECTIVE
    //=================================================

    .directive('dynamic', function ($compile) {
      return {
        restrict: 'A',
        replace: true,
        link: function (scope, ele, attrs) {
          scope.$watch(attrs.dynamic, function(html) {
            ele.html(html);
            $compile(ele.contents())(scope);
          });
        }
      };
    })

    //=================================================
    // CALENDAR
    //=================================================
    
    .controller('calendarCtrl', function($modal){
    
        //Create and add Action button with dropdown in Calendar header. 
        this.month = 'month';
    
        this.actionMenu = '<ul class="actions actions-alt" id="fc-actions">' +
                            '<li class="dropdown" dropdown>' +
                                '<a href="" dropdown-toggle><i class="zmdi zmdi-more-vert"></i></a>' +
                                '<ul class="dropdown-menu dropdown-menu-right">' +
                                    '<li class="active">' +
                                        '<a data-calendar-view="month" href="">Month View</a>' +
                                    '</li>' +
                                    '<li>' +
                                        '<a data-calendar-view="basicWeek" href="">Week View</a>' +
                                    '</li>' +
                                    '<li>' +
                                        '<a data-calendar-view="agendaWeek" href="">Agenda Week View</a>' +
                                    '</li>' +
                                    '<li>' +
                                        '<a data-calendar-view="basicDay" href="">Day View</a>' +
                                    '</li>' +
                                    '<li>' +
                                        '<a data-calendar-view="agendaDay" href="">Agenda Day View</a>' +
                                    '</li>' +
                                '</ul>' +
                            '</div>' +
                        '</li>';

            
        //Open new event modal on selecting a day
        this.onSelect = function(argStart, argEnd) {            
            var modalInstance  = $modal.open({
                templateUrl: 'addEvent.html',
                controller: 'addeventCtrl',
                backdrop: 'static',
                keyboard: false,
                resolve: {
                    calendarData: function() {
                        var x = [argStart, argEnd];
                        return x;
                    }
                }
            });
        }
    })

    //Add event Controller (Modal Instance)
    .controller('addeventCtrl', function($scope, $modalInstance, calendarData){
        
        //Calendar Event Data
        $scope.calendarData = {
            eventStartDate: calendarData[0],
            eventEndDate:  calendarData[1]
        };
    
        //Tags
        $scope.tags = [
            'bgm-teal',
            'bgm-red',
            'bgm-pink',
            'bgm-blue',
            'bgm-lime',
            'bgm-green',
            'bgm-cyan',
            'bgm-orange',
            'bgm-purple',
            'bgm-gray',
            'bgm-black',
        ]
        
        //Select Tag
        $scope.currentTag = '';
        
        $scope.onTagClick = function(tag, $index) {
            $scope.activeState = $index;
            $scope.activeTagColor = tag;
        } 
        
        //Add new event
        $scope.addEvent = function() {
            if ($scope.calendarData.eventName) {

                //Render Event
                $('#calendar').fullCalendar('renderEvent',{
                    title: $scope.calendarData.eventName,
                    start: $scope.calendarData.eventStartDate,
                    end:  $scope.calendarData.eventEndDate,
                    allDay: true,
                    className: $scope.activeTagColor

                },true ); //Stick the event

                $scope.activeState = -1;
                $scope.calendarData.eventName = '';     
                $modalInstance.close();
            }
        }
        
        //Dismiss 
        $scope.eventDismiss = function() {
            $modalInstance.dismiss();
        }
    })

    // =========================================================================
    // COMMON FORMS
    // =========================================================================

    .controller('formCtrl', function(){
    
        //Input Slider
        this.nouisliderValue = 4;
        this.nouisliderFrom = 25;
        this.nouisliderTo = 80;
        this.nouisliderRed = 35;
        this.nouisliderBlue = 90;
        this.nouisliderCyan = 20;
        this.nouisliderAmber = 60;
        this.nouisliderGreen = 75;
    
        //Color Picker
        this.color = '#03A9F4';
        this.color2 = '#8BC34A';
        this.color3 = '#F44336';
        this.color4 = '#FFC107';
    })


    // =========================================================================
    // PHOTO GALLERY
    // =========================================================================

    .controller('photoCtrl', function(){
        
        //Default grid size (2)
        this.photoColumn = 'col-md-2';
        this.photoColumnSize = 2;
    
        this.photoOptions = [
            { value: 2, column: 6 },
            { value: 3, column: 4 },
            { value: 4, column: 3 },
            { value: 1, column: 12 },
        ]
    
        //Change grid
        this.photoGrid = function(size) {
            this.photoColumn = 'col-md-'+size;
            this.photoColumnSize = size;
        }
    
    })


    // =========================================================================
    // ANIMATIONS DEMO
    // =========================================================================
    .controller('animCtrl', function($timeout){
        
        //Animation List
        this.attentionSeekers = [
            { animation: 'bounce', target: 'attentionSeeker' },
            { animation: 'flash', target: 'attentionSeeker' },
            { animation: 'pulse', target: 'attentionSeeker' },
            { animation: 'rubberBand', target: 'attentionSeeker' },
            { animation: 'shake', target: 'attentionSeeker' },
            { animation: 'swing', target: 'attentionSeeker' },
            { animation: 'tada', target: 'attentionSeeker' },
            { animation: 'wobble', target: 'attentionSeeker' }
        ]
        this.flippers = [
            { animation: 'flip', target: 'flippers' },
            { animation: 'flipInX', target: 'flippers' },
            { animation: 'flipInY', target: 'flippers' },
            { animation: 'flipOutX', target: 'flippers' },
            { animation: 'flipOutY', target: 'flippers'  }
        ]
         this.lightSpeed = [
            { animation: 'lightSpeedIn', target: 'lightSpeed' },
            { animation: 'lightSpeedOut', target: 'lightSpeed' }
        ]
        this.special = [
            { animation: 'hinge', target: 'special' },
            { animation: 'rollIn', target: 'special' },
            { animation: 'rollOut', target: 'special' }
        ]
        this.bouncingEntrance = [
            { animation: 'bounceIn', target: 'bouncingEntrance' },
            { animation: 'bounceInDown', target: 'bouncingEntrance' },
            { animation: 'bounceInLeft', target: 'bouncingEntrance' },
            { animation: 'bounceInRight', target: 'bouncingEntrance' },
            { animation: 'bounceInUp', target: 'bouncingEntrance'  }
        ]
        this.bouncingExits = [
            { animation: 'bounceOut', target: 'bouncingExits' },
            { animation: 'bounceOutDown', target: 'bouncingExits' },
            { animation: 'bounceOutLeft', target: 'bouncingExits' },
            { animation: 'bounceOutRight', target: 'bouncingExits' },
            { animation: 'bounceOutUp', target: 'bouncingExits'  }
        ]
        this.rotatingEntrances = [
            { animation: 'rotateIn', target: 'rotatingEntrances' },
            { animation: 'rotateInDownLeft', target: 'rotatingEntrances' },
            { animation: 'rotateInDownRight', target: 'rotatingEntrances' },
            { animation: 'rotateInUpLeft', target: 'rotatingEntrances' },
            { animation: 'rotateInUpRight', target: 'rotatingEntrances'  }
        ]
        this.rotatingExits = [
            { animation: 'rotateOut', target: 'rotatingExits' },
            { animation: 'rotateOutDownLeft', target: 'rotatingExits' },
            { animation: 'rotateOutDownRight', target: 'rotatingExits' },
            { animation: 'rotateOutUpLeft', target: 'rotatingExits' },
            { animation: 'rotateOutUpRight', target: 'rotatingExits'  }
        ]
        this.fadeingEntrances = [
            { animation: 'fadeIn', target: 'fadeingEntrances' },
            { animation: 'fadeInDown', target: 'fadeingEntrances' },
            { animation: 'fadeInDownBig', target: 'fadeingEntrances' },
            { animation: 'fadeInLeft', target: 'fadeingEntrances' },
            { animation: 'fadeInLeftBig', target: 'fadeingEntrances'  },
            { animation: 'fadeInRight', target: 'fadeingEntrances'  },
            { animation: 'fadeInRightBig', target: 'fadeingEntrances'  },
            { animation: 'fadeInUp', target: 'fadeingEntrances'  },
            { animation: 'fadeInBig', target: 'fadeingEntrances'  }
        ]
        this.fadeingExits = [
            { animation: 'fadeOut', target: 'fadeingExits' },
            { animation: 'fadeOutDown', target: 'fadeingExits' },
            { animation: 'fadeOutDownBig', target: 'fadeingExits' },
            { animation: 'fadeOutLeft', target: 'fadeingExits' },
            { animation: 'fadeOutLeftBig', target: 'fadeingExits'  },
            { animation: 'fadeOutRight', target: 'fadeingExits'  },
            { animation: 'fadeOutRightBig', target: 'fadeingExits'  },
            { animation: 'fadeOutUp', target: 'fadeingExits'  },
            { animation: 'fadeOutUpBig', target: 'fadeingExits'  }
        ]
        this.zoomEntrances = [
            { animation: 'zoomIn', target: 'zoomEntrances' },
            { animation: 'zoomInDown', target: 'zoomEntrances' },
            { animation: 'zoomInLeft', target: 'zoomEntrances' },
            { animation: 'zoomInRight', target: 'zoomEntrances' },
            { animation: 'zoomInUp', target: 'zoomEntrances'  }
        ]
        this.zoomExits = [
            { animation: 'zoomOut', target: 'zoomExits' },
            { animation: 'zoomOutDown', target: 'zoomExits' },
            { animation: 'zoomOutLeft', target: 'zoomExits' },
            { animation: 'zoomOutRight', target: 'zoomExits' },
            { animation: 'zoomOutUp', target: 'zoomExits'  }
        ]

        //Animate    
        this.ca = '';
    
        this.setAnimation = function(animation, target) {
            if (animation === "hinge") {
                animationDuration = 2100;
            }
            else {
                animationDuration = 1200;
            }
            
            angular.element('#'+target).addClass(animation);
            
            $timeout(function(){
                angular.element('#'+target).removeClass(animation);
            }, animationDuration);
        }
    })


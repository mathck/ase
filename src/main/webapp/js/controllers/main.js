materialAdmin
    // =========================================================================
    // Base controller for common functions
    // =========================================================================

    .controller('materialadminCtrl', function($timeout, $state, $window, $http, $cookies, $scope, growlService, LogoutFactory, UserFactory, TokenService){
        //Welcome Message

        TokenService.username = $cookies.email;
        TokenService.token = ($cookies.token).valueOf();
        if ( (TokenService.token == '') || (typeof TokenService.token === 'undefined') ){
            $window.location.href='/taskit/index.html';
        }
        TokenService.isLogged=true;
        $http.defaults.headers.common['user-token'] = String(TokenService.token);
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
            console.log("click");
            LogoutFactory.logout({email:TokenService.username});
            TokenService.user={}
            TokenService.token='';
            TokenService.isLogged=false;
            $cookies.email='';
            $cookies.token='';
            growlService.growl('Logging out...');
            $window.location.href='/taskit/index.html';
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
    .controller('headerCtrl', function($timeout, messageService){
    
         // Top Search
        this.openSearch = function(){
            angular.element('#header').addClass('search-toggled');
            //growlService.growl('Welcome back Mallinda Hollaway', 'inverse');
        }

        this.closeSearch = function(){
            angular.element('#header').removeClass('search-toggled');
        }
        
        // Get messages and notification for header
        this.img = messageService.img;
        this.user = messageService.user;
        this.user = messageService.text;

        this.messageResult = messageService.getMessage(this.img, this.user, this.text);


        //Clear Notification
        this.clearNotification = function($event) {
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
        }
        
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

    .controller('profileCtrl', function($scope, growlService, TokenService){
        
        //Get Profile Information from User Service

        //User
        $scope.avatar=TokenService.user.avatar;
        $scope.firstName=TokenService.user.firstName;
        $scope.lastName=TokenService.user.lastName;
        $scope.userID=TokenService.user.userID;

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
                        console.log("Could not log in. Wrong username or password?");
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
                    console.log("Could not log in. Wrong username or password?");
                    console.log(error);
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
    // REWARD CREATION
    //=================================================

    .controller('createRewardCtrl', function (ProjectsFactory, UsersFactory, $scope, $location, $window) {

            // callback for ng-click 'create Project':
        /*console.log("starting");
        $scope.createProject = function () {
            //UserFactory.show("test").then(function(loggedUser){
            var newProject = {
                id: "0",
                title: $scope.project.title,
                description: $scope.project.description,
                userList: []
                //taskList: [],
                //issueList: []
            };
            //newProject.userList.push(loggedUser);

            //console.log(loggedUser);
            console.log(newProject);
            ProjectsFactory.create(newProject);
            $location.path('/home');
        };//};*/
    })

    //=================================================
    // PROJECT CREATION
    //=================================================

    .controller('createProjectCtrl', function ($scope, $location, $window, $state, growlService, ErrorHandler,
        TokenService, ProjectFactory, AddUserToProjectFactory, UsersFactory, RewardsByUserFactory) {

        console.log("starting Project Creation");

        // get all users (for adding users to the project)
        UsersFactory.query().$promise.then(function(response){
            $scope.users={};
            $scope.users.contributableUsers=[];
            $scope.users.userPickerContributor=[];
            $scope.users.userPickerManager=[];

            //add a field containing a readable caption for the users
            response.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.contributableUsers.push(user);
            });

            //create a second list for managers
            $scope.users.managementableUsers=$scope.users.contributableUsers;
        }, function(error){
           ErrorHandler.handle("Could not fetch users from server.", error);
        });

        //Get all created rewards for the current user
        RewardsByUserFactory.query({uID: TokenService.username}).$promise.then(function(rewards){
            $scope.rewardList=rewards;
        }, function(error){
           ErrorHandler.handle("Could not fetch your rewards from server.", error);
        });

        $scope.createProject = function () {
            console.log($scope.users.userPickerContributor);
            ProjectFactory.create({title: $scope.project.title, description: $scope.project.description}).$promise.then(function(response){
                $scope.pID=response.item;

                AddUserToProjectFactory.add({project: $scope.pID, user: TokenService.username, role: "ADMIN"});
                $scope.users.userPickerContributor.forEach(function(contributor){
                    AddUserToProjectFactory.add({project: $scope.pID, user: contributor, role: "CONTRIBUTOR"});
                });
                $scope.users.userPickerManager.forEach(function(manager){
                    AddUserToProjectFactory.add({project: $scope.pID, user: manager, role: "ADMIN"});
                });

                growlService.growl("Project created!")
                $state.go("viewProject",{pID:$scope.pID});

            }, function(error){
               ErrorHandler.handle("Could not save your project.", error);
            });

        };
    })

    //=================================================
    // PROJECT UPDATE
    //=================================================

    .controller('updateProjectCtrl', function ($scope, $stateParams, growlService, ErrorHandler, TokenService,
        ProjectFactory, AddUserToProjectFactory, UserFactory, UsersFactory, RewardsByProjectFactory, RewardsByUserFactory, RemoveUserFromProjectFactory) {

       growlService.growl('Fetching project information...');

       //Set project ID according to parameter
       $scope.currentPID = $stateParams.pID;

       $scope.selectedProject={};
       $scope.selectedProject.userList=[];

        $scope.updateProjectInformation=function(){
            //Get project information
            ProjectFactory.show({pID: $scope.currentPID, uID:TokenService.username}).$promise.then(function(response){0
                $scope.selectedProject=response;
                $scope.selectedProject.userList=[];
                //get user information for all users of the current project
                $scope.selectedProject.allUser.forEach(function(participant){
                    UserFactory.get({uID: participant.user}).$promise.then(function(user){
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
                console.log("Current User Role:" + $scope.currentUserRole);
            }, function(error){
                ErrorHandler.handle("Could not fetch project information from server.", error);
            });
        };
        $scope.updateProjectInformation();

        UsersFactory.query().$promise.then(function(response){
            $scope.users={};
            $scope.users.contributableUsers=[];
            $scope.users.userPickerContributor=[];
            $scope.users.userPickerManager=[];

            //add a field containing a readable caption for the users
            response.forEach(function(user){
                user.name=user.firstName + " " + user.lastName + " (" + user.userID + ")";
                //copy user list into one list for contributors
                $scope.users.contributableUsers.push(user);
            });
            //create a second list for managers
            $scope.users.managementableUsers=$scope.users.contributableUsers;
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
        RewardsByUserFactory.query({uID: TokenService.username}).$promise.then(function(rewards){
            $scope.rewardList=rewards;
        }, function(error){
           ErrorHandler.handle("Could not fetch your rewards from server.", error);
        });



        $scope.deleteUserFromProject=function(userID){
            RemoveUserFromProjectFactory.delete({pID:$scope.currentPID, uID:userID}).$promise.then(function(response){
                $scope.updateProjectInformation();
            }, function(error){
                ErrorHandler.handle("Could not delete user from project.", error);
            });

        }

        //Save changes after button is clicked
        $scope.saveProject=function(){
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
        };
    })

    //=================================================
    // ISSUE CREATION
    //=================================================

    .controller('createIssueCtrl', function ( $scope, $state, $stateParams, $state, growlService, ErrorHandler, IssuePostFactory, ProjectFactory, TokenService) {

        // callback for ng-click 'create Issue':
        console.log("starting issue creation");
        $scope.currentPID = $stateParams.pID;

        ProjectFactory.show({pID: $scope.currentPID, uID: TokenService.username}).$promise.then(function(response){
            $scope.selectedProject=response;
        });


        $scope.createIssue = function () {
            IssuePostFactory.create({pID: $scope.currentPID, uID: TokenService.username}, {title:$scope.issue.title, description:$scope.issue.description}).$promise.then(function(response){
                growlService.growl("Issue created.");
                $state.go("viewProject", {pID:$scope.currentPID});
            }, function(error){
                ErrorHandler.handle("Could not create Issue.", error);
            });
        };
    })

    //=================================================
    // ISSUE TRANSFORMATION
    //=================================================

    .controller('viewIssue', function ($scope, $state, $stateParams, $state, growlService, ErrorHandler, IssueRetrieveFactory, ProjectFactory, TokenService) {
        console.log("starting issue update");
        $scope.currentIID = $stateParams.iID;
        $scope.currentPID = $stateParams.pID;

        IssueRetrieveFactory.show({issueID: $scope.currentIID}).$promise.then(function(response){
            response=issue;
        }, function(error){
            ErrorHandler.handle("Could not retrieve Issue Information.", error);
        });

        $scope.discardIssue=function(){
            IssueRetrieveFactory.delete({issueID: $scope.currentIID}).$promise.then(function(response){
                $state.go("viewProject", {pID:$scope.currentPID});
            }, function(error){
                ErrorHandler.handle("Could not delete Issue.", error);
            });
        }

	})

    //=================================================
    // TASK CREATION
    //=================================================

    .controller('createTaskCtrl', function ( $scope, IssuesFactory, $location, $window) {

          var counter = 0;
          $scope.data = {
            stateFields: [],
            templateFields: []
          }

          $scope.states = ['New', 'Open', 'Closed'];
          $scope.templates = ['Template 1', 'Template 2', 'Template 3'];

          $scope.addState = function() {
            $scope.data.stateFields.push({
              name: "test " + counter++
            });
          };
          $scope.addTemplate = function() {
            $scope.data.templateFields.push({
              name: "test " + counter++
            });
          };
          $scope.removeTemplate = function() {
            $scope.data.templateFields.pop();
          };
          $scope.removeState = function() {
            $scope.data.stateFields.pop();
          };
          $scope.createTask = function() {

          };
    })

    //=================================================
    // TASK UPDATE
    //=================================================

    .controller('updateTaskCtrl', function ( $scope, IssuesFactory, $location, $window) {

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


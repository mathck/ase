materialAdmin
    // =========================================================================
    // Base controller for common functions
    // =========================================================================

    .controller('materialadminCtrl', function($timeout, $state, $http, $cookies, $scope, growlService, LoginFactory, UserFactory, TokenService){
        //Welcome Message

        TokenService.username = $cookies.email;
        TokenService.token = ($cookies.token).valueOf();
        TokenService.isLogged=true;
        $http.defaults.headers.common['user-token'] = String(TokenService.token);
        UserFactory.show({uID: TokenService.username}).$promise.then(function(response){
                TokenService.user=response; //set persistent UserInformation
                console.log("Token is:" + TokenService.token);
                growlService.growl('Welcome back ' + TokenService.user.firstName +' :D', 'inverse');

                //initialize Variables for Menubar
                $scope.avatar=TokenService.user.avatar;
                $scope.firstName=TokenService.user.firstName;
                $scope.lastName=TokenService.user.lastName;
                $scope.userID=TokenService.user.userID;
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

        /*
        this.logoutUser() = function () {
            console.log("click");
            LoginFactory.logout(TokenService.user.userID);
            TokenService.user={}
            TokenService.token='';
            TokenService.isLogged=false;
            $cookies.email='';
            $cookies.token='';
            growlService.growl('Logging out...');
            $window.location.href='/taskit/index.html';
        };*/

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

    .controller('loginCtrl', function ($rootScope, $cookies, $window, $q, $http, growlService, LoginFactory, UserRegistrationFactory) {

            $rootScope.avatar="img/avatars/0.png";

            // callback for ng-click 'loginUser':
            this.loginUser = function () {
                 var email = this.login.email;
                 var password = this.login.password;

                 $http({
                    url:"/taskit/api/user/login?email=" + email + "&password=" + password,
                    method:'GET'
                    //transformResponse: undefined
                 })
                 .success(function(token){
                    $http.defaults.headers.common['user-token'] = token.token;
                    console.log("Token is:" + token);
                    $cookies.email=email;
                    $cookies.token=token.token;
                    $window.location.href='/taskit/main.html';

                 })
                 .error(function(response, status){
                    growlService.growl("Login failed. Responses: " + response + "; Status: " + status);
                 });
            };

            // callback for ng-click 'saveUser':
            this.createUser = function () {
                userToRegister={userID: this.user.userID, password: this.user.password,
                    firstName: this.user.firstName, lastName: this.user.lastName, avatar: "img/avatars/0.png"};
                if (userToRegister.password===this.passwordCheck){
                    if(userToRegister.password.length>7){
                        userToRegister.avatar=$rootScope.avatar;
                        //console.log("Root avatar: " + $rootScope.avatar);
                        //console.log("registration is " + userToRegister.userID + userToRegister.password + userToRegister.firstName, + userToRegister.lastName + userToRegister.avatar);

                        UserRegistrationFactory.create(userToRegister).$promise.then(function(response){
                            var loginRegisteredUser = {email: userToRegister.userID, password: userToRegister.password};
                            //growlService.growl("Registration successful, logging in...");
                            LoginFactory.create(loginRegisteredUser).$promise.then(function(token){
                                $http.defaults.headers.common['user-token'] = token.token;
                                $cookies.email=loginRegisteredUser.email;
                                $cookies.token=token.token;
                                $window.location.href='/taskit/main.html';
                            });
                        });
                    }else{
                        //growlService.growl("Password has to be longer than 7 characters. Please try again.");
                        console.log("Password has to be longer than 7 characters. Please try again.");
                    }
                }else{
                    //growlService.growl("Passwords did not match. Please try again.");
                    console.log("Passwords did not match. Please try again.");
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

    .controller('mainViewCtrl', function($timeout, $q, $scope, $location, ProjectsFactory, TokenService, AdminProjectsFactory){
        //ProjectsFactory.query({uID: TokenService.user.userID}).$promise.then(function(response){ //TODO - change after project creation works
        AdminProjectsFactory.query().$promise.then(function(response){
            $scope.userProjects=response;
            /*$scope.userProjects.forEach(function(entry){
                console.log(entry.projectID);
            });*/
        });

        $scope.viewProject=function(currentID){
            console.log("Click on viewProject with ID " + currentID);
            //state.go("viewProject", {pID: currentID});
        }

        $scope.createIssueForProject=function(currentID){
            console.log("Click on create Issue for Project with ID " + currentID);
        }


        $scope.createTaskForProject=function(currentID){
            console.log("Click on Create Task for Project with ID " + currentID);
        }

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

    .controller('createProjectCtrl', function (TokenService, ProjectsFactory, UsersFactory, $scope, $location, $window) {

        // callback for ng-click 'create Project':
        console.log("starting Project Creation");

        UsersFactory.query().$promise.then(function(response){
            /*consoleLog(response);
            consoleLog(TokenService.user);
            arrayRemove(response, TokenService.user);
            console.log(response);*/
            this.contributableUsers=response;
            this.managementableUsers=this.contributableUsers;

            /*$scope.managementableUsers.forEach(function(entry){
                console.log(entry);
            });*/
        });

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
        };//};
    })

    //=================================================
    // PROJECT UPDATE
    //=================================================

    .controller('updateProjectCtrl', function ($scope, $stateParams, growlService, TokenService, ProjectFactory, UserFactory, RewardsByProjectFactory) {

       growlService.growl('Fetching project information...');

       //Set project ID according to parameter
       $scope.currentPID = $stateParams.pID;
       console.log("pid:" + $scope.currentPID);

       $scope.selectedProject={};

       //Get project information
       ProjectFactory.show({pID: $scope.currentPID}).$promise.then(function(response){
            $scope.selectedProject=response;
            console.log(response);

            //get user information for all users of the current project
            $scope.selectedProject.userList=[];
            console.log("getting users...");
            $scope.selectedProject.allUser.forEach(function(participant){
                console.log("getting user: " + participant.user);
                UserFactory.get({uID: participant.user}).$promise.then(function(user){
                    console.log("setting role to: " + participant.role);
                    //add role of the current user for the project
                    user.role=participant.role;
                    console.log(user);
                    $scope.selectedProject.userList.push(user);
                });
                console.log("Resulting user list:");
                console.log($scope.selectedProject.userList);
            });
        });

        //Get all rewards for the current project and user
        console.log("getting rewards... for "+ TokenService.username);
        RewardsByProjectFactory.query({pID: $scope.currentPID, uID: TokenService.username}).$promise.then(function(rewards){
            $scope.selectedProject.rewards=rewards;
            console.log("rewards:");
            console.log($scope.selectedProject.rewards);
        });

        //Save changes after button is clicked
        $scope.saveProject=function(){
            ProjectFactory.update({pID: $scope.currentPID}, {
                title: $scope.selectedProject.title,
                description: $scope.selectedProject.description
            });
        };
    })

    //=================================================
    // ISSUE CREATION
    //=================================================

    .controller('createIssueCtrl', function ( $scope, IssuesFactory, $location, $window) {

            // callback for ng-click 'create Issue':
        console.log("starting");
        $scope.createIssue = function () {
            //UserFactory.show("test").then(function(loggedUser){
            var newIssue = {
                id: "1",
                title: "hans",
                description: "test",
                taskType: "blah",
                creationDate: "2015-12-31",
                updateDate: "2016-01-12",
                dslTemplateId: "1",
                projectId: "1",
                userMail:"test@test.com",
                status:"leiwand"
            };

            console.log(newIssue);
            console.log("still here");
            IssuesFactory.create(newIssue);
            $location.path('/home');
            console.log("and here");
        };
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


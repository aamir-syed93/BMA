'use strict';

var controllers = angular.module('UserModule');

controllers.controller('UserModule_AdminCtrl' , ['$scope','$rootScope','$location','$route','GlobalModule_dataStoreService','UserModule_loginService','GlobalModule_notificationService','GlobalModule_helperService', function ($scope, $rootScope,$location,$route,GlobalModule_dataStoreService,UserModule_loginService,GlobalModule_notificationService,GlobalModule_helperService) {
	
	$scope.userId=GlobalModule_dataStoreService.loadData('UserModule','userId');
	$rootScope.userId=GlobalModule_dataStoreService.loadData('UserModule','userId');
	$scope.userdetail=GlobalModule_dataStoreService.loadData('UserModule','userdetail');
	$rootScope.loggedIn=GlobalModule_dataStoreService.loadData('UserModule','loggedIn');
	$rootScope.roleName=GlobalModule_dataStoreService.loadData('UserModule','roleName');
	$scope.totalFileSize=GlobalModule_dataStoreService.loadData('EdocumentModule','totalFileSize');
	
	$scope.registeredUserDetails=GlobalModule_dataStoreService.loadData('UserModule','registeredUserDetails');
	
	$scope.userRegistrationByAdmin = function(user){
		$scope.totaledoccount=GlobalModule_dataStoreService.loadData('UserModule','totaledoccount');
		$scope.edocument=GlobalModule_dataStoreService.loadData('UserModule','edocument');
		
			
		
		
		$(".loader").show();
		user.createdBy=$scope.userdetail.id;
		UserModule_loginService.userRegistration(user).then(function(response){
			$scope.registeredUserDetails = response.data;
			
			console.log("userRegistrationByAdmin");
			console.log($scope.registeredUserDetails);
			
			
			$rootScope.userId=$scope.registeredUserDetails.id;
			GlobalModule_dataStoreService.storeData('UserModule','registeredUserDetails',$scope.registeredUserDetails);

			console.log($scope.registeredUserDetails);
			GlobalModule_notificationService.notification("success", "OTP has been sent to "+user.mail+" Please enter OTP to Login.");
			$(".loader").fadeOut("slow");
			$location.path('/otpOfUser');
		},function(response){
			//Error			
			GlobalModule_notificationService.notification("error", "Username already exists!");
			$(".loader").fadeOut("slow");

		});
	};
	
	//admin
	$scope.checkOtp = function(user){
		user.mail=$scope.registeredUserDetails.mail;
		$(".loader").show();
		UserModule_loginService.checkOtp(user).then(function(response){
			
			$scope.otpFlag = response.data.active;
			if($scope.otpFlag == true){

				GlobalModule_notificationService.notification("success","Registration successful!");
				
				$rootScope.loggedIn=true;
				GlobalModule_dataStoreService.storeData('UserModule','loggedIn',$rootScope.loggedIn);
				$(".loader").fadeOut("slow");
				$location.path('/adminpanel');
				
			}else{
				user.otp="";
				GlobalModule_notificationService.notification("error", "Please enter correct OTP!");
				$location.path('/enterotp');
				$(".loader").fadeOut("slow");
			}
			
		},function(response){
			//Error			
		//	GlobalModule_notificationService.notification("error", "Error in Sign Up");

		});
	};
}]);
	
controllers.controller('UserModule_LoginCtrl' , ['$compile','$timeout','$scope','$rootScope','$location','$route','GlobalModule_dataStoreService','UserModule_loginService','ComplainModule_ComplainService','GlobalModule_notificationService','GlobalModule_helperService', function ($compile,$timeout,$scope, $rootScope,$location,$route,GlobalModule_dataStoreService,UserModule_loginService,ComplainModule_ComplainService,GlobalModule_notificationService,GlobalModule_helperService) {
	
	$scope.userId=GlobalModule_dataStoreService.loadData('UserModule','userId');
	$rootScope.userId=GlobalModule_dataStoreService.loadData('UserModule','userId');
	$scope.userdetail=GlobalModule_dataStoreService.loadData('UserModule','userdetail');
	$rootScope.loggedIn=GlobalModule_dataStoreService.loadData('UserModule','loggedIn');
	$scope.userMobNo=GlobalModule_dataStoreService.loadData('UserModule','userMobNo');
	$rootScope.roleName=GlobalModule_dataStoreService.loadData('UserModule','roleName');
	$rootScope.roleActionList=GlobalModule_dataStoreService.loadData('UserModule','roleActionList');
$scope.useremail=GlobalModule_dataStoreService.loadData('UserModule','useremail');
	$rootScope.showProducts = GlobalModule_dataStoreService.loadData('ComplainModule','showProducts');
	$scope.showPass=false;
	$scope.useridentifier=GlobalModule_dataStoreService.loadData('UserModule','useridentifier');
	
	
	/*$scope.checkIfLogin = function() {
		if($rootScope.userId==undefined){
			$location.path('/landingPage');
			console.log($rootScope.userId);
			if($location.path() != '/forgotpassword' || $location.path() != '/landingPage'){
				$location.path('/showLoginPage');
			}
		}
	};
	
	
	
	$rootScope.$on('$locationChangeStart', $scope.checkIfLogin);
	$scope.checkIfLogin();*/
	$scope.regComplain=function()
	{
		$location.path('/complain');
		$route.reload();
	};
	
	
	
	$scope.generateLockerOTP = function(){
		
		$(".loader").show();
		//alert("OTP Gene :"+$scope.userdetail);
		console.log($scope.roleActionList);
		console.log($scope.userdetail);
		$scope.user={};
		$scope.user.mail=$scope.userdetail.mail;
		$scope.user.id=$scope.userdetail.id;
		
		
		UserModule_loginService.generateLockerOTP($scope.user).then(function(response){
			$scope.lockerType = response.data;
			
			GlobalModule_dataStoreService.storeData('UserModule','pageFrom',undefined);  
			GlobalModule_notificationService.notification("success", "OTP sent. Please enter OTP to access the locker!");
			$location.path('/lockerotp');
			
		},function(response){
			//Error			
			GlobalModule_notificationService.notification("error", "Error in OTP generation! Please try again.");
			
		});
		$(".loader").fadeOut("slow");
		
	};
	
	$scope.cancelButtonPass = function(){
		$location.path('/userinfo');
	};
	

	
	
	$scope.checkPassword=function(str)
	{
		 $("#error").css("display", "block");
		 $("#error").show();
		 
		//alert(str);
	  // at least one number, one lowercase and one uppercase letter
	  // at least six characters
	  var re = /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,}/;
	  if(!re.test(str)) {
	        alert("Error: password doesn't match requirements!");
	        $scope.user.password='';
	        $('#pass').focus();
	       
	        return false;
	      }
	};
	
	$scope.feedbackCancel=function()
	{
		
		$scope.feedback="";
	};
	
	$scope.rating = 0;
	    $scope.ratings = [ {
	        current: 0,
	        max: 5,
	        
	    }];
	    
	    $scope.rating;
	    $scope.getSelectedRating = function (rating) {
	    	
	 
	    	$scope.rating=rating;
	    };
	
	    $scope.feedback={};
	$scope.userfeedback=function(feedback)
	{
		
		
		
		
		if($scope.rating==0)
		{
			$scope.rating=5;
		}
		
		 $scope.feedback.rate=$scope.rating;
		 $scope.feedback.experience=feedback.experience;
	
		$(".loader").show();
		//GlobalModule_notificationService.notification("success", "Thank You for your feedback");
		
		console.log("feedback ");
		console.log(feedback);
		UserModule_loginService.userfeedback($scope.feedback).then(function(response){
		GlobalModule_notificationService.notification("success", "Thank You for your feedback");
			
		
		},function(response){
			//Error			

		});			
		$(".loader").fadeOut("slow");

	};


	$scope.userRegistration = function(user){
		
			

		    
		
		$(".loader").show();
		user.createdBy=1;
		//user=GlobalModule_helperService.preLoadObject(user);
		UserModule_loginService.userRegistration(user).then(function(response){
			$scope.userDetails = response.data;
			console.log($scope.userDetails);
			$rootScope.userId=$scope.userDetails.id;
			GlobalModule_dataStoreService.storeData('UserModule','userId',$rootScope.userId);
			GlobalModule_dataStoreService.storeData('UserModule','userdetail',$scope.userDetails);

			$scope.useridentifier=undefined;
			$scope.useridentifier=user.mail;
			GlobalModule_dataStoreService.storeData('UserModule','useridentifier',$scope.useridentifier);

			GlobalModule_notificationService.notification("success", "OTP has been sent to "+user.mail+" Please enter OTP to Login");
			$(".loader").fadeOut("slow");
			$location.path('/enterotp');
		},function(response){
			//Error			
			GlobalModule_notificationService.notification("error", "Username already exists!");
			$(".loader").fadeOut("slow");

		});
	};

	
	
	$scope.checkOtp = function(user){
		
		//alert("in check my otp");
		console.log("user part check otp");
		console.log(user);
		
		user.useridentifier=$scope.useridentifier;
		
		console.log("in check otp user module login ctrl");
		console.log($scope.userdetail);
		$(".loader").show();

		console.log($scope.userdetail);
		
		
		if($scope.userdetail!=undefined)
		{
			if($scope.userdetail.userRoleObj!=undefined)
			{
				console.log("customer self registering");
				console.log($scope.userdetail);
				user.mail=$scope.userdetail.mail;
				user.otp=$scope.user.otp;
			}
		
			if($scope.userdetail.roleObj!=undefined)
			{
			
					if($scope.userdetail.roleObj.roleName=="ROLE_ADMIN")
					{
							console.log("else");
							console.log("admin registering");
			//user reg by admin
							console.log($scope.userdetail);
							user.mail=$scope.useremail;
			
					}
			}
		}
	
		
		console.log("check otp user object is");
		console.log(user);
		user.roleObj={};
		
		
		UserModule_loginService.checkOtp(user).then(function(response){
		
			$scope.userdetail=response.data;
			
			GlobalModule_dataStoreService.storeData('UserModule','userdetail',$scope.userdetail);  
			

			if($scope.userMobNo!=undefined)
			{
				user.mobNo=$scope.userMobNo;

				$scope.changeMobileNo(user);
				$scope.userMobNo=GlobalModule_dataStoreService.loadData('UserModule','userMobNo',undefined);
				
			}	
			console.log("in check otp");
			console.log(response.data);
			
			$scope.otpFlag = response.data.active;
			
			if($scope.otpFlag == true){

				
				
				if($scope.userdetail.roleObj.id==2)
				{
					$location.path('/dashboard');
				}
				else
				{
					$location.path('/adminpanel');
				}
				
				
				
				$rootScope.loggedIn=true;
				GlobalModule_dataStoreService.storeData('UserModule','loggedIn',$rootScope.loggedIn);
				
				//for menu
				$rootScope.roleName= response.data.roleObj.roleName;
				GlobalModule_dataStoreService.storeData('UserModule','roleName',response.data.roleObj.roleName);
				//$rootScope.roleName="ROLE_CUSTOMER";
				$scope.userdetail.actionList={};
				angular.forEach($scope.userdetail.roleObj.roleActions, function(action) {
					$scope.userdetail.actionList[action.name]=action.active;
				});
				$rootScope.roleActionList=$scope.userdetail.actionList;
				GlobalModule_dataStoreService.storeData('UserModule','roleActionList',$scope.userdetail.actionList);

				
				
				$(".loader").fadeOut("slow");
			}else{
				user.otp="";
				GlobalModule_notificationService.notification("error", "Plaese Enter Correct OTP.");
				$location.path('/enterotp');
				$(".loader").fadeOut("slow");
			}
			
		},function(response){
			//Error			
			//alert("error");
		//	GlobalModule_notificationService.notification("error", "Error in Sign Up");

		});
	};

	$scope.loginUser = function(user){
		
		
		$(".loader").show();
		UserModule_loginService.loginUser(user).then(function(response){
		
			$scope.makeResponsiveMenu();
			$scope.userdetail = response.data;
			//$rootScope.userId=$scope.userdetail.id;
			GlobalModule_dataStoreService.storeData('UserModule','userdetail',$scope.userdetail);  
			
		console.log("right");
		
		//$scope.changeflag = GlobalModule_dataStoreService.loadData('UserModule','firstloginflag');
		$rootScope.userId=$scope.userdetail.id;
		GlobalModule_dataStoreService.storeData('UserModule','userId',$rootScope.userId);
		if($scope.userdetail.passReset == 'true')
			{
				//alert("Hello for change"  +$scope.changeflag);
			$rootScope.loggedIn=true;
			GlobalModule_dataStoreService.storeData('UserModule','loggedIn',$rootScope.loggedIn);
			//	GlobalModule_dataStoreService.storeData('UserModule','changeFlag',false);
				$location.path('/changepassword');
				$(".loader").fadeOut("slow");
			}
		else{		
			
		$rootScope.roleName=$scope.userdetail.roleObj.roleName;
		GlobalModule_dataStoreService.storeData('UserModule','roleName',$rootScope.roleName);
		
		console.log(response.data);
			$scope.user=undefined;
			
			$scope.userdetail.actionList={};
			angular.forEach($scope.userdetail.roleObj.roleActions, function(action) {
				$scope.userdetail.actionList[action.name]=action.active;
			});
			$rootScope.roleActionList=$scope.userdetail.actionList;
			GlobalModule_dataStoreService.storeData('UserModule','roleActionList',$scope.userdetail.actionList);

			
			
		
			console.log($scope.userdetail);
			//startchat($scope.userdetail.mail,$scope.userdetail.username,$scope.userdetail.username);
			//worker($scope.userdetail.id);
			if($scope.userdetail.roleObj.roleName =="ROLE_CUSTOMER"){
				//startchat($scope.userdetail.mail,$scope.userdetail.username,$scope.userdetail.username);
			}else{
				//workerRemedy($scope.userdetail.id);
			}
			
			
			if($scope.userdetail.roleObj.roleName=='ROLE_CUSTOMER'){
				if($rootScope.showProducts){
					$location.path('/productoption');
					$rootScope.showProducts=false;
				}else{
					$location.path('/dashboard');
				}
			}else{
				$location.path('/adminpanel');
			}
		
			
			
			
			
			$rootScope.loggedIn=true;
			GlobalModule_dataStoreService.storeData('UserModule','loggedIn',$rootScope.loggedIn);
		 	GlobalModule_dataStoreService.storeData('UserModule','userdetail',$scope.userdetail);
		 	
		 
			
		 
		}
		/*	
			console.log($scope.userdetail.authenticateChangePass);
			if($scope.userdetail.authenticateChangePass=="false"){
				$location.path('/changepassword');
				console.log($scope.userdetail.authenticateChangePass);
			}else{
				$location.path('/dashboard');
			}*/
		},function(response){
			//Error	
			
			 if(response.status == 400){
				//GlobalModule_notificationService.notification("error", "Please enter correct email ID/username and password!");
				 $scope.error="Please enter correct email ID/username and password!";
			}
			 else if(response.status == 401){
					GlobalModule_notificationService.notification("error", "Enter OTP for Varification");
					GlobalModule_dataStoreService.storeData('UserModule','useremail',$scope.user.useridentifier);
					GlobalModule_dataStoreService.storeData('UserModule','useridentifier',$scope.user.useridentifier);
					
					$location.path('/enterotp');
				}else{
					//GlobalModule_notificationService.notification("error", "Please enter correct email ID/username and password!");
					$scope.error="Please enter correct email ID/username and password!";
				}
			
		});
		$(".loader").fadeOut("slow");
	};
	$scope.error = undefined;
	$scope.makeResponsiveMenu = function(){

		$timeout(function(){
			$("nav#main_menu select").html("");
		//making menu for responsive screen
		$("nav#main_menu ul:not(.ng-hide) li a").each(function() {
			var el = $(this);

			console.log("checking data");
			console.log(el);

			$("<option />", {
				"value"   : el.attr("href"),
				"text"    : el.text(),
				"ng-click" : el.attr("ng-click")
			}).appendTo("nav#main_menu select");
			
		});
		console.log($("nav#main_menu select"));
		//$compile( $("nav#main_menu select").html() )($scope);

		$("nav#main_manu ul").hide();},5000);


	};
	
	
	$scope.makeResponsiveMenu();
	
		
	
	
	
	$scope.forgotPassword = function(userEmail){
		//user.id=$scope.userId;
		  $(".loader").show();
		UserModule_loginService.forgotPassword(userEmail).then(function(response){
			$scope.userdetail = response.data;
			$scope.showPass=true;
			GlobalModule_notificationService.notification("success", "Please check your registered email to reset the password!");
			GlobalModule_dataStoreService.storeData('UserModule','firstloginflag',"change");
			$location.path('/showLoginPage');
		},function(response){
			//Error			
			GlobalModule_notificationService.notification("error", "Please enter correct email ID!");
			$location.path('/forgotpassword');
		});	
		 $(".loader").fadeOut("slow");
	};
	
		$scope.changePassword = function(user){
			
			if(user.password==user.newPassword)
				{
				GlobalModule_notificationService.notification("error", "Your new Password should not be same as old password");
				  $(".loader").fadeOut("slow");
				return false;
				}
			
			
			  $(".loader").show();
			user.id=GlobalModule_dataStoreService.loadData('UserModule','userId');
			user.username=$scope.userdetail.username;
			user.mail=$scope.userdetail.mail;
			user.mobNo=$scope.userdetail.mobNo;
			user.passReset='false';
			
				
			UserModule_loginService.changePassword(user).then(function(response){
				  $(".loader").fadeOut("slow");
				  
				  
				  if(response.data.active==true)
				{
					  GlobalModule_notificationService.notification("success", "Please check your registered email to reset the password!");

						$location.path('/dashboard');
				}
				 else
				 {
					 GlobalModule_notificationService.notification("error", "Existing password does not match! Please try again.");
						
				 }
				  
			},function(response){
				//Error			
				  $(".loader").fadeOut("slow");
				GlobalModule_notificationService.notification("error", "Error in Action change Password");
				$location.path('/landingPage');
			});
		};
		 
		$scope.changeMobileNo = function(user){
		    $(".loader").show();
			user.id=$scope.userId;
			console.log(user);
			UserModule_loginService.changeMobileNo(user).then(function(response){
				$(".loader").fadeOut("slow");
				GlobalModule_notificationService.notification("success", "Mobile Number Updated.");
				$location.path('/dashboard');
			},function(response){
				//Error			
				 $(".loader").fadeOut("slow");
				GlobalModule_notificationService.notification("error", "Error in Mobile Number Update");
				$location.path('/dashboard');
			});
		};
		$scope.cancelButton = function(){
			$location.path('/landingPage');
		};
		
		$scope.cancelButtonOtp = function(){
			$location.path('/landingPage');
		};
		
		$scope.cancelLogin = function(){
			$scope.user="";
		};
		
		$scope.generateOTP = function(user){
			UserModule_loginService.generateOTP(user).then(function(response){
				$scope.userdetail = response.data;
				GlobalModule_notificationService.notification("success", "Please Check Mail To Login With New OTP.");
				$location.path('/landingPage');	
			},function(response){
				//Error			
				GlobalModule_notificationService.notification("error", "Error in OTP generation! Please try again.");
				
			});
		};
		
		$scope.regenerateOTP = function(user){
			UserModule_loginService.regenerateOTP(user).then(function(response){
				$scope.userdetail = response.data;
				GlobalModule_notificationService.notification("success", "Please Check Mail To Login With New OTP.");
				$location.path('/enterotp');
				$route.reload();
			},function(response){
				//Error			
				GlobalModule_notificationService.notification("error", "Error in OTP generation! Please try again.");
				
			});
		};
		
		
}]);

controllers.controller('UserModule_DashboardCtrl' , ['$scope','$rootScope','GlobalModule_dataStoreService','$location','UserModule_loginService','ComplainModule_ComplainService','EDocumentModule_DocumentService','GlobalModule_notificationService','$route','ComplainModule_ComplainHandlerService','CommonModule_helperService', function ($scope,$rootScope,GlobalModule_dataStoreService,$location,UserModule_loginService,ComplainModule_ComplainService,EDocumentModule_DocumentService,GlobalModule_notificationService,$route,ComplainModule_ComplainHandlerService,CommonModule_helperService) {
	
	
	$scope.userId=GlobalModule_dataStoreService.loadData('UserModule','userId');
	$scope.complain=GlobalModule_dataStoreService.loadData('ComplainModule','complain');
	$scope.userdetail=GlobalModule_dataStoreService.loadData('UserModule','userdetail');
	$rootScope.loggedIn=GlobalModule_dataStoreService.loadData('UserModule','loggedIn');
	$scope.comaplinAddMode=GlobalModule_dataStoreService.loadData('ComplainModule','comaplinAddMode');
	$scope.totalFileSize=GlobalModule_dataStoreService.loadData('EdocumentModule','totalFileSize');
	$scope.pagefrom = "dashboard";
	
	
	$scope.Math = window.Math;
	
	$scope.filteredComplainsList=[];
	$scope.categorytypein;
	$scope.sortOn;
	$scope.sortOrderAsc;
	$scope.fromDate;
	$scope.toDate;
	$scope.category=[];
	$scope.userEdocumentCategory=[];
	$scope.lockerlist=[];
	$scope.subCat=[];
	$scope.userEdocumentSubCat=[];
	$scope.product=[];
	$scope.userEdocumentProduct=[];

	$scope.userdetail=GlobalModule_dataStoreService.loadData('UserModule','userdetail');
	$scope.status=[];
	$scope.userEdocumentStatus=[];

	$scope.level=[];
	$scope.userEdocumentLevel=[];

	$scope.type=[];
	$scope.userEdocumentType=[];

	var complainTypeDefaultAll = {"id":0,"name":"All"};
	
	var productDefaultAll = {"id":0,"optionName":"All"};
	var categoryDefaultAll = {"id":0,"name":"All"};
	var categoryDefaultOther = {"id":undefined,"name":"Other"};
	var statusDefaultAll = {"id":0,"name":"All","customerStatus":"All"};
	var subCategoryDefaultAll = {"id":0,"name":"All"};
	//var productDefaultAll = {"id":0,"name":"All"};
	//var levelDefaultAll = {"id":0,"name":"All"};
	
	$scope.fetchComplainsOfUser = function(offset,limit,categorytypein,typeId,categoryId,subCatId,productId,statusId,fromDate,toDate){

		
		
		$scope.pageSrNo=offset;
		
		if(categorytypein=="")
		{
			categorytypein=undefined;
		}
		
		ComplainModule_ComplainService.fetchComplainsOfUser($scope.userId,offset,limit,categorytypein,typeId,categoryId,subCatId,productId,statusId,$scope.sortOn,$scope.sortOrderAsc,fromDate,toDate).then(function(response){
			$scope.complainsUserList = response.data;
			
			console.log("complainsUserList  ");
			console.log($scope.complainsUserList);
			$scope.fetchComplainsOfUserSize($scope.userId,offset,categorytypein,typeId,categoryId,subCatId,productId,statusId,fromDate,toDate);
			
		
			
			
	  	},function(response){
			//Error			
		});
	};
	$scope.fetchComplainsOfUser(0, 5, undefined,0, 0, 0, 0, 0, 0, 0);
		
	
	$scope.cancelComplainFilter=function()
	{
		$scope.categorytypein="";
		
		$scope.type.id=$scope.complainTypeList[0].id;
		$scope.category.id=$scope.categoryList[0].id;
		$scope.subCat.id=$scope.subCategoryList[0].id;
		$scope.product.id=$scope.productTypeList[0].id;
		$scope.status.id=$scope.statusList[0].id;
		$scope.fromDate=undefined;
		$scope.toDate=undefined;
		$scope.fetchComplainsOfUser(0, 5, undefined,0, 0, 0, 0, 0, 0, 0);
	};
	
	$scope.cancelEdocFilter=function()
	{
		$scope.categorytypein="";
		$scope.lockerlist.id=0;
		$scope.fetchEdocumentsOfUser(0,10,undefined,0);
	};
	
	$scope.generateNewEdocOTP=function()
	{
		
		$(".loader").show();
			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
				$scope.uploadEdoc = response.data;
				
				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',"upload");  
				GlobalModule_notificationService.notification("success", "OTP sent. Please enter it to upload the documents!");
				$location.path('/lockerotp');
				
			},function(response){
				//Error			
				GlobalModule_notificationService.notification("error", "Error generating OTP");
				
			});
			$(".loader").fadeOut("slow");
			
		
	};
	
	
	


		$scope.fetchStatus = function(){
			ComplainModule_ComplainHandlerService.fetchStatus().then(function(response){
				$scope.statusList = response.data;
				
				$scope.statusList.unshift(statusDefaultAll);

				console.log("status list");
				console.log($scope.statusList);
				
				$scope.status.id=$scope.statusList[0].id;
				$scope.userEdocumentStatus.id=$scope.statusList[0].id;
				
			},function(response){
				//Error			
			
			});
		};
		$scope.fetchStatus();
		
	$scope.fetchComplainsOfUserSize=function(userId,offset,categorytypein,typeId,categoryId,subCatId,productId,statusId,fromDate,toDate)
	{
		ComplainModule_ComplainService.fetchComplainsOfUserSize(userId,categorytypein,typeId,categoryId,subCatId,productId,statusId,fromDate,toDate).then(function(response){
			//Success
			
			$scope.paginationSelfButtonArray = CommonModule_helperService.forcomplaintofuser(response.data,offset,'#selfComplainTable');
			
			
		},function(response){
			//Error			
		});
	};
	
	
	
		$scope.sortResult = function(sort){
			
			
			$scope.sortOn = sort;

			$scope.fetchComplainsOfUser(0, 5,$scope.categorytypein, $scope.type.id, $scope.category.id, $scope.subCat.id, $scope.product.id, $scope.status.id, $scope.fromDate, $scope.toDate);
		};
		
		
		$scope.sortEdocResult = function(sort){
			
			$scope.sortOn = sort;
			 $scope.fetchEdocumentsOfUser(0,10,$scope.categorytypein,$scope.lockerlist.id);
				
			
		};
		
		/*	filter dropdown*/
		$scope.fetchComplainType = function(){
			ComplainModule_ComplainService.fetchComplainType(true).then(function(response){
				$scope.complainTypeList = response.data;	
				$scope.complainTypeList.unshift(complainTypeDefaultAll);
				
				$scope.type.id=$scope.complainTypeList[0].id; //0
			$scope.userEdocumentType.id=$scope.complainTypeList[0].id;	
		  	},function(response){
				//Error			
			
			});
		};
		$scope.fetchComplainType();
		$scope.fetchLockerType = function(){
			ComplainModule_ComplainService.fetchComplainType(false).then(function(response){
				$scope.LockerTypeList = response.data;	
				$scope.LockerTypeList.unshift(complainTypeDefaultAll);
				
				console.log("locker type list ");
				console.log($scope.LockerTypeList);
				$scope.lockerlist.id=$scope.LockerTypeList[0].id; //0
		  	},function(response){
				//Error			
			
			});
		};
		$scope.fetchLockerType();
		
		
		$scope.fetchCategory = function(complainTypeId){
		
			ComplainModule_ComplainService.fetchCategory(complainTypeId).then(function(response){
				$scope.categoryList = response.data;	

				$scope.categoryList.unshift(categoryDefaultOther);
				
				$scope.categoryList.unshift(categoryDefaultAll);
				
				$scope.category.id =$scope.categoryList[0].id;
				
  				$scope.userEdocumentCategory.id=$scope.categoryList[0].id; //0
				
			},function(response){
				//Error			
			
			});
		};
		$scope.fetchCategory(0);
		
		//set subCat at load :subCat dependent in cat so
		$scope.subCategoryList=[];
		$scope.subCategoryList.push(subCategoryDefaultAll);
		$scope.subCat.id=$scope.subCategoryList[0].id;
		$scope.userEdocumentSubCat.id=$scope.subCategoryList[0].id;
		
		$scope.fetchSubCategory = function(categoryId){
			ComplainModule_ComplainService.fetchSubCategory(categoryId).then(function(response){
				$scope.subCategoryList = response.data;
			
				$scope.subCategoryList.unshift(subCategoryDefaultAll);
				$scope.subCat.id=$scope.subCategoryList[0].id;
				$scope.userEdocumentSubCat.id=$scope.subCategoryList[0].id; //0
				
				
		  
			},function(response){
				//Error			
			
			});
		};
		//$scope.fetchSubCategory(0);
		
		$scope.fetchStatus = function(){
			ComplainModule_ComplainHandlerService.fetchStatus().then(function(response){
				$scope.statusList = response.data;
				$scope.statusList.unshift(statusDefaultAll);
				$scope.status.id=$scope.statusList[0].id;
			  	
				$scope.userEdocumentStatus.id=$scope.statusList[0].id;
			},function(response){
				//Error			
			
			});
		};
		$scope.fetchStatus();
		

		$scope.fetchProductType = function(){
			ComplainModule_ComplainHandlerService.fetchProductType().then(function(response){
				$scope.productTypeList = response.data;	
				$scope.productTypeList.unshift(productDefaultAll);
				
				console.log("all productTypeList");
				console.log($scope.productTypeList);
			    $scope.product.id=$scope.productTypeList[0].id;
			   
			    //$scope.allocatedproduct.id=$scope.productTypeList[0].id;
				 
			    	},function(response){
				//error			
			
			});
		};
		
		$scope.fetchProductType();
		
		$scope.fetchLevel = function(){
			//ComplainModule_ComplainService.fetchLevel().then(function(response){
			//	$scope.levelList = response.data;	
			$scope.levelList=[{"id":0,"name":"All"}]; // need to remove
			$scope.level.id=$scope.levelList[0].id;
				//$scope.levelList.unshift(levelDefaultAll);
		  	//},function(response){
				//Error			
			
			//});
		};
		$scope.fetchLevel();

	
	$scope.comaplinAddMode=false;

	
	
	$scope.convertListIntoCommaSeparatedString = function(list){
		return list.toString();
	};
	
$scope.fetchEdocumentsOfUser = function(offset,limit,categorytypein,edocTypeid){

		$scope.pageSrNo=offset;
		
		
		if(categorytypein=="")
			{
			categorytypein=undefined;
			}
		
		EDocumentModule_DocumentService.fetchEdocumentsOfUser($scope.userId,offset,limit,categorytypein,edocTypeid,$scope.sortOn,$scope.sortOrderAsc).then(function(response){
			$scope.edocumentListOfUser = response.data;

			//getting old file usage
			$scope.totalsize=0;
			for(var i=0;i<$scope.edocumentListOfUser.length;i++)
				{
				$scope.totalsize=$scope.totalsize+$scope.edocumentListOfUser[i].fileSize;
			
				}

			GlobalModule_dataStoreService.storeData('EdocumentModule','totalFileSize',$scope.totalsize);
			
			
			console.log("dashboard ctrl $scope.edocumentListOfUser");
			console.log($scope.edocumentListOfUser);
			$scope.fetchEdocumentsOfUserSize($scope.userId,offset,limit,categorytypein,edocTypeid);
	  	},function(response){
			//Error			
		});
	};
	$scope.fetchEdocumentsOfUser(0,10,undefined,0);
	
	$scope.fetchEdocumentsOfUserSize=function(userId,offset,limit,categorytypein,edocTypeid)
	{
		EDocumentModule_DocumentService.fetchEdocumentsOfUserSize(userId,categorytypein,edocTypeid).then(function(response){
			//Success
			 
			$scope.paginationEdocumentsOfUser = CommonModule_helperService.paginationRenderer(response.data,offset,'#allEdocumentTable');
			
			
			
		},function(response){
			//Error			
		});
		
	};
	
	
	
	$scope.viewComplain = function(complain){
		console.log(complain);
		GlobalModule_dataStoreService.storeData('ComplainModule','complain',complain);
		$location.path('/complain');
	};
	
	$scope.cancelUpload = function(complain){
		  $scope.dialogUpload.dialog( "close" );
	};
	   $scope.openDialogUpload = function(complain){
   	    $scope.complainId=complain.id;
   	    console.log( $scope.complainId);
	  	$scope.dialogUpload.dialog( "open" );
	     }; 
      
      $scope.dialogOptionUpload = {
		        autoOpen: false,
		        height: 300,
		        width: 500,
		        modal: true,
		        close: function() {
		        }
	    };
      
      $scope.addNewComplain = function(){
    	  GlobalModule_dataStoreService.storeData('ComplainModule','complain',undefined);
    	  $location.path('/complain');	
      };
      /*$scope.addnewEdoc = function(){
    	  GlobalModule_dataStoreService.storeData('EdocumentModule','edocument',undefined);
    	  GlobalModule_dataStoreService.storeData('EdocumentModule','editFlag',undefined);
    	  $location.path('/edocument');	
      };*/
      
  	$scope.insertComplaintDoc = function(){
  		//$("form#complainDoc").submit();
  	};
  	
  /*	$scope.deleteEDoc = function(edocument,index) {
  		
  		
  		
		EDocumentModule_DocumentService.deleteEdocument(edocument).then(
				function(response) {
					// Success
					//$scope.edocumentListOfUser[index].active=$scope.edocumentDel.active;
					$scope.fetchEdocumentsOfUser(0,10,undefined,0);
					
				}, function(response) {
					// Error

				});
	};
  	*/
	
	$scope.fetchComplainHistoryById = function(complainId) {
		 GlobalModule_dataStoreService.storeData('ComplainModule','complaintId',complainId);
		 $location.path('/CustomerHistory');
	};
	$scope.requestForChatByUser=function() {
		$scope.messageSession=window.localStorage.getItem("messageSession");
		//alert($scope.messageSession);
		//console.log($scope.messageSession);
		if($scope.messageSession=='undefined'){
			startchat($scope.userdetail.mail,$scope.userdetail.username,$scope.userdetail.username);
		}
		};
  		
  		$("form#complainDoc").submit(function(event){ 
			  //disable the default form submission
			  event.preventDefault();
			  $(".loader").show();
			  //grab all form data  
			  var formData = new FormData($(this)[0]);
			  
			  console.log("form data ");
			  console.log(formData);
			  $.ajax({
			    url: 'services/complain/insertComplainDoc',
			    type: 'POST',
			    data: formData,
			    async: true,
			    cache: false,
			    contentType: false,
			    processData: false,
			    success: function (returndata) {
			        var compalinData=JSON.parse(returndata);
			        console.log(compalinData);
			        $(".loader").fadeOut("slow");
			        $route.reload();
			    	GlobalModule_notificationService.notification("success", "complain document added Successfull.");
			        $scope.dialogUpload.dialog( "close" );
		    		
			    }
			  });
			 
			  return true;
			});
  		
  		$scope.generateLockerOTP = function(){
  		//	GlobalModule_dataStoreService.storeData('UserModule','edocument',edocument); 
  			$(".loader").show();
  			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
  				
  				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',$scope.pagefrom);  
  				GlobalModule_notificationService.notification("success", "OTP sent. Please enter OTP to access the locker!");
  				$location.path('/lockerotp');
  				
  			},function(response){
  				//Error			
  				GlobalModule_notificationService.notification("error", "Error generating OTP");
  				
  			});
  			$(".loader").fadeOut("slow");
  			
  		};
  		
  		
  		$scope.generateDocLockerOTP = function(edocpath,edocname){
  			//alert(edocpath+"   "+edocname);
  			GlobalModule_dataStoreService.storeData('UserModule','edocpath',edocpath); 
  			GlobalModule_dataStoreService.storeData('UserModule','edocname',edocname); 
  	  		//	GlobalModule_dataStoreService.storeData('UserModule','edocument',edocument); 
  	  			$(".loader").show();
  	  			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
  	  				
  	  				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',$scope.pagefrom);  
  	  				GlobalModule_notificationService.notification("success", "OTP sent. Please enter OTP to access the locker!");
  	  				$location.path('/lockerotp');
  	  				
  	  			},function(response){
  	  				//Error			
  	  				GlobalModule_notificationService.notification("error", "Error generating OTP");
  	  				
  	  			});
  	  			$(".loader").fadeOut("slow");
  	  			
  	  		};
  		
  		$scope.generateUpdateLockerOTP = function(edocument){
  	  			GlobalModule_dataStoreService.storeData('UserModule','edocument',edocument); 
  	  			$(".loader").show();
  	  			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
  	  				$scope.UpdateLocker = response.data;
  	  				
  	  				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',"update");  
  	  				GlobalModule_notificationService.notification("success", "OTP sent. Please enter it to update the documents! ");
  	  				$location.path('/lockerotp');
  	  				
  	  			},function(response){
  	  				//Error			
  	  				GlobalModule_notificationService.notification("error", "Error generating OTP");
  	  				
  	  			});
  	  			$(".loader").fadeOut("slow");
  	  			
  	  		};
  		
  	  	$scope.generateDeleteLockerOTP = function(edocument){
	  			GlobalModule_dataStoreService.storeData('UserModule','edocument',edocument); 
	  			$(".loader").show();
	  			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
	  				
	  				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',"delete");  
	  				GlobalModule_notificationService.notification("success", "OTP sent. Please enter it to delete the documents!");
	  				$location.path('/lockerotp');
	  				
	  			},function(response){
	  				//Error			
	  				GlobalModule_notificationService.notification("error", "Error generating OTP");
	  				
	  			});
	  			$(".loader").fadeOut("slow");
	  			
	  		};
  		
  		$scope.generateUploadLockerOTP = function(edocument){
  			
	  			GlobalModule_dataStoreService.storeData('UserModule','edocument',edocument); 
	  			$(".loader").show();
	  		//	alert("OTP Gene :"+$scope.userdetail);
	  			//alert("OTP Gene :"+$scope.userdetail);
	  			UserModule_loginService.generateLockerOTP($scope.userdetail).then(function(response){
	  				$scope.userdetail = response.data;
	  				
	  				GlobalModule_dataStoreService.storeData('UserModule','pageFrom',"upload");  
	  				GlobalModule_notificationService.notification("success", "An OTP is been send to your EmailId For Upload E-document ");
	  				$location.path('/lockerotp');
	  				
	  			},function(response){
	  				//Error			
	  				GlobalModule_notificationService.notification("error", "Error generating OTP");
	  				
	  			});
	  			$(".loader").fadeOut("slow");
	  			
	  		};
}]);

	
controllers.controller('UserModule_LogoutCtrl' , ['$scope','$rootScope','GlobalModule_dataStoreService','$location','$timeout', function ($scope,$rootScope,GlobalModule_dataStoreService,$location,$timeout) {
	
	
	$scope.logout = function() {


		$scope.makeResponsiveMenu = function(){

			$timeout(function(){
				$("nav#main_menu select").html("");
			//making menu for responsive screen
			$("nav#main_menu ul:not(.ng-hide) li a").each(function() {
				var el = $(this);

				console.log("checking data");
				console.log(el);

				$("<option />", {
					"value"   : el.attr("href"),
					"text"    : el.text(),
					"ng-click" : el.attr("ng-click")
				}).appendTo("nav#main_menu select");
				
			});
			console.log($("nav#main_menu select"));
			//$compile( $("nav#main_menu select").html() )($scope);

			$("nav#main_manu ul").hide();},5000);


		};
		
		
		$scope.makeResponsiveMenu();
		
		
		if($rootScope.roleName=='ROLE_CUSTOMER'){
			closeChatOfClient();
		}else{
			closeChatOfAdmin();
		}
		
		$rootScope.showProducts=false;
		$rootScope.username = "";
		GlobalModule_dataStoreService.storeData('UserModule','loggedIn',undefined);
		GlobalModule_dataStoreService.storeData('UserModule','userId',undefined);
		GlobalModule_dataStoreService.storeData('EdocumentModule','currentSessionEdocumentList',undefined);
		$rootScope.loggedIn = false;
		$rootScope.logout = false;
		$rootScope.roleName=undefined;
		GlobalModule_dataStoreService.storeData('ComplainModule','showProducts',undefined);
		GlobalModule_dataStoreService.storeData('UserModule','userdetail',undefined);
		$rootScope.showuser=false;
		$rootScope.userId = undefined;
		
		$rootScope.roleActionList=undefined;
		GlobalModule_dataStoreService.storeData('UserModule','roleActionList',undefined);
		GlobalModule_dataStoreService.storeData('UserModule','useridentifier',undefined);
		
		
		$location.path('/landingPage');	
	};
}]);


	function IsNumeric(evt)
	{
		 var iKeyCode = (evt.which) ? evt.which : evt.keyCode;
				 if (iKeyCode != 46 && iKeyCode > 31 && (iKeyCode < 48 || iKeyCode > 57))
			            return false;
			     return true;
    }

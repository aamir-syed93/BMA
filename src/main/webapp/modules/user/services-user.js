'use strict';

var services = angular.module('UserModule');


services.service('UserModule_loginService',['$rootScope','$location','$http','GlobalModule_dataStoreService', function($rootScope, $location, $http, GlobalModule_dataStoreService) {

	this.userRegistration = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/registration',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
this.userfeedback=function(feedback)
{
	var promise = $http({
		method : 'POST',
		data : feedback,
		url : 'services/user/userfeedback',
		headers : {
			'Content-Type' : 'application/json'
		},
		cache : false
	}).then(function (response) {
        return response;
    });
	return promise;		

};

	
	this.loginUser = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/login',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.forgotPassword = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/forgotpassword',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.changePassword = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/changepassword',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.changeMobileNo = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/changemobileno',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.generateOTP = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/generatenewotp',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.regenerateOTP = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/regeneratenewotp',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.generateLockerOTP = function(user){
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/generatelockerotp',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	
	this.checkOtp = function(user){
		console.log(user);
		var promise = $http({
			method : 'POST',
			data : user,
			url : 'services/user/checkotp',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
	this.fetchcareerlist=function(jobTitle,expRequired)
	{
		var promise = $http({
			method : 'GET',
			url : 'services/user/carrers/'+jobTitle+'/'+expRequired,
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
			return response;
	    });
		return promise;		
	};
	
	this.fetchsearchlist=function(search,userid)
	{
		var promise = $http({
			method : 'GET',
			url : 'services/user/search/'+search+'/'+userid,
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
			return response;
	    });
		return promise;		
	};
	
	
	this.updateLocaleFile = function(filedata){
		var promise = $http({
			method : 'POST',
			data : filedata,
			url : 'services/user/updatelocalefile',
			headers : {
				'Content-Type' : 'application/json'
			},
			cache : false
		}).then(function (response) {
	        return response;
	    });
		return promise;		
	};
	
}]);

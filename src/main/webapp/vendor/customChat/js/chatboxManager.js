// Need this to make IE happy
// see http://soledadpenades.com/2007/05/17/arrayindexof-in-internet-explorer/
if(!Array.indexOf){
    Array.prototype.indexOf = function(obj){
	for(var i=0; i<this.length; i++){
	    if(this[i]==obj){
	        return i;
	    }
	}
	return -1;
    }
}


var chatboxManager = function() {

    // list of all opened boxes
    var boxList = new Array();
    // list of boxes shown on the page
    var showList = new Array();
    // list of first names, for in-page demo
    var nameList = new Array();

    var config = {
	width : 200, //px
	gap : 20,
	maxBoxes : 5,
	messageSent :  function(id, user, msg) {
        $("#log").append(id + " said: " + msg + "<br/>");
        $("#"+id).chatbox("option", "boxManager").addMsg(id, msg);
        
    }
    };

    var init = function(options) {
	$.extend(config, options)
    };


    var delBox = function(idx) {
	// TODO

	
    };

    var getNextOffset = function() {
	return (config.width + config.gap) * showList.length;
    };

    var boxClosedCallback = function(id) {
	// close button in the titlebar is clicked
	var idx = showList.indexOf(id);
	if(idx != -1) {
	    showList.splice(idx, 1);
	    diff = config.width + config.gap;
	    for(var i = idx; i < showList.length; i++) {
		offset = $("#" + showList[i]).chatbox("option", "offset");
		$("#" + showList[i]).chatbox("option", "offset", offset - diff);
	    }
	}
	else {
	  //  alert("should not happen: " + id);
	}
    };

    // caller should guarantee the uniqueness of id
    var addBox = function(id, user, name) {
	var idx1 = showList.indexOf(id);
	var idx2 = boxList.indexOf(id);
	if(idx1 != -1) {
	    // found one in show box, do nothing
	}
	else if(idx2 != -1) {
	    // exists, but hidden
	    // show it and put it back to showList
	    $("#"+id).chatbox("option", "offset", getNextOffset());
	    var manager = $("#"+id).chatbox("option", "boxManager");
	    manager.toggleBox();
	    showList.push(id);
	}
	else{
	    var el = document.createElement('div');
	    el.setAttribute('id', id);
	    $(el).chatbox({id : id,
			   user : user,
			   title : user.first_name + " " + user.last_name,
			   hidden : false,
			   width : config.width,
			   offset : getNextOffset(),
			   messageSent : messageSentCallback,
			   boxClosed : boxClosedCallback
			  });
	    boxList.push(id);
	    showList.push(id);
	    nameList.push(user.first_name);
	}
    };
    
   var msgid = 1;
    
    var messageSentCallback = function(id, user, msg) {
	/*var idx = boxList.indexOf(id);
	config.messageSent(nameList[idx], msg);*/
    	  /*$("#log").append(user.first_name + " said: " + msg + "<br/>");*/
       /*   $("#"+id).chatbox("option", "boxManager").addMsg(user.first_name, msg);*/
    	
    	$("#log").append("you" + " said: " + msg + "<br/>");
           /*$("#"+id).chatbox("option", "boxManager").addMsg("you", msg);*/
           var message ={
        		   messageId : msgid++,
        		   message : msg,
        		   userId : user.userId,
        		   sessionId: user.session 
           };
       	/*$.ajax({
       	 type: "POST",
      	  url: address +'/astagurucrm/rest/Chat/user/sendMessage',
      	 data: message,
      	dataType: "json",
      	  success: function(data){
      		  	console.log(data.messageId);
	      		console.log(data.message);
	      		console.log(data.createdOn);
	      		console.log(data.userId);
      	  },
      	  error: function(){
      	
      	  }
      	}); */
       	
       	$.ajax({
       	    url : servicesURL+'/Chat/user/sendMessage',
       	    type: "POST",
       	    dataType : "JSON", 
       	    contentType: 'application/json',
       	    data : JSON.stringify(message),
       	    success: function(data, textStatus, jqXHR)
       	    {
       	 	console.log(data.messageId);
      		console.log(data.message);
      		console.log(data.createdOn);
      		console.log(data.sessionId);
       	    },
       	    error: function (jqXHR, textStatus, errorThrown)
       	    {
       	 
       	    }
       	});
          
    };

   
    // not used in demo
    var dispatch = function(id, user, msg) {
    	$("#" + id).find('textarea');
	$("#" + id).chatbox("option", "boxManager").addMsg(user.first_name, msg);
    }
    
    var dispatchMsg = function(id, user, msg) {
    	$("#" + id).append("<div  class='ui-chatbox-msg'><I>Help desk is not available right now.Please try again!</I></div>");
    	var test = $("#" + id).next();
    	$("#" + id).next().find('textarea').attr("disabled","true");
    	 var box = $("#" + id);
         box.scrollTop(box.get(0).scrollHeight);
        }

    var dispatchMsgUser = function(id, user, msg) {
    	$("#" + id).append("<div  class='ui-chatbox-msg'><I>User Closed chat!</I></div>");
    	var test = $("#" + id).next();
    	$("#" + id).next().find('textarea').attr("disabled","true");
    	  var box = $("#" + id);
          box.scrollTop(box.get(0).scrollHeight);
        }
    var dispatchUserConnected = function(id, user, msg) {
    	$("#" + id).append("<div class='ui-chatbox-msg' ><I>Client connected! Please wait for client input! </I></div>");
    	var test = $("#" + id).next();
    	 var box = $("#" + id);
         box.scrollTop(box.get(0).scrollHeight);
        }
    return {
	init : init,
	addBox : addBox,
	delBox : delBox,
	dispatch : dispatch,
	dispatchMsg:dispatchMsg,
	dispatchMsgUser:dispatchMsgUser,
	dispatchUserConnected:dispatchUserConnected
    };
}();
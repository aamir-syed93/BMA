chatboxManager.init();

var box = [];
var config = {
		width : 200, //px
		gap : 20,
		maxBoxes : 5,
		messageSent : function(dest, msg) {
			// override this
			$("#" + dest).chatbox("option", "boxManager").addMsg(dest, msg);
		}
};

var getNextOffset = function() {
	return (config.width + config.gap) * box.length;
};
var close = false;
var idOfWorker = null;
var msgSessionIdArray=[];
var userMsgSend = new Array();
var userConnectedMsg = new Array();
var workerRemedy = function(userid){
	if(userid!=null&&userid!=undefined){

		
		$.ajax({
			url: servicesURL+'/Chat/user/'+userid,
			success: function(data){
				//console.log(data);
				/* for(var cnt=1; cnt <= data.length;cnt++){


		 		  $('#chat_div'+cnt).empty();
			  } */
				for(var cnt=0; cnt < data.length;cnt++){

					var oldMsgsSize = null;
					var id  = data[cnt].messageSession;
					msgSessionIdArray.push(data[cnt].messageSession);
					if(!data[cnt].active){
						var temp = $("#chat_div"+id).length;
						if(temp&& !userMsgSend[cnt]){
							chatboxManager.dispatchMsgUser("chat_div"+id,
									{ first_name:"you",
								last_name : lname
									},
									""
							);
							userMsgSend[cnt] = true;
						}

					}
					if(data[cnt].active){


						if(box[cnt]!=null)
							oldMsgsSize = box[cnt].length;           
						var $obj = data[cnt];
						var maximized = $obj.maximized;
						if($obj.messageSessionData ==null)
							continue;
						box[cnt] =$obj.messageSessionData.messageList;

						var client = data[cnt].client;
						console.log(data[cnt]);
						var fname = null;
						var lname = null;
						if(client == null){
							fname = $obj.messageSessionData.userList[1].username;
							lname ="";
						}
						else{
							fname =   client.firstName;
							lname =	client.lastName;

						}


						chatboxManager.addBox("chat_div"+id, {
							first_name: fname,
							last_name : lname,
							session : $obj.messageSession,
							userId : userid

						}
						);

						if(userConnectedMsg[cnt]==null || userConnectedMsg[cnt] == false){
							chatboxManager.dispatchUserConnected("chat_div"+id,
									{ first_name:"you",
								last_name : lname
									},
									""
							);
							userConnectedMsg[cnt]=true;
						}

						/*if(maximized!=null&&maximized !=true){
			 			// $("#chat_div"+cnt).parent().css("display","none");

			 		  }*/

						var parent = $("#chat_div"+id).parent();
						var next =  $("#chat_div"+id).parent().next(".ui-widget-header .ui-icon-minusthick").html();
						$("#chat_div"+id).parent().parent().find(".ui-icon-minusthick").click(
								function(){

									//to do minimizde

									/*	$.ajax({
			 					  url: address + '/astagurucrm/rest/Chat/user/'+userid,
			 					  success: function(data){

			 					  },
			 					  error: function(){
			 					   console.log("cannot connect to chat")
			 					  }
			 					}); 
									 */


									//
								}	  

						);

						$("#chat_div"+id).parent().parent().find(".ui-icon-closethick").unbind('click');
						$("#chat_div"+id).parent().parent().find(".ui-icon-closethick").bind('click',
								{obj: $obj}	
						,function(event){

							//to do minimizde
							close = true;
							clearTimeout(idOfWorker);
							$.ajax({
								url: servicesURL+'/Chat/close/'+event.data.obj.messageSession,
								success: function(data){
									console.log("data is"+data);
									close = false;

									idOfWorker = setTimeout(function(){workerRemedy(userid)}, 500);
									// setInterval(workerRemedy(userId),3000);

								},
								error: function(){
									console.log("cannot connect to chat");
									idOfWorker = setTimeout(function(){workerRemedy(userid)}, 500);
									//setInterval(workerRemedy(userid),3000);
								}
							}); 

						}

						//


						);

						var chatuserList = null;
						var messageList =null;
						var messageSessionData = $obj.messageSessionData;
						if(messageSessionData!=null){
							chatuserList = $obj.messageSessionData.userList;
							messageList =  $obj.messageSessionData.messageList;
						}

						var getUserName = function(msg){

							for(var cntforuser=0;cntforuser <chatuserList.length; cntforuser++){
								if(msg.userId==userid/*JAYOTTAM -> && chatuserList[cntforuser].client==false*/){
									return 'you';
								}
								else if(chatuserList[cntforuser].client==true) {
									return chatuserList[cntforuser].userName;

								}
							}

						};
						var diff =  null;
						if(messageList!=null){
							if(oldMsgsSize == null){
								oldMsgsSize = 0;
								diff = messageList.length;
							}
							else
								diff =  (messageList.length - oldMsgsSize); 
							for(var cntformsg=oldMsgsSize;cntformsg  < (oldMsgsSize+diff);cntformsg++)   
							{
								chatboxManager.dispatch("chat_div"+id,
										{ first_name: getUserName(messageList[cntformsg]),
									last_name : lname
										},
										messageList[cntformsg].message
								);

							}
						}
					}

				}

				idOfWorker = setTimeout(function(){workerRemedy(userid)}, 2000);
				//idOfWorker =

				// console.log("hit");


			},
			error: function(){
				console.log("cannot connect to chat");
			}
		}); 


	}
};
function closeChatOfAdmin(){
	window.localStorage.setItem("messageSession",undefined);
	close = true;
	workerRemedy(null);
	for(var cnt=0; cnt < msgSessionIdArray.length;cnt++){
		$.ajax({
			url: servicesURL+'/Chat/close/'+msgSessionIdArray[cnt],
			success: function(data){
				console.log("data is"+data);
				close = false;
				

				// setInterval(workerRemedy(userId),3000);

			},
			error: function(){
				console.log("cannot connect to chat");

				//setInterval(workerRemedy(userid),3000);
			}
		}); 
	}

}

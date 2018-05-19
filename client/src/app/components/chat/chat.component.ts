import { User } from '../../model/user';
import { Message } from '../../model/message';
import { AuthService } from '../../services/auth.service';
import { MessageService } from '../../services/message.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent{
  currentUser: User = new User();
  messages: Message[];
  private serverUrl = 'https://web-event-manager.herokuapp.com/socket'//https://web-event-manager.herokuapp.com/socket
  private title = 'Chat';
  private stompClient;
  private id;
      
  constructor(private router: Router,private auth: AuthService, private msgService: MessageService){
    this.initializeWebSocketConnection();
  }
  
  ngOnInit() {
    this.auth.current_user.subscribe(
      current_user => {
        this.currentUser = current_user;
        this.id = this.currentUser.id;
      });
    this.msgService.loadMessages().subscribe((messages: any) => {
      this.messages = messages;
    });
  }
  
  initializeWebSocketConnection(){
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe(that.router.url, (message:any) => {
        if(message.body) {
          var q = JSON.parse(message.body);
          $(".chat").append(
            "<li class='left clearfix'> " +
            "<span class='chat-img pull-left'> " +
            "<img src='"+q.image+"' width='50' height='50' alt='User Avatar' class='img-circle' />" +
            "</span> <div class='chat-body clearfix'> " +
              "<div class='header'>" +
              " <strong class='primary-font'>&nbsp"+q.login+"</strong> <small class='pull-right text-muted'>" +
              "<span class='glyphicon glyphicon-time'></span>"+q.date+"</small> </div>" +
              "<p>&nbsp"+q.text+"</p> </div> </li>" +
              "<hr width='90%' size='2' color='grey' />"    
          )
        }
      });
    });
  }

  sendMessage(message){
    if(message.length>0){
      this.stompClient.send("/app/send"+this.router.url, {userId:this.id}, this.id+";"+message);
    }
    $('#input').val('');
  }
}

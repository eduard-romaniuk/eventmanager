import {Component} from '@angular/core';
import {EventService} from '../../services/event.service';
import {Router, ActivatedRoute} from '@angular/router';
import {JQueryStatic} from 'jquery';

import {Event} from '../../model/event'
import {User} from "../../model/user";
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs/Subscription";
import {FormGroup} from "@angular/forms";
import {ToastService} from "../../services/toast.service";
import {UserService} from "../../services/user.service";
import {NotificationSettings} from "../../model/notificationSettings";
import {NotificationSettingsService} from "../../services/notification-settings.service";

@Component({
  selector: 'app-createEvent',
  templateUrl: './viewEvent.component.html',
  styleUrls: ['./viewEvent.component.css']
})
export class ViewEventComponent {

  event: Event = new Event();
  userId: number;
  priority: String;
  notificationSetting: NotificationSettings = new NotificationSettings();
  form: FormGroup;
  isParticipant: boolean;
  isCreator: boolean;
  participationStr: String;
  participants: User[];

  candidates: User[];


  newParticipants: User[];

  latitude: Number;
  longitude: Number;


  sub: Subscription;

  public isLoading = true;

  constructor(private auth: AuthService,
              private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService,
              private toast: ToastService,
              private userService: UserService,
              private notificationSettingsService: NotificationSettingsService) {
  }

  ngOnInit() {
    this.auth.getUser().subscribe((user: any) => {
      this.userId = user.id;

      console.log("currentUserId = " + user.id);

      this.sub = this.route.params.subscribe(params => {
        const id = params['id'];
        if (id) {
          this.eventService.isParticipantRequest(id).subscribe((participation: String) => {
            if (participation) {
              this.participationStr = participation;
              this.isParticipant = (this.participationStr == "true");
            } else {
              console.log(`Participation not found!`);
            }
            this.eventService.getEventById(id).subscribe((event: any) => {
              if (event) {
                this.event = event;
                this.isCreator = this.isCreatorTest();
                this.Position();
                this.eventService.getPriority(id).subscribe((priority: String) => {
                  if (priority) {
                    this.priority = priority;

                    this.notificationSettingsService.getByUserIdAndEventId(this.userId, this.event.id)
                      .subscribe((notificationSetting: any) => {
                        if (notificationSetting) {
                          this.notificationSetting = notificationSetting;
                        } else {
                          console.log(`Notification Setting not found!`);
                        }
                      });

                  } else {
                    console.log(`Priority not found!`);
                  }
                  this.isLoading = false;
                });
                console.log(`Event with id '${id}' was loaded!`);
                console.log(event);
              } else {
                console.log(`Event with id '${id}' not found!`);
              }
            });

          });
        }
      });
    });
  }

  private Position() {

    let coords = this.event.place.split("/");
    this.latitude = Number(coords[0]);
    this.longitude = Number(coords[1]);
    console.log(this.latitude);
    console.log(this.longitude);

  }

  public submitPriority() {
    this.eventService.changePriority(this.event.id, this.priority).subscribe();
  }

  public join() {
    this.eventService.joinToEvent(this.event.id).subscribe(response => {
      window.location.reload();
      this.toast.success('You become a participant in this event');
    });
  }

  public leave() {
    this.eventService.leaveEvent(this.event.id).subscribe();
    window.location.reload();
  }

  public delete() {
    this.eventService.deleteEvent(this.event.id).subscribe((user: any) => {
      this.router.navigate(['home']);
    }, error => console.error(error));
  }

  publish(){
    this.event.isSent=true;
    this.eventService.updateEvent(this.event).subscribe((user: any) => {
      this.router.navigate(['event/', this.event.id]);
    }, error => console.error(error));
  }
  addUsers(){

    console.log(this.newParticipants);
    this.eventService.addUsers(this.newParticipants,this.event.id).subscribe();
  }

  getFriends() {

    this.eventService.getFriendsNotParticipants(this.event.id)
      .subscribe((friends: any) => {
        this.candidates = friends;
        console.log(this.candidates)
      });
  }



  public isCreatorTest(): boolean {
    return this.userId === this.event.creator.id;
  }

  public showParticipants() {
    this.eventService.getParticipants(this.event.id)
      .subscribe((users: any) => {
        this.participants = users;
      });
    console.log(this.participants)
  }

  goToChatWithCreator() {
    this.router.navigate(['chats', this.event.id, 'withCreator']);
  }

  goToChatWithoutCreator() {
    this.router.navigate(['chats', this.event.id, 'withoutCreator']);
  }

  low() {
    if (this.priority == "0") return true
  }

  normal() {
    if (this.priority == "1") return true
  }

  urgent() {
    if (this.priority == "2") return true
  }
}

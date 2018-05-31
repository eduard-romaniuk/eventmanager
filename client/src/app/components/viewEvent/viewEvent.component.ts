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
  removeParticipants: User[];

  latitude: Number;
  longitude: Number;
  isStarted: boolean;

  sub: Subscription;

  public isLoading = true;

  constructor(private auth: AuthService,
              private route: ActivatedRoute,
              private router: Router,
              private eventService: EventService,
              private toast: ToastService,
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
                this.isStarted = (new Date() >= new Date(this.event.timeLineStart));

                this.eventService.getPriority(id).subscribe((priority: String) => {
                  if (priority) {
                    this.priority = priority;

                    this.notificationSettingsService.getByUserIdAndEventId(this.userId, this.event.id)
                      .subscribe((notificationSetting: any) => {
                        if (notificationSetting) {
                          this.notificationSetting = notificationSetting;

                          if (this.notificationSetting.startDate == null) {
                            const eventStartDate = new Date(this.event.timeLineStart);
                            this.notificationSetting.startDate = new Date(eventStartDate.getTime());
                            const maxNotificationDate = eventStartDate.getDate() - 1;
                            this.notificationSetting.startDate.setDate(maxNotificationDate);
                          }

                        } else {
                          console.log(`Notification Setting not found!`);
                        }
                      });

                  } else {
                    console.log(`Priority not found!`);
                  }
                  this.newParticipants = [];
                  this.removeParticipants =[];
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
      this.eventService.getPriority(this.event.id).subscribe((priority: String) => {
        this.priority = priority;
        this.isParticipant = true;
        this.router.navigate(['event/', this.event.id]);
        this.toast.success('You become a participant in this event');
      });
    });
  }

  public leave() {
    this.eventService.leaveEvent(this.event.id).subscribe(response => {
      this.router.navigate(['event/', this.event.id]);
      this.toast.warn('You leave this event');
      this.isParticipant = false;
    });

  }

  public delete() {
    this.eventService.deleteEvent(this.event.id).subscribe((user: any) => {
      this.toast.success("Event '"+ this.event.name + "' was deleted");
      this.router.navigate(['home']);
    }, error => console.error(error));
  }

  publish() {
    this.event.isSent = true;
    this.eventService.updateEvent(this.event).subscribe((user: any) => {
      this.router.navigate(['event/', this.event.id]);
    }, error => console.error(error));
  }

  addUsers() {

    console.log(this.newParticipants);

    let participantsNames = "";
    this.newParticipants.forEach(function (value) {
      participantsNames = participantsNames +  value.name+" "+value.surName + " ";
    });

    this.eventService.addUsers(this.newParticipants, this.event.id).subscribe((user: any) => {
      this.getFriends();
      this.toast.success(participantsNames+" was added to this event");

    }, error => console.error(error));

    this.newParticipants = [];
  }

  cancelAddUsers(){
    this.newParticipants = [];
    this.router.navigate(['event/', this.event.id]);

  }

  removeUsers() {
    let participantsNames = "";
    this.removeParticipants.forEach(function (value) {
      participantsNames = participantsNames +  value.name+" "+value.surName + " ";
    });

    this.eventService.removeUsers(this.removeParticipants, this.event.id).subscribe((user: any) => {
      this.showParticipants();
      this.toast.warn(participantsNames+" was remove from this event");

    }, error => console.error(error));

    this.removeParticipants = [];
  }

  cancelRemoveUsers(){
    this.removeParticipants = [];
    this.router.navigate(['event/', this.event.id]);
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

  isBegun(){
    if(new Date() > new Date(this.event.timeLineStart)) return true
  }
  isEnded(){
    if(new Date() > new Date(this.event.timeLineFinish)) return true
  }
}

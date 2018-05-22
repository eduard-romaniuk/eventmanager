import { NgModule, Injectable } from '@angular/core';
import { RouterModule, Routes, CanActivate, Router } from '@angular/router';
import { HelloComponent } 		from '../components/hello/hello.component';
import { HomeComponent } 		  from '../components/home/home.component';
import { CreateEventComponent }       from '../components/createEvent/createEvent.component';
import { ViewEventComponent }       from '../components/viewEvent/viewEvent.component';
import { AuthService } 			  from '../services/auth.service';

import { EmailVerificationComponent } from '../components/email-verification/email-verification.component';
import { UserComponent } from '../components/user/user.component';
import { UserListComponent } from '../components/user/user-list.component';
import { UserEditComponent } from '../components/user/user-edit.component';
import { EventEditComponent } from '../components/event-edit/event-edit.component';
import { PublicEventListComponent } from '../components/public-event-list/event-list.component';
import { EventsListComponent } from '../components/events-list/events-list.component';
import { UserEventListComponent } from '../components/user-event-list/user-event-list.component';
import { UserEditImageComponent } from '../components/user/user-edit-image.component';
import { UserSearchComponent } from "../components/user/user-search.component";
import { WishListComponent } from '../components/wishlist/wishlist.component';
import { ChatComponent } from "../components/chat/chat.component";
import { ExportEventsPlanComponent } from "../components/export-events-plan/export-events-plan.component";
import { UserEditPasswordComponent } from "../components/user/user-edit-password/user-edit-password.component";
import { PersonalPlanSettingComponent } from "../components/personal-plan-setting/personal-plan-setting.component";
import { RecoverPasswordComponent } from '../components/recover-password/recover-password.component';
import { RecoverLoginComponent } from '../components/recover-login/recover-login.component';
import { ChangePasswordComponent } from '../components/change-password/change-password.component';
import { RootFolderComponent } from "../components/folders/rootFolder/rootFolder.component"
import { CreateNoteComponent } from "../components/note/createNote/createNote.component"
import { ViewNoteComponent } from "../components/note/viewNote/viewNote.component"
import { FolderComponent } from "../components/folders/folder/folder.component"
import { UserSettingsComponent } from "../components/user/user-settings/user-settings.component";
import {StatisticsComponent} from '../components/statistics/statistics.component';
import { NoteEditComponent } from '../components/note/note-edit/note-edit.component';
import {ConvertComponent} from "../components/note/convert/convert.component";

@Injectable()
export class OnlyLoggedInUsersGuard implements CanActivate {
  constructor(private auth: AuthService, private router: Router) {};

  canActivate() {
    if (!this.auth.authenticated) {
      this.router.navigateByUrl('/hello');
      return false;
    }
    return true;
  }
}

const routes: Routes = [
  { path: '', redirectTo: '/hello', pathMatch: 'full' },
  { path: 'hello',
  	component: HelloComponent },
  { path: 'email-verification/:token',
    component: EmailVerificationComponent },
  { path: 'recover/password',
    component: RecoverPasswordComponent },
  { path: 'recover/login',
    component: RecoverLoginComponent },
  { path: 'recover-password/:token',
    component: ChangePasswordComponent },
  { path: 'home',
    component: HomeComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'event/create',
    component: CreateEventComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'events/list',
      component: EventsListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'events/list/:date',
      component: PublicEventListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/:id/events/:priority/:date',
      component: UserEventListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'event/:id',
    component: ViewEventComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users',
    component: UserListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'wishlist',
    component: WishListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/:id',
    component: UserComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/:userId/wishlist',
    component: WishListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'items/popular',
    component: WishListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/:id/edit',
    component: UserEditComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'settings',
    component: UserSettingsComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'event/:id/edit',
    component: EventEditComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'event/:eventId/:userId/wishlist',
    component: WishListComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/:id/updateImage',
    component: UserEditImageComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'changePassword',
    component: UserEditPasswordComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'searchUsers',
    component: UserSearchComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'chats/:id/withCreator',
    component: ChatComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'chats/:id/withoutCreator',
    component: ChatComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'events/export',
    component: ExportEventsPlanComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'users/setting/plan',
    component: PersonalPlanSettingComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'folders/rootFolder',
    component: RootFolderComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'folders/rootFolder/folder/:id',
    component: FolderComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'note/create/:folderId',
    component: CreateNoteComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'note/:noteId',
    component: ViewNoteComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'statistics',
    component: StatisticsComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'note/:noteId/edit',
    component: NoteEditComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: 'note/:noteId/convert',
    component: ConvertComponent,
    canActivate: [ OnlyLoggedInUsersGuard ] },
  { path: '**', redirectTo: '/hello', pathMatch: 'full'}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ],
  providers: [ OnlyLoggedInUsersGuard,
               AuthService ]
})
export class AppRoutingModule {}

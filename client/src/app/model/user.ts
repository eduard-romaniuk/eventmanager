export class User {
  //TODO private
   id: number;
   login: String;
   password: String;
   passwordConfirm: String;
   name: String;
   surName: String;
   email: String;
   birth: any; //TODO date
   phone: String;
   sex: boolean;
   image: String;
   regDate: Date; //TODO date
   friends: User[];
   wishList: any; //TODO wishList
   events: any[]; //TODO events
   settings: any; //TODO settings
   token: String;
   verified: boolean;
}

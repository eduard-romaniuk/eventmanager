export class User {
  id: number;
  login: string;
  password: string;
  passwordConfirm: string;
  name: string;
  surName: string;
  email: string;
  birth: any; //TODO date
  phone: string;
  sex: boolean;
  active: boolean;
  image: any; //TODO Image
  regDate: any; //TODO date
  friends: User[];
  wishList: any; //TODO wishList
  events: any[]; //TODO events
  settings: any; //TODO settings
  confLink: string;
}

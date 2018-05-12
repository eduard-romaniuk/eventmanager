import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-email-verification',
  templateUrl: './email-verification.component.html',
  styleUrls: ['./email-verification.component.css']
})
export class EmailVerificationComponent implements OnInit {

  public token: string;
  public form: FormGroup;
  public credentials = {login: '', password: ''};
  public loading = false;
  public error = false;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private auth: AuthService,
              private toast: ToastService,
              private formBuilder: FormBuilder,
              private userService: UserService) {}

  ngOnInit() {
  	this.route.params.subscribe(params => {
  		this.token = params['token'];
  		if(this.token) {
			  this.form = this.formBuilder.group({
			    login: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9_]*$') ]],
			    password: ['', [ Validators.required, Validators.pattern('^[a-zA-Z0-9]*$') ]]
			  });
  		} else {
  			this.router.navigate(['home']);
  			this.toast.error('Invalid verification link');
  		}
  	});
  }

  public login() {
    this.loading = true;
    this.error = false;

    this.auth.authenticate(this.credentials, () => {
        this.auth.current_user.subscribe(
          user => {
            this.verify(user, this.token);
          });
      },
      () => {
        this.error = true,
        this.loading = false
      });
  }

  public verify(user: User, token: string) {
  	if(user.verified) {
		  this.router.navigate(['home']);
  		this.toast.info('Already verified');
	  } else {
  		const isVerified = this.token === user.token;
      const isActive = this.diffDays(new Date(), user.regDate) < 1;
	  	if(isVerified && isActive) {
	  		user.verified = isVerified;
	  		this.userService.updateUser(user).subscribe(response =>{
		  		this.router.navigate(['home']);
		  		this.toast.success('Your account verified');
	  		}, error => {
	  			this.toast.error('Put error');
	  		});
	  	} else {
        if (!isActive) {
          this.userService.deleteUser(user.id);
        }
	  		this.router.navigate(['home']);
	  		this.toast.error('Invalid verification link');
	  	}
	  }
  }

  private diffDays(date1: Date, date2: Date) {
    return Math.abs(date2.getTime() - date1.getTime()) / (1000 * 3600 * 24);
  }

}

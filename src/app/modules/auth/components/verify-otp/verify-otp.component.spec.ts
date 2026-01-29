import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { VerifyOtpComponent } from './verify-otp.component';
import { AuthService } from '../../../../core/services/auth.service';

describe('VerifyOtpComponent', () => {
  let component: VerifyOtpComponent;
  let fixture: ComponentFixture<VerifyOtpComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let activatedRouteSpy: any;

  beforeEach(async () => {
    const authSpy = jasmine.createSpyObj('AuthService', ['verifyOtp']);
    const routeSpy = {
      snapshot: {
        queryParamMap: {
          get: jasmine.createSpy('get').and.returnValue('test@example.com')
        }
      }
    };

    await TestBed.configureTestingModule({
      declarations: [VerifyOtpComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: AuthService, useValue: authSpy },
        { provide: ActivatedRoute, useValue: routeSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(VerifyOtpComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    activatedRouteSpy = TestBed.inject(ActivatedRoute);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with email from query params', () => {
    expect(component.email).toBe('test@example.com');
    expect(component.verifyOtpForm.get('email')?.value).toBe('test@example.com');
  });

  it('should validate OTP format', () => {
    const form = component.verifyOtpForm;
    form.setValue({ email: 'test@example.com', otp: '12345' });
    expect(form.invalid).toBeTruthy();
    expect(form.get('otp')?.errors?.['pattern']).toBeTruthy();

    form.setValue({ email: 'test@example.com', otp: '123456' });
    expect(form.get('otp')?.errors?.['pattern']).toBeFalsy();
  });

  it('should call authService.verifyOtp on form submit', () => {
    const form = component.verifyOtpForm;
    form.setValue({ email: 'test@example.com', otp: '123456' });
    
    component.onSubmit();
    
    expect(authServiceSpy.verifyOtp).toHaveBeenCalledWith({
      email: 'test@example.com',
      otp: '123456'
    });
  });
});

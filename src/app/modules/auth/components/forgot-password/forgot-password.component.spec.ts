import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { ForgotPasswordComponent } from './forgot-password.component';
import { AuthService } from '../../../../core/services/auth.service';

describe('ForgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('AuthService', ['forgotPassword']);

    await TestBed.configureTestingModule({
      declarations: [ForgotPasswordComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: AuthService, useValue: spy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty email field', () => {
    expect(component.forgotPasswordForm.get('email')?.value).toBe('');
  });

  it('should validate email field', () => {
    const form = component.forgotPasswordForm;
    
    form.setValue({ email: '' });
    expect(form.invalid).toBeTruthy();
    expect(form.get('email')?.errors?.['required']).toBeTruthy();

    form.setValue({ email: 'invalid-email' });
    expect(form.invalid).toBeTruthy();
    expect(form.get('email')?.errors?.['email']).toBeTruthy();

    form.setValue({ email: 'valid@example.com' });
    expect(form.valid).toBeTruthy();
  });

  it('should call authService.forgotPassword on form submit', () => {
    const form = component.forgotPasswordForm;
    form.setValue({ email: 'test@example.com' });
    
    component.onSubmit();
    
    expect(authServiceSpy.forgotPassword).toHaveBeenCalledWith({
      email: 'test@example.com'
    });
  });
});

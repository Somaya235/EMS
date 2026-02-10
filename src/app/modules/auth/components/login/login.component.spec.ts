import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../../core/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: AuthService, useValue: spy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty fields', () => {
    expect(component.loginForm.get('email')?.value).toBe('');
    expect(component.loginForm.get('password')?.value).toBe('');
  });

  it('should validate required fields', () => {
    const form = component.loginForm;
    form.setValue({ email: '', password: '' });
    expect(form.invalid).toBeTruthy();
  });

  it('should call authService.login on form submit', () => {
    const form = component.loginForm;
    form.setValue({ email: 'testuser@example.com', password: 'testpass' });

    component.onSubmit();

    expect(authServiceSpy.login).toHaveBeenCalledWith({
      email: 'testuser@example.com',
      password: 'testpass'
    });
  });
});

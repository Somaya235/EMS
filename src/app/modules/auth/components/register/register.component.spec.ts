import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../../core/services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('AuthService', ['register']);

    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [ReactiveFormsModule, RouterTestingModule],
      providers: [
        { provide: AuthService, useValue: spy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authServiceSpy = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize stepper with step 1', () => {
    expect(component.currentStep).toBe(1);
    expect(component.totalSteps).toBe(3);
    expect(component.getStepTitle()).toBe('Personal Information');
  });

  it('should navigate to next step when valid', () => {
    // Set valid values for step 1
    component.registerForm.get('fullName')?.setValue('Test User');
    component.registerForm.get('email')?.setValue('test@example.com');

    component.nextStep();

    expect(component.currentStep).toBe(2);
    expect(component.getStepTitle()).toBe('Account Security');
  });

  it('should not navigate to next step when invalid', () => {
    // Keep step 1 invalid
    component.nextStep();

    expect(component.currentStep).toBe(1);
  });

  it('should navigate to previous step', () => {
    component.currentStep = 2;
    component.previousStep();

    expect(component.currentStep).toBe(1);
  });

  it('should validate current step correctly', () => {
    // Step 1 validation
    component.registerForm.get('fullName')?.setValue('Test User');
    component.registerForm.get('email')?.setValue('test@example.com');
    expect(component.isCurrentStepValid()).toBeTruthy();

    // Step 2 validation
    component.currentStep = 2;
    component.registerForm.get('password')?.setValue('password123');
    component.registerForm.get('confirmPassword')?.setValue('password123');
    component.registerForm.get('grade')?.setValue('10');
    component.registerForm.get('phoneNumber')?.setValue('1234567890');
    expect(component.isCurrentStepValid()).toBeTruthy();
  });

  it('should validate required fields', () => {
    const form = component.registerForm;
    form.setValue({
      fullName: '',
      email: '',
      password: '',
      confirmPassword: '',
      grade: '',
      phoneNumber: '',
      nationalNumber: '',
      dateOfBirth: '',
      collageId: '',
      roles: ['MEMBER']
    });
    expect(form.invalid).toBeTruthy();
  });

  it('should validate password matching', () => {
    const form = component.registerForm;
    form.setValue({
      fullName: 'Test User',
      email: 'test@example.com',
      password: 'password123',
      confirmPassword: 'different',
      grade: '10',
      phoneNumber: '1234567890',
      nationalNumber: '1234567890123',
      dateOfBirth: '2000-01-01',
      collageId: 'COL001',
      roles: ['USER']
    });
    expect(form.invalid).toBeTruthy();
    expect(form.get('confirmPassword')?.errors?.['mustMatch']).toBeTruthy();
  });

  it('should call authService.register on form submit', () => {
    // Fill all form data
    component.registerForm.setValue({
      fullName: 'Test User',
      email: 'test@example.com',
      password: 'password123',
      confirmPassword: 'password123',
      grade: '10',
      phoneNumber: '1234567890',
      nationalNumber: '1234567890123',
      dateOfBirth: '2000-01-01',
      collageId: 'COL001',
      roles: ['MEMBER']
    });

    component.onSubmit();

    expect(authServiceSpy.register).toHaveBeenCalledWith({
      fullName: 'Test User',
      email: 'test@example.com',
      password: 'password123',
      grade: 10,
      phoneNumber: '1234567890',
      nationalNumber: '1234567890123',
      dateOfBirth: new Date('2000-01-01'),
      collageId: 'COL001',
      roles: ['MEMBER']
    });
  });
});

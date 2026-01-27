# Glassmorphism Login Component

A standalone Angular component featuring a modern glassmorphism-styled login form.

## Features

- **Glassmorphism Design**: Semi-transparent backgrounds with backdrop blur effects
- **Responsive Layout**: Mobile-first responsive design
- **Accessibility**: Proper ARIA attributes and semantic HTML
- **Template-Driven Forms**: Angular forms integration
- **Modern UI**: Beautiful gradient backgrounds and hover effects

## Component Structure

### Files Created:
- `glassmorphism-login.component.ts` - Main component logic
- `glassmorphism-login.component.html` - Template with glassmorphism styling
- `glassmorphism-login.component.css` - Complete CSS styling with responsive design
- `glassmorphism-login.module.ts` - Module for non-standalone usage
- `index.ts` - Barrel export file

## Usage

### As Standalone Component:

```typescript
import { Component } from '@angular/core';
import { GlassmorphismLoginComponent } from './components/glassmorphism-login';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [GlassmorphismLoginComponent],
  template: '<app-glassmorphism-login></app-glassmorphism-login>'
})
export class AppComponent {}
```

### As Module:

```typescript
import { GlassmorphismLoginModule } from './components/glassmorphism-login';

@NgModule({
  imports: [GlassmorphismLoginModule],
  // ...
})
export class AppModule {}
```

## Template Usage:

```html
<app-glassmorphism-login></app-glassmorphism-login>
```

## Styling Features

- **Background**: Full-screen background image with gradient overlay
- **Glass Effect**: Multiple layers of backdrop blur for depth
- **Input Fields**: Rounded corners (3xl) with semi-transparent backgrounds
- **Color Scheme**: White/70 to white/90 text opacity
- **Hover States**: Smooth transitions and interactive feedback
- **Responsive**: Different padding for mobile, tablet, and desktop

## Form Elements

1. **Brand Logo**: SVG placeholder with glass effect
2. **Welcome Heading**: "Welcome Back" title
3. **Sign Up Link**: "Don't have an account? Sign up" text
4. **Username Input**: With user icon and glass styling
5. **Password Input**: With lock icon and glass styling
6. **Sign In Button**: Full-width with hover effects
7. **Forgot Password**: Link at the bottom

## Dependencies

- Angular 20+
- Angular Forms (for ngModel)
- Angular Common (for directives)

## Browser Support

- Modern browsers with backdrop-filter support
- Webkit browsers with -webkit-backdrop-filter prefix
- Graceful degradation for older browsers

## Customization

The component can be easily customized by modifying the CSS variables in the component stylesheet:

- Background images
- Color schemes
- Border radius values
- Blur intensities
- Animation timings

# Admin Dashboard - Traffic Fine System

## Overview
This is the admin dashboard for the Traffic Fine System, built with React and Vite. It provides administrators with a complete interface to manage traffic fines, view analytics, and manage users.

## Features
- 📊 Dashboard with analytics and statistics
- 👥 User management
- 📋 Traffic fine management
- 📈 Real-time data visualization with charts
- 🔐 Secure authentication with JWT
- 📱 Responsive design with Tailwind CSS
- ⚡ Fast performance with Vite

## Tech Stack
- **Frontend Framework:** React 18.2.0
- **Build Tool:** Vite 5.0.0
- **Styling:** Tailwind CSS 3.4.1
- **Charts:** Recharts 2.10.0
- **HTTP Client:** Axios 1.6.0
- **Notifications:** React Toastify 9.1.3
- **Icons:** Lucide React 0.294.0
- **Routing:** React Router DOM 6.20.0

## Installation

### Prerequisites
- Node.js 16+ 
- npm or yarn

### Setup
1. Install dependencies:
```bash
npm install
```

2. Configure environment variables by creating `.env.local`:
```
VITE_API_BASE_URL=http://localhost:8080/api
```

3. Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173`

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## Project Structure
```
src/
├── components/         # Reusable UI components
├── pages/             # Page components (Login, Dashboard)
├── services/          # API communication
├── context/           # React context for authentication
├── hooks/             # Custom React hooks
├── App.jsx            # Main app component
├── main.jsx           # Application entry point
└── index.css          # Global styles
```

## Usage

### Authentication
The dashboard uses JWT-based authentication. Users must log in with their credentials to access the admin features.

### Dashboard
The main dashboard displays:
- Key statistics and metrics
- Traffic fine analytics
- System overview
- Recent activities

## API Integration
The dashboard communicates with the backend API for:
- User authentication
- Traffic fine data management
- Analytics and reporting
- User administration

## Deployment

### Build for Production
```bash
npm run build
```

This creates an optimized production build in the `dist/` directory.

## License
This project is part of the Traffic Fine System. All rights reserved.

## Support
For issues and questions, please contact the development team.

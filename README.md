# Booking system for neurobiofeedback clinic 🧠

## Overview 📋

This application allows patients to easily book, manage, and track their appointments at my brother's neurobiofeedback clinic, enhancing both patient and clinic efficiency. The app is deployed on AWS EC2 instance as docker containers and is available at: https://neurorezerwacje.pl

## Features 🌟

- **Mail Confirmations**: Sends automated email confirmations to users, ensuring they receive timely and relevant updates.
- **Fully Responsive**: Provides a seamless user experience on any device, as the app is fully optimized for both desktop and mobile platforms.
- **Google Sign-In**: Enables easy sign-in with Google accounts for a quick and secure authentication process.

## Technologies Used 🛠️

### Backend 🔧
- Spring Boot REST API
- Deployed as a Docker container on AWS EC2 instance
- Spring Security with JWT tokens for authentication
- Google sign-in integration
- Email confirmations using JavaMailSender
- Audit functionality
- JPA (Java Persistence API)
- PostgreSQL database

### Frontend 💻
- TypeScript
- React
- Tailwind CSS
- DaisyUI
- Deployed as a Docker container on AWS EC2 instance
- Multilingual support (two languages)
- Dual theme support (light and dark modes)

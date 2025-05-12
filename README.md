# Eventora - Event Management Application

## Description
Eventora is a comprehensive event management platform that simplifies the organization, booking, and participation in all types of events. Developed by a team of 5 students, this application aims to revolutionize how events are created, managed, and experienced by users.

* **Its objective**: To facilitate event management and enhance user experience for both organizers and participants by providing an intuitive interface and advanced features.
* **The problem it solves**: Eliminates the complexity of event organization by centralizing all necessary functionalities in a single application, reducing the need to use multiple separate tools.
* **Main features**:
  * Member management and multiple authentication (local and Google OAuth)
  * Customizable event packs and services
  * Advanced booking system (standard and customized)
  * Secure payment integration with Stripe
  * Claims management and detailed feedback system
  * Multi-channel notifications (email via Brevo/Sendinblue, SMS via Twilio)
  * Google Calendar integration for event synchronization
  * QR code generation for entry verification
  * Complete and secure administration interface
  * Multilingual support (French, English)

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Technologies](#technologies)
- [Project Architecture](#project-architecture)
- [Detailed Features](#detailed-features)
- [API and Integrations](#api-and-integrations)
- [Development Team](#development-team)
- [Contributions](#contributions)
- [License](#license)
- [Contact](#contact)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/team-eventora/EventoraApp.git
   cd EventoraApp
   ```

2. Configure your environment:
   * Create a `.env.local` file from `.env`:
     ```bash
     cp .env .env.local
     ```
   * Configure the following environment variables in your `.env.local`:
     ```
     # Database
     DATABASE_URL="mysql://user:password@127.0.0.1:3306/eventora_db?serverVersion=8.0"
     
     # API Keys
     GOOGLE_CLIENT_ID=your_client_id
     GOOGLE_CLIENT_SECRET=your_client_secret
     STRIPE_KEY=your_public_key
     STRIPE_SECRET=your_secret_key
     BREVO_API_KEY=your_api_key
     TWILIO_ACCOUNT_SID=your_sid
     TWILIO_AUTH_TOKEN=your_token
     TWILIO_PHONE_NUMBER=your_number
     ```

3. Configure Java 17 and JavaFX 20

4. Run the main class

## Usage

### Java Installation

To use this project, you need to install Java 17 or higher. Here are the steps:

1. Download Java from the official website: [Java - Downloads](https://www.oracle.com/java/technologies/downloads/)

2. Install Java following the instructions specific to your operating system:
   - For Windows, download and run the installer
   - For macOS, you can use Homebrew:
     ```bash
     brew install openjdk@17
     ```
   - For Linux, you can install Java via package manager:
     ```bash
     sudo apt update
     sudo apt install openjdk-17-jdk
     ```

3. Verify Java installation:
   ```bash
   java -version
   ```

### Application Access

After installation, you can access different spaces:

1. **Public Space**: Accessible at root URL (`/`)
   * Browse available events
   * Registration and login
   * Event search

2. **Member Space**: Accessible after login (`/member`)
   * Profile management
   * Event booking
   * Booking consultation
   * Payments

3. **Admin Space**: Accessible for administrators (`/admin`)
   * User management
   * Event creation and editing
   * Statistics and reports
   * System configuration

### Demo Accounts

You can log in with these accounts:

* **Administrator**:
  * Email: admin@eventora.com
  * Password: admin123

* **Standard User**:
  * Email: user@example.com
  * Password: user123

## Technologies

The project uses a modern and robust technology stack:

* **Backend**:
  * **Language**: Java 17
  * **Framework**: JavaFX 20
  * **Database**: MySQL 8.0
  * **API**: REST API
  * **Authentication**: OAuth 2.0 (Google)

* **Frontend**:
  * **UI Framework**: JavaFX
  * **CSS**: Custom styling
  * **Build tools**: Maven

* **Integrations**:
  * **Payment**: Stripe
  * **Email**: Brevo/Sendinblue
  * **SMS**: Twilio
  * **Calendar**: Google Calendar API
  * **Communication**: Discord API

* **DevOps**:
  * **CI/CD**: GitHub Actions
  * **Testing**: JUnit

* **Tools**:
  * **PDF**: iText
  * **QR Code**: ZXing
  * **Forms**: JavaFX Forms

## Project Architecture

The project follows the MVC pattern:

```
src/
├── main/
│   ├── java/
│   │   └── Gui/
│   │       ├── Reservation/
│   │       │   ├── Controllers/
│   │       │   ├── Interface/
│   │       │   └── Models/
│   │       ├── Service/
│   │       │   ├── Controllers/
│   │       │   ├── Interface/
│   │       │   └── Models/
│   │       └── Pack/
│   │           ├── Controllers/
│   │           ├── Interface/
│   │           └── Models/
│   └── resources/
│       ├── style.css
│       └── images/
```

## Detailed Features

### Member Management
* **Registration and authentication**:
  * Local registration with email validation
  * Google OAuth 2.0 login
  * Secure password recovery
* **User profiles**:
  * Personal information management
  * Avatar upload
  * Activity history

### Event Management
* **Event packs**:
  * Custom pack creation and configuration
  * Service association with packs
  * Capacity and availability management
* **Services**:
  * Configurable service catalog
  * Standard and custom services
  * Flexible pricing

### Booking System
* **Standard booking**:
  * Predefined pack selection
  * Date and option selection
  * Integrated payment
* **Custom booking**:
  * Custom event configuration
  * A la carte service selection
  * Automated quotes

### Payment System
* **Stripe integration**:
  * Secure card payment
  * Refund management
  * Split payments
* **Billing**:
  * Automatic PDF invoice generation
  * Transaction history
  * Accounting export

### Customer Support
* **Claims management**:
  * Ticket submission
  * Status tracking
  * Admin assignment
* **Feedback**:
  * Post-event evaluation
  * Improvement suggestions
  * Statistical analysis

## Development Team

Eventora was developed by a team of 5 students passionate about development and event management:

* **Khalil Abdelmoumen** - Claims and feedback management
  * Claims tracking and processing
  * Feedback system implementation

* **Hedia Snoussi** - User management
  * Authentication and security
  * Profile and role management
  * User administration interface

* **Nadhem Hmida** - Service management
  * Service and partner catalog
  * Pricing and availability
  * Service integration with packs
  * Payment integration

* **Farah Gharbi** - Pack management
  * Pack creation and configuration
  * Service association with packs
  * Recommendation system
  * Favorites list management

* **Rayen Trad** - Reservation management
  * Standard and customized reservation system
  * Reservation workflow

## Contributions

We thank everyone who has contributed to this project!

### Contributors

The following people have contributed to this project by adding features, fixing bugs, or improving documentation:

- [Khalil Abdelmoumen](https://github.com/Khaalilabd/EventoraApp/tree/Reclamation) - Claims and feedback management
- [Hedia Snoussi](https://github.com/Khaalilabd/EventoraApp/tree/Utilisateur) - User management
- [Nadhem Hmida](https://github.com/Khaalilabd/EventoraApp/tree/Gestion_Service) - Service management
- [Farah Gharbi](https://github.com/Khaalilabd/EventoraApp/tree/GestionPack) - Pack management
- [Rayen Trad](https://github.com/Khaalilabd/EventoraApp/tree/Reservation) - Reservation management

### How to contribute?

1. **Fork the project**: Go to the project's GitHub page and click the *Fork* button in the top right corner.

2. **Clone your fork**: Clone the fork to your local machine:
   ```bash
   git clone https://github.com/your-username/EventoraApp.git
   cd EventoraApp
   ```

3. **Create a branch**: Create a new branch for your feature:
   ```bash
   git checkout -b feature/new-feature
   ```

4. **Make your changes**: Implement your feature or fix.

5. **Test**: Ensure your changes work correctly:
   ```bash
   ./mvnw test
   ```

6. **Commit**: Save your changes:
   ```bash
   git commit -m "Add feature X"
   ```

7. **Push**: Send your changes to your fork:
   ```bash
   git push origin feature/new-feature
   ```

8. **Create a Pull Request**: Open a Pull Request from your fork on GitHub.

### Code Standards

* Follow Java coding standards
* Comment your code in English
* Write tests for new features
* Respect the existing project architecture

## License

This project is under the **MIT License**. For more details, see the [LICENSE](/LICENSE) file.

## Contact

For any questions about this project, you can contact the development team:

* **Email**: contact@eventora.com
* **Website**: https://www.eventora.com
* **GitHub**: https://github.com/team-eventora

---

© 2023-2024 Eventora Team. All rights reserved. 
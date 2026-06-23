# SmartCart Hyper Market Management System

A comprehensive Java Swing application for managing sales and purchases in large markets with different user roles and modules.

## Project Overview

The SmartCart Hyper Market Management System is designed to streamline operations in large market environments. It provides a complete solution for managing inventory, sales, marketing, customer relations, and administrative tasks.

## System Modules

### 1. Administrative Module
- **User Management**: Add, delete, update, list, and search employees
- **Authentication**: Manage usernames and passwords
- **Access Control**: Role-based access control for different user types

### 2. Marketing Module
- **Product Reports**: Generate comprehensive product performance reports
- **Special Offers**: Create and manage promotional campaigns
- **Inventory Integration**: Send special offers to inventory management

### 3. Inventory Management Module
- **Product Management**: Add, delete, update, list, and search products
- **Stock Monitoring**: Real-time stock level tracking
- **Alerts System**: Notifications for low stock and approaching expiry dates
- **Damage Management**: Handle damaged items and sales returns

### 4. Sales Module
- **Product Search**: Advanced product search functionality
- **Order Processing**: Make and cancel orders efficiently
- **Shopping Cart**: Full cart management system
- **Transaction History**: Complete sales tracking

### 5. User Module
- **Authentication**: Secure login and logout functionality
- **Profile Management**: Update user information (except ID)
- **Action History**: Track and view previous user actions

## Features

### Core Functionality
- **Multi-user Support**: Different roles (Admin, Manager, Marketing, User)
- **Real-time Updates**: Live inventory and sales tracking
- **Comprehensive Reporting**: Detailed reports across all modules
- **Security**: Password hashing and secure authentication
- **User-friendly Interface**: Modern, intuitive Swing GUI

### Advanced Features
- **Dashboard Analytics**: Real-time statistics and metrics
- **Search Functionality**: Advanced search across all modules
- **Data Validation**: Input validation and error handling
- **Export Capabilities**: Export reports and data
- **Notification System**: Real-time alerts and notifications

## Technical Specifications

### Technology Stack
- **Language**: Java
- **GUI Framework**: Java Swing
- **Architecture**: Modular design with separate panels for each module
- **Data Storage**: In-memory data structures (HashMap, ArrayList)

### System Requirements
- **Java**: JDK 8 or higher
- **Operating System**: Windows, macOS, Linux
- **Memory**: Minimum 512MB RAM
- **Storage**: 50MB free space

## File Structure

```
├── Main.java                 # Application entry point
├── Login.java               # Login interface and authentication
├── IDandPassword.java       # User credential management
├── Dashboard.java           # Main dashboard and navigation
├── HomePanel.java           # Home screen with quick stats
├── AdminPanel.java          # Administrative module
├── MarketingPanel.java      # Marketing and reports module
├── InventoryPanel.java      # Inventory management module
├── SalesPanel.java          # Sales and order processing
├── ProfilePanel.java        # User profile management
├── MenuPanel.java           # Navigation menu
├── CustomersPanel.java      # Customer management
├── ReportsPanel.java        # Reports and analytics
└── README.md               # This documentation file
```

## Installation and Setup

### Prerequisites
1. Install Java JDK 8 or higher
2. Ensure Java is in your system PATH

### Running the Application
1. Clone or download the project files
2. Navigate to the project directory
3. Compile the Java files:
   ```bash
   javac *.java
   ```
4. Run the application:
   ```bash
   java Main
   ```

## Default Login Credentials

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | Administrator |
| manager | password1 | Manager |
| marketing | password3 | Marketing |
| user2 | password2 | User |
| default | default123 | User |

## Usage Guide

### Getting Started
1. Launch the application using `java Main`
2. Login with default credentials
3. Navigate through modules using the sidebar menu
4. Access different functionalities based on your user role

### Module Navigation
- **Home**: Dashboard with system overview and quick actions
- **Admin**: User management and system administration
- **Marketing**: Report generation and promotional campaigns
- **Inventory**: Product and stock management
- **Sales**: Order processing and customer transactions
- **Customers**: Customer relationship management
- **Reports**: Comprehensive reporting and analytics
- **Profile**: User account management and history

## Development Information

### Code Structure
- **Modular Design**: Each module is implemented as a separate panel class
- **Event-Driven**: Uses Swing event listeners for user interactions
- **Data Management**: HashMap for user credentials, ArrayList for data storage
- **Security**: SHA-256 password hashing

### Design Patterns
- **MVC Pattern**: Separation of data, view, and controller logic
- **Observer Pattern**: Event handling and UI updates
- **Factory Pattern**: Panel creation and management

## Future Enhancements

### Planned Features
- Database integration (MySQL/PostgreSQL)
- Web-based interface
- Mobile application support
- Advanced analytics and AI-powered insights
- Multi-language support
- Cloud synchronization

### Scalability
- Multi-store support
- Advanced user permissions
- API integration for third-party services
- Real-time collaboration features

## Support and Maintenance

### Troubleshooting
- Ensure Java is properly installed and configured
- Check file permissions if compilation fails
- Verify all required files are present in the directory

### Contributing
1. Follow Java coding conventions
2. Add appropriate comments and documentation
3. Test all new features thoroughly
4. Maintain backward compatibility

## License

This project is developed for educational purposes as part of the Programming Languages 2 course.

## Contact

For questions or support regarding this project, please contact the development team.

---

**Note**: This is a prototype system developed for educational purposes. For production use, consider implementing database integration, enhanced security measures, and additional error handling.

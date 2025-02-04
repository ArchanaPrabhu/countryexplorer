# Country Explorer

## Overview
Country Explorer is an Android application that allows users to explore details about various countries. 
The app fetches country data from a remote API and displays information.

## Features
- Fetch and display a list of countries.
- Scroll position preservation on rotation to maintain user experience.
- Error handling for network failures.
- Uses modern Android architecture components (ViewModel, LiveData, StateFlow).

## Tech Stack
- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit
- **Asynchronous Operations:** Coroutines & Flow
- **UI:** Views with XML
- **State Management:** LiveData, StateFlow

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/country-explorer.git
   ```
2. Open the project in Android Studio.
3. Sync Gradle dependencies.
4. Run the app on an emulator or a physical device.

## API Integration
The app fetches country data from a REST API. Ensure you have the correct API endpoint configured in the repository class.

## Project Structure
```
CountryExplorer/
│── app/
│   ├── src/main/java/com/demo/countryexplorer/
│   │   ├── business/             # Data models
│   │   │   ├── data/             # Data models
│   │   │   │   ├── cache/             # Data models
│   │   │   │   │   ├── abstraction/             # CacheDataSource interfaces
│   │   │   │   │   ├── implementation/          # CacheDataSource classes
│   │   │   │   ├── network/                     
│   │   │   │   │   ├── abstraction/             # NetworkDataSource interface
│   │   │   │   │   ├── implementation/          # NetworkDataSource classes
│   │   │   │   ├── util/             
│   │   │   ├── domain/             
│   │   │   │   ├── model/                       # Concreate classes that can help with ViewState processing
│   │   │   │   ├── state/                       # ViewState processing classes
│   │   │   │   ├── util/                        # Domain layer related utils for transformation of objects Eg. Mappers
│   │   │   ├── interactors/                     
│   │   │   │   ├── countrydetail/               # Repository classes and interfaces
│   │   │   │   ├── countrylist/                 # Repository classes and interfaces
│   │   ├── framework/                           
│   │   │   ├── datasource
│   │   │   │   ├── cache/                       
│   │   │   │   ├── network/                     
│   │   │   │   │   ├── abstraction/             # Service interface to define contracts (Not Retrofit)
│   │   │   │   │   ├── api/                     # Interface implementation for retrofit 
│   │   │   │   │   ├── implementation/          # Implementation of service
│   │   │   │   │   ├── model/                   # Model class for network calls
│   │   │   ├── presentation/                    # 
│   │   │   │   ├── countrydetail/               # Data models
│   │   │   │   ├── countrylist/                 # Data models
│   │   │   │   │   ├── state/                   # ViewState and StateEvent
│   │   ├── util/                                # Utility classes (Error Handling, UI State)
```

## ViewModel Explanation
- More details about the project -[ document link]

## Running Unit Tests
1. Add the following dependencies for testing:
   ```gradle
   testImplementation 'junit:junit:4.13.2'
   androidTestImplementation 'androidx.test.ext:junit:1.1.5'
   androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
   testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1'
   ```
2. Run tests using:
   ```sh
   ./gradlew test
   ```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


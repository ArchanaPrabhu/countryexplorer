# Country Explorer

## Overview
Country Explorer is an Android application that allows users to explore details about various countries. 
The app fetches country data from a remote API and displays information such as name, population, region, and flag.

## Features
- Fetch and display a list of countries.
- Scroll position preservation to maintain user experience.
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
│   │   ├── models/             # Data models
│   │   ├── repository/         # Data fetching logic
│   │   ├── util/               # Utility classes (Error Handling, UI State)
│   │   ├── viewmodel/          # ViewModel layer (Business Logic)
│   │   ├── ui/                 # UI components & Activities
│   │   ├── api/                # Service APIs to execute network calls
```

## ViewModel Explanation
- `CountryListingViewModel` manages the state of country data retrieval and UI updates.
- Uses `StateFlow` for reactive UI updates.
- Handles errors using `CoroutineExceptionHandler`.
- Preserves scroll position using `SavedStateHandle`.

## Error Handling
The project includes `ErrorHandlingCallAdapterFactory` and `ErrorHandlingCall` to wrap API calls and handle failures like:
- Network failures
- API response errors
- JSON deserialization errors

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


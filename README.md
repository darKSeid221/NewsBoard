# NewsBoard - News Article App

Android App to show list of news article , detail article , can be bookmarked and share.

<img width="200" height="400" alt="Screenshot 2025-12-10 at 12 45 19 PM" src="https://github.com/user-attachments/assets/1faccb66-1e5c-4e3e-930f-88db4656629c" />

<img width="200" height="400" alt="Screenshot 2025-12-10 at 12 45 08 PM" src="https://github.com/user-attachments/assets/bcec6502-0898-4e75-b256-19860b98bf93" />

<img width="200" height="400" alt="Screenshot 2025-12-10 at 12 45 24 PM" src="https://github.com/user-attachments/assets/0fe754f4-feec-40ae-96c6-6ab481649508" />

<img width="200" height="400" alt="Screenshot 2025-12-10 at 12 45 02 PM" src="https://github.com/user-attachments/assets/497d7b64-c06a-4a27-bcac-eeeb772026ba" />

## 🏗️ Architecture

**MVVM + Clean Architecture**

```
Presentation → ViewModel → Domain → Data
```

## 🛠️ Tech Stack

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose, Coroutines & Flow
- Min SDK: 24 | Target SDK: 35

## 🚀 Getting Started
1. Clone repository
2. Open in Android Studio
3. Sync Gradle
4. Run app

```bash
./gradlew assembleDebug  # Build
./gradlew test          # Test
```

## 🌿 Branching Strategy

**Rules:**
- ✅ Always checkout from `development` branch
- ✅ Create feature branches: `feature/feature-name`
- ❌ Never push to `main` (protected)

**Workflow:**
```bash
git checkout development
git pull origin development
git checkout -b feature/your-feature-name
# Make changes, commit, push
git push origin feature/your-feature-name
# Create PR to development
```

## 🎯 MVVM Best Practices

- **View**: UI only (Compose Screens)
- **ViewModel**: Business logic, state management (StateFlow/LiveData)
- **Repository**: Single source of truth
- Use `viewModelScope` for coroutines
- Unit test ViewModels

## 🤝 Contributing

1. Follow MVVM architecture
2. Create feature branch from `development`
3. Write clean, tested code
4. Follow code style guidelines

---

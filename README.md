# NewsBoard - News Article App
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

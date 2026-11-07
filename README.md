# Learning Tracker

Kunlik o'rganish jurnali ilovasi. Har kuni soat, vazifalar, kayfiyat, fokus va energiya darajasini qayd eting.

## Funksiyalar

- **Bugungi yozuv** — har kun uchun o'rganish sessiyasi: soat, vazifalar soni, asosiy mavzu, baholash (tushunish, energiya, fokus), g'alaba va qiyinchilik
- **Dashboard** — streak, bugungi holat, 7/30 kunlik o'rtacha ko'rsatkichlar, grafik
- **Tarix** — barcha yozuvlar ro'yxati, sana bo'yicha filter, tahrirlash va o'chirish
- **Haftalik hisobot** — hafta bo'yicha statistika, bar grafik, top g'alabalar va qiyinchiliklar

## Arxitektura

```
data/   — Room (entity, DAO, database), mapper, RepositoryImpl, DataStore
domain/ — model, Repository interfeys, use-case-lar (Android importlarsiz)
ui/     — theme, navigation, components, screen + ViewModel + UiState
```

- **DI**: qo'lda `AppContainer` (`Application` klasida), Hilt/Koin yo'q
- **ViewModel**: har biri bitta `StateFlow<UiState>` chiqaradi
- **Saqlash**: Room (barcha yozuvlar), DataStore (faqat mavzu tanlovi)
- **Navigatsiya**: Navigation Compose, `NavHost`, bitta `Activity`
- **Mavzu**: Material 3, yorug'/qorong'i rejim, DataStore orqali saqlanadi

## Stack

Kotlin · Jetpack Compose · Material 3 · Room (KSP) · DataStore · Navigation Compose · java.time · Canvas (grafiklar)

## Build

```bash
./gradlew assembleDebug   # APK yig'ish
./gradlew testDebugUnitTest  # unit testlar
./gradlew lintDebug          # lint tekshiruvi
```

minSdk 26, targetSdk 35. Package: `uz.learningtracker`.

# Android-Food-Planner-Application
**Food Planner Android App** project using the **MVP design pattern**:

---

## 🍱 Food Planner App

An Android mobile application that helps users plan their weekly meals. Built using Java, MVP architecture, and TheMealDB public API.

---

### 🚀 Features

- 🥘 **Meal of the Day** — Random meal for daily inspiration  
- 🔍 **Search Meals** — By name, category, country, or ingredient  
- 📂 **Browse Categories & Countries** — Discover global cuisines  
- ❤️ **Favorites** — Save meals to local database (Room)  
- 🗓️ **Weekly Planner** — Add meals to your weekly plan  
- 🔐 **Authentication** — Firebase login (Email, Google, Facebook)  
- 📡 **Offline Support** — View favorites and plans without internet  
- 🎬 **Meal Details** — Name, image, ingredients, instructions & embedded video  
- 🎉 **Splash Screen** — Lottie animation  
- 📆 *(Bonus)* Add meals to device calendar

---

### 🧠 Architecture

The app follows the **MVP (Model - View - Presenter)** design pattern for better separation of concerns.

```
com.foodplannerapp
├── model           // Data models (Meal, Category, etc.)
├── view            // Activities & Fragments
├── presenter       // Business logic layer
├── data
│   ├── local       // Room DB
│   └── remote      // Retrofit & Firebase
├── utils           // SharedPrefs, Constants, Helpers
├── network         // Retrofit setup & Network callbacks
```

---

### 🔧 Tech Stack

- **Java**
- **Retrofit** (TheMealDB API)
- **Room Database**
- **Firebase Auth**
- **Lottie Animation**
- **Glide** (Image loading)
- **MVP Architecture**


---


### 📌 Project Milestones

- ✔️ UI Mockups (Figma / Adobe XD)  
- ✔️ Retrofit and Firebase integration  
- ✔️ Offline Room DB + SharedPreferences
- ✔️ MVP structure for all major features  
- ✔️ Trello for task tracking  

---


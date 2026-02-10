[![](https://jitpack.io/v/Team-Namah/swipemodeview-android.svg)](https://jitpack.io/#Team-Namah/swipemodeview-android)

# SwipeModeView 🚀

A highly customizable, smooth, and interactive dual-state swipe view for Android. Perfect for implementing "Swipe to Active," mode toggles, or any binary state switching with a premium feel.


---

## 📽️ Demo & Output

<div align="center">
  <h3>Interactive Swipe Experience</h3>
  
  <!-- If using a GIF -->
  <img src="assets/swipemodeview.gif" width="300" />

  <!-- OR If using a Video (Uncomment below and remove the img tag above) -->
  <!-- 
  <video src="outputs/demo.mp4" width="300" controls muted autoplay loop>
    Your browser does not support the video tag.
  </video> 
  -->
</div>

---

## ✨ Features

- 🎨 **Dynamic Styling**: Customize track colors, thumb colors, and text colors for each state.
- 🏎️ **Smooth Animations**: Built-in hardware-accelerated animations for state transitions.
- 📳 **Haptic Feedback**: Integrated tactile response for a premium user experience.
- ↔️ **Bi-Directional**: Supports both Horizontal and Vertical orientations.
- 🔠 **Custom Icons & Text**: Easily set unique icons and labels for Start and End states.
- 🛠️ **Fully Configurable**: All properties are exposed via XML attributes and Java API.

---

## 📦 Installation

Add the JitPack repository to your `build.gradle` file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.Team-Namah:swipemodeview-android:LATEST_VERSION'
}
```

---

## 🚀 Usage

### XML Implementation

Add `SwipeModeView` to your layout:

```xml
<com.namah.swipemodeview.SwipeModeView
    android:id="@+id/swipeView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:startText="SWIPE TO ACTIVE"
    app:endText="ACTIVE MODE"
    app:leftModeColor="#BFFFFFFF"
    app:rightModeColor="#CE4401"
    app:thumbColor="#000000"
    app:centerTextColor="#000000"
    app:startIcon="@drawable/ic_arrow_right"
    app:endIcon="@drawable/ic_arrow_left"
    app:orientation="horizontal"
    app:hapticsEnabled="true"
    app:animationDuration="250" />
```

### Java Implementation

```java
SwipeModeView swipeModeView = findViewById(R.id.swipeView);

swipeModeView.setOnModeChangeListener(mode -> {
    if (mode == SwipeModeView.Mode.START) {
        // Handle Start state
    } else {
        // Handle End state
    }
});

// Programmatic control
swipeModeView.setMode(SwipeModeView.MODE_END);
```

---

## 🛠️ Attributes

| Attribute | Format | Description |
| :--- | :--- | :--- |
| `leftModeColor` | color | Background color for the Start state. |
| `rightModeColor` | color | Background color for the End state. |
| `thumbColor` | color | Color of the swiping thumb. |
| `centerTextColor`| color | Color of the center label text. |
| `startText` | string | Text displayed in the Start state. |
| `endText` | string | Text displayed in the End state. |
| `startIcon` | reference| Icon displayed on the thumb in Start state. |
| `endIcon` | reference| Icon displayed on the thumb in End state. |
| `orientation` | enum | `horizontal` or `vertical`. |
| `hapticsEnabled` | boolean | Enable/Disable vibration on state change. |
| `animationDuration`| integer | Duration of the transition in ms. |

---

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests.

## 📄 License

MIT License

Copyright (c) 2026 Team Namah

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
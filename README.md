# DrawOnImage

**DrawOnImage** is an Android project that allows users to draw on an image using multiple colors and preview their drawings in real-time. The app leverages **Jetpack Compose** for the UI and provides an interactive drawing canvas that users can use to create, modify, and view their artwork.

## Features

- **Interactive Drawing Canvas**: Users can draw on the image using multiple colors.
- **Multi-color Support**: Select from a variety of colors to customize your drawings.
- **Real-time Preview**: See your drawing in real-time on the canvas overlaid on the image.
- **Clear Canvas**: Clear the entire canvas with a simple button click.
- **Customizable Stroke Width**: Adjust the stroke width for different drawing effects.

## Tech Stack

- **Jetpack Compose**: For creating the modern and responsive UI.
- **Android ViewModel**: For managing UI-related data in a lifecycle-conscious way.
- **Canvas API**: For handling drawing and painting on the canvas.
- **Bitmap Handling**: For manipulating images (e.g., scaling, drawing, and overlaying).

## Setup

### Prerequisites

- Android Studio (preferably the latest stable version)
- Android SDK (API 21+)
- A connected Android device or emulator to run the app

### Clone the Repository

```bash
git clone https://github.com/kirankumar946/DrawOnImage.git
```

### Build and Run the App

1. Open the project in Android Studio.
2. Make sure to sync the project with Gradle files.
3. Run the project on an emulator or physical device.

### Dependencies

- Jetpack Compose libraries
- AndroidX libraries for lifecycle, state management, and UI components
- Kotlin Standard Library

## How It Works

### ViewModel - `DrawingCanvasViewModel`
The **`DrawingCanvasViewModel`** class manages the state of the drawing canvas. It handles the following tasks:

- **Path Management**: Stores the drawing paths and colors used by the user.
- **Bitmap Handling**: Stores the base image (Bitmap) and the final canvas image after drawing.
- **User Interaction**: Handles tap gestures and drag events for drawing paths on the canvas.

### Canvas UI - `DrawingCanvas`
The **`DrawingCanvas`** composable is the UI that displays the drawing canvas. It allows the user to interact with the canvas by tapping and dragging to create paths. It:

- Draws paths and handles touch gestures.
- Allows the user to preview their drawing in real-time.
- Provides options to change the drawing color and stroke width.

### Drawing Features
- The user can start drawing by tapping on the canvas.
- The user can draw continuous lines by dragging their finger.
- A **color picker** lets the user choose from various colors to draw.
- The **clear button** removes all drawn paths and resets the canvas.

## Usage

Once the app is launched:

1. **Draw on Image**: Tap and drag to draw on the canvas over the image.
2. **Choose Color**: Select from predefined colors to change the drawing color.
3. **Preview**: See the drawing overlay in real-time on the image.
4. **Clear Canvas**: Click the clear button to reset the canvas and start drawing again.

## Code Structure

- **MainActivity.kt**: The main entry point of the application, which hosts the **DrawingCanvas** composable.
- **DrawingCanvasViewModel.kt**: The ViewModel responsible for managing drawing paths, colors, and bitmaps.
- **DrawingCanvas.kt**: The composable function responsible for rendering the drawing canvas with touch interactions.
- **Bitmap Handling**: Logic to handle scaling and drawing on images.

## Contributing

We welcome contributions! Feel free to fork the repository, make changes, and create a pull request.

### How to Contribute:
1. Fork the repository.
2. Create a new branch for your changes.
3. Make your changes and commit them.
4. Push your branch to your forked repository.
5. Open a pull request with a detailed description of your changes.

## License

This project is open-source and available under the **MIT License**. See the [LICENSE](LICENSE) file for more information.

---

Feel free to adjust any sections based on your actual app’s features, libraries, or functionality! This template provides a clear and concise overview of the project and its key components.
